package com.benbenlaw.opolisutilities.screen.slot.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ResourceGeneratorInputSlot extends SlotItemHandler {

    private final Level level;
    private final BlockPos blockPos;
    public ResourceGeneratorInputSlot(IItemHandler itemHandler, int index, int x, int y, Level level, BlockPos blockPos) {
        super(itemHandler, index, x, y);
        this.level = level;
        this.blockPos = blockPos;
    }
}