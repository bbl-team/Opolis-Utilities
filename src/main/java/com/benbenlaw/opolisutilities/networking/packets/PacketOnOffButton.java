package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock;
import com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.networking.payload.OnOffButtonPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record PacketOnOffButton() {

    public static final PacketOnOffButton INSTANCE = new PacketOnOffButton();

    public static PacketOnOffButton get() {
        return INSTANCE;
    }

    public void handle(final OnOffButtonPayload payload, IPayloadContext context) {

        Player player = context.player();
        Level level = player.level();
        BlockPos blockPos = payload.blockPos();
        BlockState blockState = level.getBlockState(blockPos);


        //Block Placer Power Button
        if (blockState.getBlock() instanceof BlockPlacerBlock) {

            if (blockState.getValue(BlockPlacerBlock.POWERED)) {
                level.setBlockAndUpdate(blockPos, ModBlocks.BLOCK_PLACER.get().defaultBlockState().setValue(BlockPlacerBlock.POWERED, false)
                        .setValue(BlockPlacerBlock.FACING, blockState.getValue(BlockPlacerBlock.FACING))
                        .setValue(BlockPlacerBlock.TIMER, blockState.getValue(BlockPlacerBlock.TIMER)));
            } else {
                level.setBlockAndUpdate(blockPos, ModBlocks.BLOCK_PLACER.get().defaultBlockState().setValue(BlockPlacerBlock.POWERED, true)
                        .setValue(BlockPlacerBlock.FACING, blockState.getValue(BlockPlacerBlock.FACING))
                        .setValue(BlockPlacerBlock.TIMER, blockState.getValue(BlockPlacerBlock.TIMER)));
            }
        }

        //Block Breaker Power Button
        if (blockState.getBlock() instanceof BlockBreakerBlock) {

            if (blockState.getValue(BlockBreakerBlock.POWERED)) {
                level.setBlockAndUpdate(blockPos, ModBlocks.BLOCK_BREAKER.get().defaultBlockState().setValue(BlockBreakerBlock.POWERED, false)
                        .setValue(BlockBreakerBlock.FACING, blockState.getValue(BlockBreakerBlock.FACING)));
            } else {
                level.setBlockAndUpdate(blockPos, ModBlocks.BLOCK_BREAKER.get().defaultBlockState().setValue(BlockBreakerBlock.POWERED, true)
                        .setValue(BlockBreakerBlock.FACING, blockState.getValue(BlockBreakerBlock.FACING)));
            }
        }



        //Crafter Power Button
        if (blockState.getBlock() instanceof CrafterBlock) {

            if (blockState.getValue(CrafterBlock.POWERED)) {
                level.setBlockAndUpdate(blockPos, ModBlocks.CRAFTER.get().defaultBlockState().setValue(CrafterBlock.POWERED, false)
                        .setValue(CrafterBlock.FACING, blockState.getValue(CrafterBlock.FACING)));
            } else {
                level.setBlockAndUpdate(blockPos, ModBlocks.CRAFTER.get().defaultBlockState().setValue(CrafterBlock.POWERED, true)
                        .setValue(CrafterBlock.FACING, blockState.getValue(CrafterBlock.FACING)));
            }
        }
    }
}