package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.custom.BlockBreakerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.screen.slot.utils.ModResultSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrafterMenu extends AbstractContainerMenu {
    protected CrafterBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public CrafterMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(10));
    }

    public CrafterMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(ModMenuTypes.CRAFTER_MENU.get(), containerID);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.blockEntity = (CrafterBlockEntity) this.level.getBlockEntity(blockPos);
        this.data = data;

        checkContainerSize(inventory, 10);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        assert blockEntity != null;
        this.addSlot(new SlotItemHandler(blockEntity.getItemStackHandler(), 0, 30, 17)); // table
        this.addSlot(new SlotItemHandler(blockEntity.getItemStackHandler(), 1, 48, 17)); // table
        this.addSlot(new SlotItemHandler(blockEntity.getItemStackHandler(), 2, 66, 17)); // table
        this.addSlot(new SlotItemHandler(blockEntity.getItemStackHandler(), 3, 30, 35)); // table
        this.addSlot(new SlotItemHandler(blockEntity.getItemStackHandler(), 4, 48, 35)); // table
        this.addSlot(new SlotItemHandler(blockEntity.getItemStackHandler(), 5, 66, 35)); // table
        this.addSlot(new SlotItemHandler(blockEntity.getItemStackHandler(), 6, 30, 53)); // table
        this.addSlot(new SlotItemHandler(blockEntity.getItemStackHandler(), 7, 48, 53)); // table
        this.addSlot(new SlotItemHandler(blockEntity.getItemStackHandler(), 8, 66, 53)); // table

        this.addSlot(new ModResultSlot(blockEntity.getItemStackHandler(), 9, 124, 35)); //result

        addDataSlots(data);
    }

    @Nullable
    public CrafterBlockEntity getBlockEntity() {
        return blockEntity;
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

    private static final int TE_INVENTORY_SLOT_COUNT = 10;  // must be the number of slots you have!

    @Override
    public @NotNull ItemStack quickMoveStack(Player playerIn, int index) {
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
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }


    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), blockPos),
                player, ModBlocks.CRAFTER.get());
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
