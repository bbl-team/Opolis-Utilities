package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.*;
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
                        .setValue(BlockPlacerBlock.FACING, blockState.getValue(BlockPlacerBlock.FACING)));
            } else {
                level.setBlockAndUpdate(blockPos, ModBlocks.BLOCK_PLACER.get().defaultBlockState().setValue(BlockPlacerBlock.POWERED, true)
                        .setValue(BlockPlacerBlock.FACING, blockState.getValue(BlockPlacerBlock.FACING)));
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

        //Item Repairer Power Button
        if (blockState.getBlock() instanceof ItemRepairerBlock) {

            if (blockState.getValue(ItemRepairerBlock.POWERED)) {
                level.setBlockAndUpdate(blockPos, ModBlocks.ITEM_REPAIRER.get().defaultBlockState().setValue(ItemRepairerBlock.POWERED, false)
                        .setValue(ItemRepairerBlock.FACING, blockState.getValue(ItemRepairerBlock.FACING)));
            } else {
                level.setBlockAndUpdate(blockPos, ModBlocks.ITEM_REPAIRER.get().defaultBlockState().setValue(ItemRepairerBlock.POWERED, true)
                        .setValue(ItemRepairerBlock.FACING, blockState.getValue(ItemRepairerBlock.FACING)));
            }
        }

        //Summoning Block Power Button
        if (blockState.getBlock() instanceof SummoningBlock) {

            if (blockState.getValue(SummoningBlock.POWERED)) {
                level.setBlockAndUpdate(blockPos, ModBlocks.SUMMONING_BLOCK.get().defaultBlockState().setValue(SummoningBlock.POWERED, false)
                        .setValue(SummoningBlock.FACING, blockState.getValue(SummoningBlock.FACING)));
            } else {
                level.setBlockAndUpdate(blockPos, ModBlocks.SUMMONING_BLOCK.get().defaultBlockState().setValue(SummoningBlock.POWERED, true)
                        .setValue(SummoningBlock.FACING, blockState.getValue(SummoningBlock.FACING)));
            }
        }

        //Ender Scrambler Power Button
        if (blockState.getBlock() instanceof EnderScramblerBlock) {

            if (blockState.getValue(EnderScramblerBlock.POWERED)) {
                level.setBlockAndUpdate(blockPos, ModBlocks.ENDER_SCRAMBLER.get().defaultBlockState().setValue(EnderScramblerBlock.POWERED, false));
            } else {
                level.setBlockAndUpdate(blockPos, ModBlocks.ENDER_SCRAMBLER.get().defaultBlockState().setValue(EnderScramblerBlock.POWERED, true));
            }
        }

        //Cloche Power Button
        if (blockState.getBlock() instanceof ClocheBlock) {

            if (blockState.getValue(ClocheBlock.POWERED)) {
                level.setBlockAndUpdate(blockPos, ModBlocks.CLOCHE.get().defaultBlockState().setValue(ClocheBlock.POWERED, false));
            } else {
                level.setBlockAndUpdate(blockPos, ModBlocks.CLOCHE.get().defaultBlockState().setValue(ClocheBlock.POWERED, true));
            }
        }

    }
}