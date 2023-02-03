package com.benbenlaw.opolisutilities.block.entity.custom;

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



public class ResourceGenerator2BlockEntity extends BlockEntity {
    // Add a counter variable
    private int counter = 0;

    public ResourceGenerator2BlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.RESOURCE_GENERATOR_2_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ResourceGenerator2BlockEntity entity) {
        // Increment the counter
        entity.counter++;

        // Only execute the rest of the code if the counter is a multiple of 20

        int tickRate = 80;

        if (level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(ModTags.Blocks.RESOURCE_GENERATOR_SPEED_BLOCKS_1)) {
            tickRate = 60;
        }

        if (level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(ModTags.Blocks.RESOURCE_GENERATOR_SPEED_BLOCKS_2)) {
            tickRate = 40;
        }

        if (level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(ModTags.Blocks.RESOURCE_GENERATOR_SPEED_BLOCKS_3)) {
            tickRate = 20;
        }

        if (entity.counter % tickRate == 0) {
            if (level.getBlockState(blockPos.above()).getBlockHolder().containsTag(ModTags.Blocks.RESOURCE_GENERATOR_BLOCKS)) {

                BlockState notWorkingState = level.getBlockState(blockPos).setValue(ResourceGenerator2Block.LIT, false);
                level.setBlockAndUpdate(blockPos, notWorkingState);

                if (level.getBlockEntity(blockPos.below()) != null) {

                    BlockState workingState = level.getBlockState(blockPos).setValue(ResourceGenerator2Block.LIT, true);
                    level.setBlockAndUpdate(blockPos, workingState);

                    BlockEntity ent = level.getBlockEntity(blockPos.below());
                    Block blockAbove = level.getBlockState(blockPos.above()).getBlock();
                    assert ent != null;
                    ent.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(itemHandler -> {

                                ItemStack stack = new ItemStack(blockAbove.asItem());

                                for (int i = 0; i < itemHandler.getSlots(); i++) {
                                    if (itemHandler.isItemValid(i, stack) && itemHandler.insertItem(i, stack, true).isEmpty()) {
                                        itemHandler.insertItem(i, stack, false);
                                        break;
                                    }
                                }
                            }

                    );
                }
            }
        }
    }
}