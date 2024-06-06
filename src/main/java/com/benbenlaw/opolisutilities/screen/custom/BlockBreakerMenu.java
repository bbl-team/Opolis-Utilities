package com.benbenlaw.opolisutilities.screen.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.custom.BlockBreakerBlockEntity;
import com.benbenlaw.opolisutilities.screen.ModMenuTypes;
import com.benbenlaw.opolisutilities.screen.slot.utils.BlacklistMaxStackSizeOneSlot;
import com.benbenlaw.opolisutilities.screen.slot.utils.WhitelistMaxStackSizeOneSlot;
import com.benbenlaw.opolisutilities.screen.utils.ModSlotTextures;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BlockBreakerMenu extends AbstractContainerMenu {

    protected BlockBreakerBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public BlockBreakerMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(3));
    }

    public BlockBreakerMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(ModMenuTypes.BLOCK_BREAKER_MENU.get(), containerID);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.blockEntity = (BlockBreakerBlockEntity) this.level.getBlockEntity(blockPos);
        this.data = data;

        checkContainerSize(inventory, 3);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        //Add Slots
        assert blockEntity != null;

        this.addSlot(new SlotItemHandler(blockEntity.getItemStackHandler(), 0, 40, 26) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.EMPTY_TOOL_SLOT);
            }
        });
        this.addSlot(new WhitelistMaxStackSizeOneSlot(blockEntity.getItemStackHandler(), 1, 80, 26) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                ItemStack blacklistStack = blockEntity.getItemStackHandler().getStackInSlot(2);
                return blacklistStack.isEmpty() && super.mayPlace(stack);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                if (blockEntity.getItemStackHandler().getStackInSlot(2).isEmpty()) {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.WHITELIST_SLOT);
                }
                else
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.BLOCKED_SLOT);
            }
        });

        this.addSlot(new BlacklistMaxStackSizeOneSlot(blockEntity.getItemStackHandler(), 2, 120, 26) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                ItemStack whitelistStack = blockEntity.getItemStackHandler().getStackInSlot(1);
                return whitelistStack.isEmpty() && super.mayPlace(stack);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                if (blockEntity.getItemStackHandler().getStackInSlot(1).isEmpty()) {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.BLACKLIST_SLOT);
                }
                else
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.BLOCKED_SLOT);
            }
        });


        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0 ;
    }

    public int getScaledProgress() {

        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 26; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;

    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must be the number of slots you have!


    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), blockPos),
                player, ModBlocks.BLOCK_BREAKER.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }


}