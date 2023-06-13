package com.benbenlaw.opolisutilities.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ITeleporter;

public class ModTeleport implements ITeleporter {
    protected final Level level;

    public ModTeleport(ServerLevel level) {
        this.level = level;
    }
}
