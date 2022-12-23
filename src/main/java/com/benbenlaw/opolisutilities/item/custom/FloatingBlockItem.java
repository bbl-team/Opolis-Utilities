package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class FloatingBlockItem extends Item {
    public FloatingBlockItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {


            double xPos = player.getX() + 3 * player.getLookAngle().x;
            double yPos = 1.5 + player.getY() + 3 * player.getLookAngle().y;
            double zPos = player.getZ() + 3 * player.getLookAngle().z;

        BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
    //    if (level.getBlockState(blockPos).is(Blocks.WATER) || !level.getBlockState(blockPos).is(Blocks.AIR) ) {
    //        player.sendSystemMessage(Component.translatable("tooltips.floating_block.place_in_air")
    //                .withStyle(ChatFormatting.RED));
    //   }

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
