package com.benbenlaw.opolisutilities.screen.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BlacklistMaxStackSizeOneSlot extends SlotItemHandler {
    public BlacklistMaxStackSizeOneSlot(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getCount() <= 1;

    }

}