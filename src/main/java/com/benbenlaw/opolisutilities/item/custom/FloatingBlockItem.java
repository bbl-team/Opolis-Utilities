package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;

public class FloatingBlockItem extends Item {
    public FloatingBlockItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {

            int xPos = (int) (player.getX() + 3 * player.getLookAngle().x);
            int yPos = (int) (1.5 + player.getY() + 3 * player.getLookAngle().y);
            int zPos = (int) (player.getZ() + 3 * player.getLookAngle().z);

            BlockPos blockPos = new BlockPos(xPos, yPos, zPos);

            if (level.getBlockState(blockPos).is(Blocks.WATER)) {
                level.setBlockAndUpdate(blockPos, ModBlocks.FLOATING_BLOCK.get().defaultBlockState());
                if (!player.isCreative())
                    player.getItemInHand(hand).shrink(1);

            }

            if (level.getBlockState(blockPos).is(Blocks.AIR)) {
                level.setBlockAndUpdate(blockPos, ModBlocks.FLOATING_BLOCK.get().defaultBlockState());
                if (!player.isCreative())
                    player.getItemInHand(hand).shrink(1);

            }

        } return super.use(level, player, hand);
    }

}
