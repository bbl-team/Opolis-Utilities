package com.benbenlaw.opolisutilities.networking.payload;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record RequestChunkLoadPayload(BlockPos blockPos) implements CustomPacketPayload {

    public static final Type<RequestChunkLoadPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "request_chunk_load"));

    @Override
    public Type<RequestChunkLoadPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, RequestChunkLoadPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RequestChunkLoadPayload::blockPos,
            RequestChunkLoadPayload::new
    );
}

