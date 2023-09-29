package com.benbenlaw.opolisutilities.capabillties.handlers;

import com.benbenlaw.opolisutilities.capabillties.IDataHandler;

public record SidedDataHandler(IDataHandler server, IDataHandler client) {
    public static SidedDataHandler create(IDataHandler server, IDataHandler client) {
        return new SidedDataHandler(server, client);
    }
}
