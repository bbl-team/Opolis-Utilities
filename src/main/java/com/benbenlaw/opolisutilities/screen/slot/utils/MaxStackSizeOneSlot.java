package com.benbenlaw.opolisutilities.screen.slot.utils;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class MaxStackSizeOneSlot extends SlotItemHandler {
    public MaxStackSizeOneSlot(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getCount() <= 1;
    }


}