package com.benbenlaw.opolisutilities.screen.slot.utils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class BlacklistItemInputSlot extends SlotItemHandler {

    //EXAMPLE

    //this.addSlot(new ItemInputSlot(handler, 2, 8, 53, Everything but this ITEM, maxStackSizeForSlot));

    private final Item item;
    private final int maxStackSizeForSlot;

    public BlacklistItemInputSlot(IItemHandler itemHandler, int index, int x, int y, Item item, int maxStackSizeForSlot) {
        super(itemHandler, index, x, y);
        this.item = item;
        this.maxStackSizeForSlot = maxStackSizeForSlot;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getCount() <= maxStackSizeForSlot && !stack.is(this.item);
    }
}