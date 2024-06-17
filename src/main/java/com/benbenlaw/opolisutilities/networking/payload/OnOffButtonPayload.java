package com.benbenlaw.opolisutilities.networking.payload;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OnOffButtonPayload(BlockPos blockPos) implements CustomPacketPayload {

    public static final Type<OnOffButtonPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "on_off_button"));

    @Override
    public Type<OnOffButtonPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, OnOffButtonPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OnOffButtonPayload::blockPos,
            OnOffButtonPayload::new
    );

}
