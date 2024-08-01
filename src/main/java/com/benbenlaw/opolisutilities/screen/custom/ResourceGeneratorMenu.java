package com.benbenlaw.opolisutilities.screen.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.custom.ResourceGeneratorBlockEntity;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.screen.ModMenuTypes;
import com.benbenlaw.opolisutilities.screen.slot.utils.ModResultSlot;
import com.benbenlaw.opolisutilities.screen.slot.utils.ResourceGeneratorInputSlot;
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
import org.jetbrains.annotations.NotNull;

public class ResourceGeneratorMenu extends AbstractContainerMenu {
    protected ResourceGeneratorBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public ResourceGeneratorMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(3));

    }

    public ResourceGeneratorMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(ModMenuTypes.RESOURCE_GENERATOR_MENU.get(), containerID);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.data = data;
        this.blockEntity = (ResourceGeneratorBlockEntity) this.level.getBlockEntity(blockPos);

        checkContainerSize(inventory, 3);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        assert blockEntity != null;

        this.addSlot(new ResourceGeneratorInputSlot(blockEntity.getItemStackHandler(), ResourceGeneratorBlockEntity.INPUT_SLOT, 80, 16, level, blockPos) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {

                if (level.getBlockState(blockPos.above(1)).is(Blocks.AIR)) {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.BLOCK_SLOT);
                }
                return null;
            }

            @Override
            public boolean isFake() {
                return super.isFake();
            }
        });

        this.addSlot(new ResourceGeneratorUpgradeSlot(blockEntity.getItemStackHandler(), ResourceGeneratorBlockEntity.UPGRADE_SLOT, 116, 16, level, blockPos) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {

                if (level.getBlockState(blockPos.above(2)).is(Blocks.AIR)) {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, ModSlotTextures.SPEED_UPGRADE);
                }
                return null;
            }
        });

        this.addSlot(new ModResultSlot(blockEntity.getItemStackHandler(), ResourceGeneratorBlockEntity.OUTPUT_SLOT, 80, 64));
        addDataSlots(data);
    }


    public boolean isCrafting() {
        return data.get(0) > 0;
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
                player, ModBlocks.RESOURCE_GENERATOR.get());
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
