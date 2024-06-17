package com.benbenlaw.opolisutilities.networking.payload;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SaveRecipePayload(BlockPos blockPos) implements CustomPacketPayload {

    public static final Type<SaveRecipePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "save_recipe"));

    @Override
    public Type<SaveRecipePayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SaveRecipePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SaveRecipePayload::blockPos,
            SaveRecipePayload::new
    );

}
