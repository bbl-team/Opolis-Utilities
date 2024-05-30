package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.block.custom.RedstoneClockBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock.POWERED;
import static com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock.*;
import static com.benbenlaw.opolisutilities.block.custom.RedstoneClockBlock.*;

public class WrenchItem extends Item {
    public WrenchItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {

        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();

        if (!level.isClientSide()) {

            BlockPos blockPos = pContext.getClickedPos();
            BlockState blockState = level.getBlockState(blockPos);

            if (blockState.is(ModBlocks.ENDER_SCRAMBLER.get())) {

                int currentRange = blockState.getValue(SCRAMBLER_RANGE);

                assert player != null;
                if (player.getMainHandItem().is(ModItems.OPOLIS_WRENCH.get())) {

                    if (!player.isCrouching() && currentRange < maxRange) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(EnderScramblerBlock.SCRAMBLER_RANGE, currentRange + 1));
                        return InteractionResult.SUCCESS;
                    } else if (player.isCrouching() && currentRange > minRange) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(EnderScramblerBlock.SCRAMBLER_RANGE, currentRange - 1));
                        return InteractionResult.SUCCESS;
                    }
                } else if (player.getOffhandItem().is(ModItems.OPOLIS_WRENCH.get())) {
                    if (blockState.getValue(POWERED).equals(true)) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, false));
                        return InteractionResult.SUCCESS;
                    } else if (blockState.getValue(POWERED).equals(false)) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, true));
                        return InteractionResult.SUCCESS;
                    }
                }
            }

            if (blockState.is(ModBlocks.REDSTONE_CLOCK.get())) {

                int currentRange = blockState.getValue(CLOCK_TIMER);

                assert player != null;
                if (player.getMainHandItem().is(ModItems.OPOLIS_WRENCH.get())) {

                    if (!player.isCrouching() && !Screen.hasControlDown() && currentRange < maxTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(RedstoneClockBlock.CLOCK_TIMER, currentRange + 10));
                        return InteractionResult.SUCCESS;
                    } else if (!player.isCrouching() && Screen.hasControlDown() && currentRange < maxTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(RedstoneClockBlock.CLOCK_TIMER, currentRange + 50));
                        return InteractionResult.SUCCESS;
                    } else if (player.isCrouching() && !Screen.hasControlDown() && currentRange > minTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(RedstoneClockBlock.CLOCK_TIMER, currentRange - 10));
                        return InteractionResult.SUCCESS;
                    } else if (player.isCrouching() && Screen.hasControlDown() && currentRange > minTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(RedstoneClockBlock.CLOCK_TIMER, currentRange - 50));
                        return InteractionResult.SUCCESS;
                    }
                }
            }

            if (blockState.is(ModBlocks.BLOCK_BREAKER.get())) {
            }


            if (blockState.is(ModBlocks.BLOCK_PLACER.get())) {

            }

            if (blockState.is(ModBlocks.CRAFTER.get())) {

            }

            return InteractionResult.SUCCESS;

        }

        return null;
    }
}

