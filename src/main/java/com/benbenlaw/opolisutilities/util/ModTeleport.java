package com.benbenlaw.opolisutilities.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.ITeleporter;

public class ModTeleport implements ITeleporter {
    protected final ServerLevel level;

    public ModTeleport(ServerLevel level) {
        this.level = level;
    }
}
