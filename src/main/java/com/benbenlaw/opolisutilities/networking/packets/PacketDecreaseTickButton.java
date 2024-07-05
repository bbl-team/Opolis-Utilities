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
import com.benbenlaw.opolisutilities.networking.payload.DecreaseTickButtonPayload;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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
        BlockEntity blockEntity = level.getBlockEntity(blockPos);


        //Block Placer Decrease Tick Button
        if (blockEntity instanceof BlockPlacerBlockEntity blockPlacerBlockEntity) {
            int timer = blockPlacerBlockEntity.maxProgress;
            if (timer > 20) {
                blockPlacerBlockEntity.maxProgress = timer - 10;
            }
        }

        //Crafter Decrease Tick Button
        if (blockEntity instanceof CrafterBlockEntity crafterBlockEntity) {
            int timer = crafterBlockEntity.maxProgress;
            if (timer > 40) {
                crafterBlockEntity.maxProgress = timer - 10;
            }
        }

        //Redstone Clock Decrease Tick Button

        if (blockEntity instanceof RedstoneClockBlockEntity redstoneClockBlockEntity) {
            int increment = Screen.hasShiftDown() ? 50 : 10;
            int timer = redstoneClockBlockEntity.maxProgress;

            if (timer > 20) {
                redstoneClockBlockEntity.maxProgress = timer + increment;
                RedstoneClockBlockEntity entity = (RedstoneClockBlockEntity) level.getBlockEntity(blockPos);
                assert entity != null;
                entity.maxProgress = timer - increment;
                entity.progress = 0;
            }
        }

        //Ender Scrambler Decrease Range Button
        if (blockEntity instanceof EnderScramblerBlockEntity enderScramblerBlockEntity)  {
            int range = enderScramblerBlockEntity.SCRAMBLER_RANGE;
            if (range > 1) {
                enderScramblerBlockEntity.SCRAMBLER_RANGE = range - 1;
            }
        }
    }
}