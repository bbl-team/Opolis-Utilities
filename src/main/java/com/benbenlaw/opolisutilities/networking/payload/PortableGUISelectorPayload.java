package com.benbenlaw.opolisutilities.networking.payload;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record PortableGUISelectorPayload(int itemSlot, boolean increase, int location) implements CustomPacketPayload {

    public static final Type<PortableGUISelectorPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "portable_gui_selector"));

    @Override
    public Type<PortableGUISelectorPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, PortableGUISelectorPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PortableGUISelectorPayload::itemSlot,
            ByteBufCodecs.BOOL, PortableGUISelectorPayload::increase,
            ByteBufCodecs.INT, PortableGUISelectorPayload::location,
            PortableGUISelectorPayload::new
    );

}
