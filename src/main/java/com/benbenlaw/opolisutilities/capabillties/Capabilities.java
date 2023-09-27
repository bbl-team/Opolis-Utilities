package com.benbenlaw.opolisutilities.capabillties;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
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
            if (!player.level().isClientSide)
                e.setValue(e.getValue() + 1);

            player.sendSystemMessage(Component.literal("Value: %s SERVER: %s".formatted(e.getValue(), player instanceof ServerPlayer)));
        });
    }

    public static class Data {
        private final int value;
        public Data(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    static {
        MIGRATION.register(
                Capabilities.TESTING,
                (old, newC) -> {
                    newC.setValue(old.getValue());
                }
        );
    }

    public static void register(IEventBus bus) {
        MIGRATION.register(bus);
        ATTACHER.register(bus);
    }
}
