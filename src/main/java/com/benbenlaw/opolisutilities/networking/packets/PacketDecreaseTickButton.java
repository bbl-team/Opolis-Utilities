package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.block.custom.RedstoneClockBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.RedstoneClockBlockEntity;
import com.benbenlaw.opolisutilities.networking.payload.DecreaseTickButtonPayload;
import net.minecraft.client.gui.screens.Screen;
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

        //Redstone Clock Decrease Tick Button

        if (blockState.getBlock() instanceof RedstoneClockBlock) {

            int increment = Screen.hasShiftDown() ? 50 : 10;
            int timer = blockState.getValue(RedstoneClockBlock.TIMER);

            if (timer > RedstoneClockBlock.MIN_TIMER) {
                level.setBlockAndUpdate(blockPos, ModBlocks.REDSTONE_CLOCK.get()
                        .defaultBlockState()
                        .setValue(RedstoneClockBlock.TIMER, timer - increment)
                        .setValue(RedstoneClockBlock.POWERED, blockState.getValue(RedstoneClockBlock.POWERED)));

                RedstoneClockBlockEntity entity = (RedstoneClockBlockEntity) level.getBlockEntity(blockPos);
                assert entity != null;
                entity.maxProgress = timer - increment;
                entity.progress = 0;
            }

        }

        //Ender Scrambler Decrease Range Button

        if (blockState.getBlock() instanceof EnderScramblerBlock) {
            int range = blockState.getValue(EnderScramblerBlock.SCRAMBLER_RANGE);
            if (range > EnderScramblerBlock.MIN_RANGE) {
                level.setBlockAndUpdate(blockPos, ModBlocks.ENDER_SCRAMBLER.get().defaultBlockState().setValue(EnderScramblerBlock.SCRAMBLER_RANGE, range - 1)
                        .setValue(EnderScramblerBlock.POWERED, blockState.getValue(EnderScramblerBlock.POWERED)));
            }
        }
    }
}