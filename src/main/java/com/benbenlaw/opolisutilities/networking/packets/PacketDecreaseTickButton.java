package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.networking.payload.DecreaseTickButtonPayload;
import com.benbenlaw.opolisutilities.networking.payload.IncreaseTickButtonPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record PacketDecreaseTickButton() {

    public static final PacketDecreaseTickButton INSTANCE = new PacketDecreaseTickButton();

    public static PacketDecreaseTickButton get() {
        return INSTANCE;
    }

    public void handle(final DecreaseTickButtonPayload payload, IPayloadContext context) {

        Player player = context.player();
        Level level = player.level();
        BlockPos blockPos = payload.blockPos();
        BlockState blockState = level.getBlockState(blockPos);


        //Block Placer Decrease Tick Button
        if (blockState.getBlock() instanceof BlockPlacerBlock) {
            int timer = blockState.getValue(BlockPlacerBlock.TIMER);
            if (timer > BlockPlacerBlock.MIN_TIMER) {
                level.setBlockAndUpdate(blockPos, ModBlocks.BLOCK_PLACER.get().defaultBlockState().setValue(BlockPlacerBlock.TIMER, timer - 10)
                        .setValue(BlockPlacerBlock.FACING, blockState.getValue(BlockPlacerBlock.FACING))
                        .setValue(BlockPlacerBlock.POWERED, blockState.getValue(BlockPlacerBlock.POWERED)));
            }
        }

        //Crafter Decrease Tick Button

        if (blockState.getBlock() instanceof CrafterBlock) {
            int timer = blockState.getValue(CrafterBlock.TIMER);
            if (timer > CrafterBlock.MIN_TIMER) {
                level.setBlockAndUpdate(blockPos, ModBlocks.CRAFTER.get().defaultBlockState().setValue(CrafterBlock.TIMER, timer - 10)
                        .setValue(CrafterBlock.FACING, blockState.getValue(CrafterBlock.FACING))
                        .setValue(CrafterBlock.POWERED, blockState.getValue(CrafterBlock.POWERED)));
            }
        }
    }
}