package com.benbenlaw.opolisutilities.capabillties;

import net.minecraft.nbt.CompoundTag;

public interface IDataHandler {
    void handle(CompoundTag data, Object context);
}
