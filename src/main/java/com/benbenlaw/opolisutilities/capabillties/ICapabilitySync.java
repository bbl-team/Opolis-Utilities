package com.benbenlaw.opolisutilities.capabillties;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;

public interface ICapabilitySync {
    void toNetwork(CompoundTag data); // To handle data that gets sent to target
    void handle(CompoundTag data);
    Capability<?> getCap();
}
