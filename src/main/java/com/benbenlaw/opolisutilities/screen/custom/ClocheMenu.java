package com.benbenlaw.opolisutilities.screen.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.custom.ClocheBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.ResourceGeneratorBlockEntity;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.screen.ModMenuTypes;
import com.benbenlaw.opolisutilities.screen.slot.utils.ModResultSlot;
import com.benbenlaw.opolisutilities.screen.slot.utils.ResourceGeneratorUpgradeSlot;
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
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ClocheMenu extends AbstractContainerMenu {
    protected ClocheBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public ClocheMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(2));
    }

    public ClocheMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(ModMenuTypes.CLOCHE_MENU.get(), containerID);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.blockEntity = (ClocheBlockEntity) this.level.getBlockEntity(blockPos);
        this.data = data;

        ClocheBlockEntity entity = (ClocheBlockEntity) this.level.getBlockEntity(blockPos);

        checkContainerSize(inventory, 8);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        assert entity != null;

        this.addSlot(new SlotItemHandler(entity.getItemStackHandler(), 0, 35, 18) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.SEED_SLOT);
            }
        });
        this.addSlot(new SlotItemHandler(entity.getItemStackHandler(), 1, 35, 36) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.BLOCK_SLOT);
            }
        });

        this.addSlot(new SlotItemHandler(entity.getItemStackHandler(), 2, 35, 54) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.CATALYST_SLOT);
            }
        });

        this.addSlot(new ResourceGeneratorUpgradeSlot(blockEntity.getItemStackHandler(), 3, 71, 54, level, blockPos) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.SPEED_UPGRADE);
            }
        });

        this.addSlot(new ModResultSlot(entity.getItemStackHandler(), 4, 107, 27));
        this.addSlot(new ModResultSlot(entity.getItemStackHandler(), 5, 125, 27));
        this.addSlot(new ModResultSlot(entity.getItemStackHandler(), 6, 107, 45));
        this.addSlot(new ModResultSlot(entity.getItemStackHandler(), 7, 125, 45));

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



    private static final int TE_INVENTORY_SLOT_COUNT = 8;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
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
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {

        if (player.getItemInHand(player.getUsedItemHand()).is(ModItems.PORTABLE_GUI))
            return true;

        return stillValid(ContainerLevelAccess.create(player.level(), blockPos),
                player, ModBlocks.CLOCHE.get());
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
