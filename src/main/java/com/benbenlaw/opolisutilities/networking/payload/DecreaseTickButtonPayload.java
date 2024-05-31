package com.benbenlaw.opolisutilities.networking.payload;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DecreaseTickButtonPayload(BlockPos blockPos) implements CustomPacketPayload {

    public static final Type<DecreaseTickButtonPayload> TYPE = new Type<>(new ResourceLocation(OpolisUtilities.MOD_ID, "decrease_tick"));

    @Override
    public Type<DecreaseTickButtonPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, DecreaseTickButtonPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, DecreaseTickButtonPayload::blockPos,
            DecreaseTickButtonPayload::new
    );

}
