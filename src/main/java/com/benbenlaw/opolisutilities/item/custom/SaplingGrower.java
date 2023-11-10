package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import static com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock.*;
import static com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock.POWERED;

public class SaplingGrower extends Item {
    public SaplingGrower(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {

        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();

        if (!level.isClientSide()) {
            BlockPos blockPos = pContext.getClickedPos();
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(BlockTags.SAPLINGS)) {
                assert player != null;
                if (player.getItemInHand(InteractionHand.MAIN_HAND).is(this)) {
                    applyBonemeal(player.getItemInHand(InteractionHand.MAIN_HAND), level, blockPos, player);
                } else if (player.getItemInHand(InteractionHand.OFF_HAND).is(this)) {
                    applyBonemeal(player.getItemInHand(InteractionHand.OFF_HAND), level, blockPos, player);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    public static void applyBonemeal(ItemStack stack, Level level, BlockPos blockPos, Player player) {
        BlockState blockstate = level.getBlockState(blockPos);
        int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, level, blockPos, blockstate, stack);
        if (hook != 0) return;
        if (blockstate.getBlock() instanceof BonemealableBlock) {
            BonemealableBlock bonemealableblock = (BonemealableBlock)blockstate.getBlock();
            if (bonemealableblock.isValidBonemealTarget(level, blockPos, blockstate, level.isClientSide)) {
                if (level instanceof ServerLevel) {
                    if (bonemealableblock.isBonemealSuccess(level, level.random, blockPos, blockstate)) {
                        bonemealableblock.performBonemeal((ServerLevel) level, level.random, blockPos, blockstate);
                    }

                    //Dont remove till 2.3.0
                }
            }
        }

    }



}