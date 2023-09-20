package com.benbenlaw.opolisutilities.capabillties;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Capabilities {
    public static final Capability<ITesting> TESTING = CapabilityManager.get(new CapabilityToken<>() {});
    public static final CapabilityMigration MIGRATION = CapabilityMigration.create();
    public static final CapabilityAttacher ATTACHER = CapabilityAttacher.create();

    public static void test(Player player) {
        LazyOptional<ITesting> testingLazyOptional = player.getCapability(TESTING);
        testingLazyOptional.ifPresent(e -> {
            e.setValue(e.getValue() + 1);
            player.sendSystemMessage(Component.literal("Value: %s".formatted(e.getValue())));
        });
    }


    static {
        MIGRATION.register(
                Capabilities.TESTING,
                (old, newC) -> {
                    newC.setValue(old.getValue());
                }
        );

        ATTACHER.register(Capabilities.TESTING,
                CapabilityAttacher.Attacher.create(
                    o -> o instanceof Player,
                    new ResourceLocation("key"),
                        () -> {
                            class Provider implements ICapabilityProvider {
                                private final ITesting TESTING = new TestingCap();
                                private final LazyOptional<ITesting> TEST = LazyOptional.of(() -> TESTING);
                                @Override
                                public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                                    return getCapability(cap);
                                }

                                @Override
                                public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
                                    if (cap == Capabilities.TESTING)
                                        return TEST.cast();
                                    return LazyOptional.empty();
                                }
                            }
                            return new Provider();
                        }
                )
        );
    }

    public static void register(IEventBus bus) {
        MIGRATION.register(bus);
        ATTACHER.register(bus);
    }
}
