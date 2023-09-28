package com.benbenlaw.opolisutilities.capabillties;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

public interface ICapabilitySync {
    void toNetwork(CompoundTag data); // To handle data that gets sent to target
    void handle(CompoundTag data);
    Capability<?> getCap();
}
