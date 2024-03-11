package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.*;
import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.benbenlaw.opolisutilities.block.custom.CrafterBlock.TIMER;
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
                    }

                    else if (!player.isCrouching() && Screen.hasControlDown() && currentRange < maxTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(RedstoneClockBlock.CLOCK_TIMER, currentRange + 50));
                        return InteractionResult.SUCCESS;
                    }

                    else if (player.isCrouching() && !Screen.hasControlDown() && currentRange > minTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(RedstoneClockBlock.CLOCK_TIMER, currentRange - 10));
                        return InteractionResult.SUCCESS;
                    }

                    else if (player.isCrouching() && Screen.hasControlDown() && currentRange > minTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(RedstoneClockBlock.CLOCK_TIMER, currentRange - 50));
                        return InteractionResult.SUCCESS;
                    }
                }
            }

            if (blockState.is(ModBlocks.BLOCK_BREAKER.get())) {

                int currentRange = blockState.getValue(BlockBreakerBlock.TIMER);

                assert player != null;
                if (player.getMainHandItem().is(ModItems.OPOLIS_WRENCH.get())) {

                    if (player.isCrouching() && !Screen.hasControlDown() && currentRange < BlockBreakerBlock.maxTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(BlockBreakerBlock.TIMER, currentRange + 10));
                        return InteractionResult.SUCCESS;
                    }

                    else if (player.isCrouching() && Screen.hasControlDown() && currentRange > BlockBreakerBlock.minTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(BlockBreakerBlock.TIMER, currentRange - 10));
                        return InteractionResult.SUCCESS;
                    }

                }
            }

            if (blockState.is(ModBlocks.BLOCK_PLACER.get())) {

                int currentRange = blockState.getValue(BlockPlacerBlock.TIMER);

                assert player != null;
                if (player.getMainHandItem().is(ModItems.OPOLIS_WRENCH.get())) {

                    if (player.isCrouching() && !Screen.hasControlDown() && currentRange < BlockPlacerBlock.maxTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(BlockPlacerBlock.TIMER, currentRange + 10));
                        return InteractionResult.SUCCESS;
                    }

                    else if (player.isCrouching() && Screen.hasControlDown() && currentRange > BlockPlacerBlock.minTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(BlockPlacerBlock.TIMER, currentRange - 10));
                        return InteractionResult.SUCCESS;
                    }
                }
            }

            if (blockState.is(ModBlocks.CRAFTER.get())) {

                int currentTimer = blockState.getValue(TIMER);

                assert player != null;

                if (player.getMainHandItem().is(this)) {

                    if (blockState.getValue(CrafterBlock.POWERED).equals(true)) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(CrafterBlock.POWERED, false));
                    } else if (blockState.getValue(CrafterBlock.POWERED).equals(false)) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(CrafterBlock.POWERED, true));
                    }
                }

                if (player.getOffhandItem().is(this)) {

                    if (!Screen.hasControlDown() && currentTimer < CrafterBlock.maxTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(CrafterBlock.TIMER, currentTimer + 10));
                        return InteractionResult.SUCCESS;
                    } else if (Screen.hasControlDown() && currentTimer > CrafterBlock.minTimer) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(CrafterBlock.TIMER, currentTimer - 10));
                        return InteractionResult.SUCCESS;
                    }
                }


            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.wrench_item.shift.held")
                    .withStyle(ChatFormatting.GREEN));
        }
        else {
            components.add(Component.translatable("tooltips.scrambler_item.hover.shift").withStyle(ChatFormatting.BLUE));
        }
    }

}

