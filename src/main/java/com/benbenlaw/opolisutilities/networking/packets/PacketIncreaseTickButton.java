package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.block.custom.RedstoneClockBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.BlockPlacerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.EnderScramblerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.RedstoneClockBlockEntity;
import com.benbenlaw.opolisutilities.networking.payload.IncreaseTickButtonPayload;
import com.benbenlaw.opolisutilities.screen.utils.ConfigValues;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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
        boolean isShiftDown = payload.isShiftDown();
        BlockEntity blockEntity = level.getBlockEntity(blockPos);


        //Block Placer Increase Tick Button
        if (blockEntity instanceof BlockPlacerBlockEntity blockPlacerBlockEntity) {
            int timer = blockPlacerBlockEntity.maxProgress;
            int increment = isShiftDown ? 50 : 10;
            if (timer < 2000) {
                blockPlacerBlockEntity.maxProgress = timer + increment;

            }
        }

        //Crafter Increase Tick Button
        if (blockEntity instanceof CrafterBlockEntity crafterBlockEntity) {
            int timer = crafterBlockEntity.maxProgress;
            int increment = isShiftDown ? 50 : 10;

            if (timer < 1200) {
                crafterBlockEntity.maxProgress = timer + increment;

            }
        }

        //Redstone Clock Increase Tick Button
        if (blockEntity instanceof RedstoneClockBlockEntity redstoneClockBlockEntity) {

            int increment = isShiftDown ? 50 : 10;
            int timer = redstoneClockBlockEntity.maxProgress;

            if (timer < 1200) {
                redstoneClockBlockEntity.maxProgress = timer + increment;
                RedstoneClockBlockEntity entity = (RedstoneClockBlockEntity) level.getBlockEntity(blockPos);
                assert entity != null;
                entity.maxProgress = timer + increment;
                entity.progress = 0;
            }

        }

        //Ender Scrambler Increase Range Button
        if (blockEntity instanceof EnderScramblerBlockEntity enderScramblerBlockEntity) {
            int increment = isShiftDown ? 3 : 1;
            int range = enderScramblerBlockEntity.SCRAMBLER_RANGE;
            int maxRange = ConfigValues.ENDER_SCRAMBLER_MAX_RANGE;

            if (range + increment > maxRange) {
                increment = maxRange - range;
            }

            if (range < maxRange) {
                enderScramblerBlockEntity.SCRAMBLER_RANGE = range + increment;
            }
        }
    }
}