package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.entity.custom.FluidGeneratorBlockEntity;
import com.benbenlaw.opolisutilities.networking.payload.ClearTankPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClearTankPacket() {

    public static final ClearTankPacket INSTANCE = new ClearTankPacket();

    public static ClearTankPacket get() {
        return INSTANCE;
    }

    public void handle(final ClearTankPayload payload, IPayloadContext context) {

        Player player = context.player();
        Level level = player.level();
        boolean isShiftDown = payload.isShiftDown();
        BlockPos blockPos = payload.blockPos();
        BlockState blockState = level.getBlockState(blockPos);
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        int tankNumber = payload.tankID();

        if (blockEntity instanceof FluidGeneratorBlockEntity fluidGeneratorBlockEntity) {
            if (isShiftDown) {
                fluidGeneratorBlockEntity.FLUID_TANK.getFluid().setAmount(0);
                fluidGeneratorBlockEntity.sync();
            }
        }

    }
}
