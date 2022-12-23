package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.custom.RedstoneClockBlock;
import com.benbenlaw.opolisutilities.block.custom.ResourceGenerator2Block;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class RedstoneClockBlockEntity extends BlockEntity {
    // Add a counter variable
    private int counter = 0;

    public RedstoneClockBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.REDSTONE_CLOCK_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, RedstoneClockBlockEntity entity) {
        // Increment the counter
        entity.counter++;

        // Only execute the rest of the code if the counter is a multiple of 20

        int tickRate = 80;

        /*

        if (level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(ModTags.Blocks.RESOURCE_GENERATOR_SPEED_BLOCKS_1)) {
            tickRate = 60;
        }

        if (level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(ModTags.Blocks.RESOURCE_GENERATOR_SPEED_BLOCKS_2)) {
            tickRate = 40;
        }

        if (level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(ModTags.Blocks.RESOURCE_GENERATOR_SPEED_BLOCKS_3)) {
            tickRate = 20;
        }

         */

        if (entity.counter % tickRate == 0) {

            BlockState powered = level.getBlockState(blockPos).setValue(RedstoneClockBlock.POWERED, true);
            level.setBlockAndUpdate(blockPos, powered);

        }

        else if (entity.counter % tickRate == 40) {
            BlockState unPowered = level.getBlockState(blockPos).setValue(RedstoneClockBlock.POWERED, false);
            level.setBlockAndUpdate(blockPos, unPowered);
        }
    }
}