package com.benbenlaw.opolisutilities.capabillties;

import net.neoforged.bus.api.IEventBus;

public class Capabilities {
    public static final CapabilityMigration MIGRATION = CapabilityMigration.create();
    public static final CapabilityAttacher ATTACHER = CapabilityAttacher.create();


    public static void register(IEventBus bus) {
        MIGRATION.register(bus);
        ATTACHER.register(bus);
    }
}
