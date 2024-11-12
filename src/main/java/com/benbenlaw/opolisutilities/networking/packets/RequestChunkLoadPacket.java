package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.entity.custom.FluidGeneratorBlockEntity;
import com.benbenlaw.opolisutilities.networking.payload.ClearTankPayload;
import com.benbenlaw.opolisutilities.networking.payload.RequestChunkLoadPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestChunkLoadPacket() {

    public static final RequestChunkLoadPacket INSTANCE = new RequestChunkLoadPacket();

    public static RequestChunkLoadPacket get() {
        return INSTANCE;
    }

    public void handle(final RequestChunkLoadPayload payload, IPayloadContext context) {
        Level level = context.player().level();
        BlockPos pos = payload.blockPos();
        LevelChunk chunk = level.getChunkAt(pos);

        ServerLevel serverLevel = (ServerLevel) chunk.getLevel();

        assert serverLevel != null;
        serverLevel.startTickingChunk(chunk);



    }
}
