package com.benbenlaw.opolisutilities.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class MiniCoalItem extends Item {

    final boolean hasBurnTime;

    public MiniCoalItem(boolean burnTime) {
        super(new Properties());

        this.hasBurnTime = burnTime;

    }
    @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> type) {
        if(this.hasBurnTime) {
            return 200;
        }

        return -1;
    }
}
