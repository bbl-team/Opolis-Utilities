package com.benbenlaw.opolisutilities.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Optional;

public final class ItemCapabilityMenuHelper {
    private ItemCapabilityMenuHelper() {}

    public static Optional<IItemHandler> getCapabilityItemHandler(Level level, BlockEntity blockEntity) {

        Direction side = blockEntity.getBlockState().getValue(BlockStateProperties.FACING);

        return Optional.ofNullable(level.getCapability(Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(),
                blockEntity.getBlockState(), blockEntity, null));
    }
}