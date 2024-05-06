package com.benbenlaw.opolisutilities.capabillties;

import java.util.HashMap;

public class CapabilityMigration {

    /*
    public static CapabilityMigration create() {
        return new CapabilityMigration();
    }


    public interface IMigrator<T> {
        void migrate(T oldCap, T newCap);
        @SuppressWarnings("all")
        default void migrateCast(Object oldObj, Object newObj) {
            migrate((T) oldObj, (T) newObj);
        }
    }

    private final HashMap<Capability<?>, IMigrator<?>> CAPS = new HashMap<>();

    private CapabilityMigration() {}
    public <T> void register(Capability<T> capability, IMigrator<T> updater) {
        if (CAPS.containsKey(capability)) return;
        CAPS.put(capability, updater);
    }

    @SuppressWarnings("all")
    private void handle(CapabilityProvider oldProvider, CapabilityProvider newProvider) {
        oldProvider.reviveCaps();
        CAPS.forEach((cap, migrator) -> {
            var oldCap = oldProvider.getCapability(cap);
            var newCap = newProvider.getCapability(cap);
            if (oldCap.resolve().isPresent() && newCap.resolve().isPresent())
                migrator.migrateCast(oldCap.resolve().get(), newCap.resolve().get());
        });
        oldProvider.invalidateCaps();
    }

    public void onRespawn(PlayerEvent.Clone playerClone) {
        if (playerClone.isWasDeath())
            handle(playerClone.getOriginal(), playerClone.getEntity());
    }

    public void register(IEventBus bus) {
        bus.addListener(this::onRespawn);
    }

     */
}