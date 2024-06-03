package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock;
import com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.custom.RedstoneClockBlock;
import com.benbenlaw.opolisutilities.networking.payload.IncreaseTickButtonPayload;
import com.benbenlaw.opolisutilities.networking.payload.OnOffButtonPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record PacketIncreaseTickButton() {

    public static final PacketIncreaseTickButton INSTANCE = new PacketIncreaseTickButton();

    public static PacketIncreaseTickButton get() {
        return INSTANCE;
    }

    public void handle(final IncreaseTickButtonPayload payload, IPayloadContext context) {

        Player player = context.player();
        Level level = player.level();
        BlockPos blockPos = payload.blockPos();
        BlockState blockState = level.getBlockState(blockPos);


        //Block Placer Increase Tick Button
        if (blockState.getBlock() instanceof BlockPlacerBlock) {
            int timer = blockState.getValue(BlockPlacerBlock.TIMER);
            if (timer < BlockPlacerBlock.MAX_TIMER) {
                level.setBlockAndUpdate(blockPos, ModBlocks.BLOCK_PLACER.get().defaultBlockState().setValue(BlockPlacerBlock.TIMER, timer + 10)
                        .setValue(BlockPlacerBlock.FACING, blockState.getValue(BlockPlacerBlock.FACING))
                        .setValue(BlockPlacerBlock.POWERED, blockState.getValue(BlockPlacerBlock.POWERED)));
            }
        }

        //Crafter Increase Tick Button
        if (blockState.getBlock() instanceof CrafterBlock) {
            int timer = blockState.getValue(CrafterBlock.TIMER);
            if (timer < CrafterBlock.MAX_TIMER) {
                level.setBlockAndUpdate(blockPos, ModBlocks.CRAFTER.get().defaultBlockState().setValue(CrafterBlock.TIMER, timer + 10)
                        .setValue(CrafterBlock.FACING, blockState.getValue(CrafterBlock.FACING))
                        .setValue(CrafterBlock.POWERED, blockState.getValue(CrafterBlock.POWERED)));
            }
        }

        //Redstone Clock Increase Tick Button
        if (blockState.getBlock() instanceof RedstoneClockBlock) {
            int timer = blockState.getValue(RedstoneClockBlock.TIMER);
            if (timer < RedstoneClockBlock.MAX_TIMER) {
                level.setBlockAndUpdate(blockPos, ModBlocks.REDSTONE_CLOCK.get().defaultBlockState().setValue(RedstoneClockBlock.TIMER, timer + 10)
                        .setValue(RedstoneClockBlock.POWERED, blockState.getValue(RedstoneClockBlock.POWERED)));
            }
        }
    }
}