package com.benbenlaw.opolisutilities.capabillties;

import net.minecraftforge.network.NetworkEvent;

public interface ICapabilitySyncHandler {
    void handle(NetworkEvent.Context context);
}
