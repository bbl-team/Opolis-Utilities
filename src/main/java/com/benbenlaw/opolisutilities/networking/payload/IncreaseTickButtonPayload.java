package com.benbenlaw.opolisutilities.networking.payload;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record IncreaseTickButtonPayload(BlockPos blockPos) implements CustomPacketPayload {

    public static final Type<IncreaseTickButtonPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "increase_tick"));

    @Override
    public Type<IncreaseTickButtonPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, IncreaseTickButtonPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, IncreaseTickButtonPayload::blockPos,
            IncreaseTickButtonPayload::new
    );

}
