package com.benbenlaw.opolisutilities.capabillties;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface ITesting {
    int getValue();
    void setValue(int test);
}
