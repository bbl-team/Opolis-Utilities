package com.benbenlaw.opolisutilities.screen.slot.utils;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class WhitelistTagInputSlot extends SlotItemHandler {

    //EXAMPLE

    //this.addSlot(new ItemInputSlot(handler, 2, 8, 53, TAGS, maxStackSizeForSlot));

    private final TagKey<Item> tag;
    private final int maxStackSizeForSlot;

    public WhitelistTagInputSlot(IItemHandler itemHandler, int index, int x, int y, TagKey<net.minecraft.world.item.Item> tag, int maxStackSizeForSlot) {
        super(itemHandler, index, x, y);
        this.tag = tag;
        this.maxStackSizeForSlot = maxStackSizeForSlot;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getCount() <= maxStackSizeForSlot && stack.is(this.tag);
    }
}