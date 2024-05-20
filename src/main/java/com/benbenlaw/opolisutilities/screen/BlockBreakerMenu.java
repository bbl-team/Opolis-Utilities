package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.custom.BlockBreakerBlockEntity;
import com.benbenlaw.opolisutilities.screen.slot.BlacklistMaxStackSizeOneSlot;
import com.benbenlaw.opolisutilities.screen.slot.WhitelistMaxStackSizeOneSlot;
import com.benbenlaw.opolisutilities.util.ItemCapabilityMenuHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.SlotItemHandler;

public class BlockBreakerMenu extends AbstractContainerMenu {
    protected BlockBreakerBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public BlockBreakerMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public BlockBreakerMenu(int containerID, Inventory inventory, BlockPos blockPos) {
        super(ModMenuTypes.BLOCK_BREAKER_MENU.get(), containerID);
        this.player = inventory.player;
        this.blockPos = blockPos;

    //    checkContainerSize(inventory, 3);

    //    addPlayerInventory(inventory);
    //    addPlayerHotbar(inventory);
    //    addDataSlots(data);


    }

    public BlockBreakerMenu(int containerID, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.BLOCK_BREAKER_MENU.get(), containerID);

        this.data = data;
        this.blockEntity = ((BlockBreakerBlockEntity) entity);
        this.level = inventory.player.level();

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        /*
        ItemCapabilityMenuHelper.getCapabilityItemHandler(this.level, this.blockEntity).ifPresent(itemHandler -> {
            addSlot(new SlotItemHandler(itemHandler, 0, 40, 40));
        });

         */


        this.addSlot(new SlotItemHandler(this.blockEntity.getItemStackHandler(), 0, 40, 40));
        this.addSlot(new WhitelistMaxStackSizeOneSlot(blockEntity.getItemStackHandler(), 1, 80, 40) {
                         @Override
                         public boolean mayPlace(ItemStack stack) {
                             ItemStack blacklistStack = blockEntity.getItemStackHandler().getStackInSlot(2);
                             return blacklistStack.isEmpty() && super.mayPlace(stack);
                         }
                     });

        this.addSlot(new BlacklistMaxStackSizeOneSlot(blockEntity.getItemStackHandler(), 2, 120, 40) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                ItemStack whitelistStack = blockEntity.getItemStackHandler().getStackInSlot(1);
                return whitelistStack.isEmpty() && super.mayPlace(stack);
            }
        });





            /*
            this.addSlot(new WhitelistMaxStackSizeOneSlot(blockEntity.getItemStackHandler(), 1, 80, 40) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    ItemStack blacklistStack = blockEntity.getItemStackHandler().getStackInSlot(2);
                    return blacklistStack.isEmpty() && super.mayPlace(stack);
                }
            });
            this.addSlot(new BlacklistMaxStackSizeOneSlot(blockEntity.getItemStackHandler(), 2, 120, 40) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    ItemStack whitelistStack = blockEntity.getItemStackHandler().getStackInSlot(1);
                    return whitelistStack.isEmpty() && super.mayPlace(stack);
                }
            });

             */

        addDataSlots(data);




    }










        /*
        checkContainerSize(inventory, 3);
        blockEntity = ((BlockBreakerBlockEntity) entity);
        this.level = inventory.player.level();
        this.data = data;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        /*
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 40, 40));
            this.addSlot(new WhitelistMaxStackSizeOneSlot(handler, 1, 80, 40) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    ItemStack blacklistStack = handler.getStackInSlot(2);
                    return blacklistStack.isEmpty() && super.mayPlace(stack);
                }
            });
            this.addSlot(new BlacklistMaxStackSizeOneSlot(handler, 2, 120, 40) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    ItemStack whitelistStack = handler.getStackInSlot(1);
                    return whitelistStack.isEmpty() && super.mayPlace(stack);
                }
            });
        });




        */



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
    public boolean stillValid(Player pPlayer) {
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
