package com.benbenlaw.opolisutilities.item.custom;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class SaplingGrower extends Item {
    public SaplingGrower(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.screen == null) {
                HitResult hitResult = minecraft.hitResult;
                if (hitResult instanceof BlockHitResult hit) {
                    BlockPos blockPos = hit.getBlockPos();
                    assert minecraft.level != null;
                    BlockState blockState = minecraft.level.getBlockState(blockPos);
                    if (blockState.is(BlockTags.SAPLINGS)) {
                        applyBonemeal(player.getItemInHand(hand), level, blockPos, player);
                        return InteractionResultHolder.success(this.getDefaultInstance());
                    }
                }
            }
        }
        return super.use(level, player, hand);
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
                        bonemealableblock.performBonemeal((ServerLevel)level, level.random, blockPos, blockstate);
                    }
                    stack.shrink(1);
                }
            }
        }

    }



}
