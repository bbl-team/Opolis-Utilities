package com.benbenlaw.opolisutilities.capabillties;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

public interface ICapabilitySync<T> {
    void toNetwork(FriendlyByteBuf buf); // To handle data that gets sent to target
    LazyOptional<T> fromNetwork(FriendlyByteBuf buf); // To handle data from target

    void handle(NetworkEvent.Context context, LazyOptional<T> data);
    Capability<?> getCap();
}
