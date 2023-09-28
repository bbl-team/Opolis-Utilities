package com.benbenlaw.opolisutilities.capabillties.handlers;

import com.benbenlaw.opolisutilities.capabillties.IDataHandler;
import net.minecraft.nbt.CompoundTag;

public abstract class BlockDataHandler {
    public final static class Server implements IDataHandler {
        @Override
        public void handle(CompoundTag data, Object context) {

        }
    }

    public final static class Client implements IDataHandler {
        @Override
        public void handle(CompoundTag data, Object context) {

        }
    }
}
