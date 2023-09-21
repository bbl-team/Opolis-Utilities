package com.benbenlaw.opolisutilities.screen.slot.utils;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BlacklistTagInputSlot extends SlotItemHandler {

    //EXAMPLE

    //this.addSlot(new ItemInputSlot(handler, 2, 8, 53, Everything but this TAG, maxStackSizeForSlot));

    private final TagKey<Item> tag;
    private final int maxStackSizeForSlot;

    public BlacklistTagInputSlot(IItemHandler itemHandler, int index, int x, int y, TagKey<Item> tag, int maxStackSizeForSlot) {
        super(itemHandler, index, x, y);
        this.tag = tag;
        this.maxStackSizeForSlot = maxStackSizeForSlot;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getCount() <= maxStackSizeForSlot && !stack.is(this.tag);
    }
}