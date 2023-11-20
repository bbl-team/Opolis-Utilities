package com.benbenlaw.opolisutilities.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

//Thanks To https://github.com/bl4ckscor3/Growmeal/tree/1.19/src/main for the instant effect, changed for opolis utilities
public class SaplingGrower extends Item {
    public SaplingGrower(Properties p_41383_) {
        super(p_41383_);
    }

    public static int totalGrowthAttempts = 128; //ConfigFile.totalGrowthAttempts.get();

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {

        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);

        if (!level.isClientSide()) {

            if (blockState.is(BlockTags.SAPLINGS)) {
                tryToGrow(level, blockPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    public void tryToGrow(Level level, BlockPos pos) {
        for (int i = 0; i < totalGrowthAttempts; i++) {
            doGrow(level, pos);
        }
    }

    public void doGrow(Level level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        if (!level.isClientSide && blockState.getBlock() instanceof BonemealableBlock bonemealableBlock)
            bonemealableBlock.performBonemeal((ServerLevel) level, level.random, pos, blockState);
    }
}
