package com.benbenlaw.opolisutilities.screen.slot.utils;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class WhitelistItemInputSlot extends SlotItemHandler {

    //EXAMPLE

    //this.addSlot(new ItemInputSlot(handler, 2, 8, 53, ITEM, maxStackSizeForSlot));

    private final Item item;
    private final int maxStackSizeForSlot;

    public WhitelistItemInputSlot(IItemHandler itemHandler, int index, int x, int y, Item item, int maxStackSizeForSlot) {
        super(itemHandler, index, x, y);
        this.item = item;
        this.maxStackSizeForSlot = maxStackSizeForSlot;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getCount() <= maxStackSizeForSlot && !stack.is(this.item);
    }
}