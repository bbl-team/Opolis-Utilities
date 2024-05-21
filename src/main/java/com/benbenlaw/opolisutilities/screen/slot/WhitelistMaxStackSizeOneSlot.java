package com.benbenlaw.opolisutilities.screen.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;

public class WhitelistMaxStackSizeOneSlot extends SlotItemHandler {
    public WhitelistMaxStackSizeOneSlot(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getCount() <= 1;
    }
    @Override
    public int getMaxStackSize(ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        int maxInput = 1;
        maxAdd.setCount(maxInput);

        IItemHandler handler = this.getItemHandler();
        int index = this.getSlotIndex();
        ItemStack currentStack = handler.getStackInSlot(index);
        if (handler instanceof IItemHandlerModifiable handlerModifiable) {

            handlerModifiable.setStackInSlot(index, ItemStack.EMPTY);
            ItemStack remainder = handlerModifiable.insertItem(index, maxAdd, true);
            handlerModifiable.setStackInSlot(index, currentStack);

            return maxInput - remainder.getCount();
        } else {
            ItemStack remainder = handler.insertItem(index, maxAdd, true);

            int current = currentStack.getCount();
            int added = maxInput - remainder.getCount();
            return current + added;
        }
    }

}