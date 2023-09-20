package com.benbenlaw.opolisutilities.capabillties;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CapabilityAttacher {
    public static CapabilityAttacher create() {
        return new CapabilityAttacher();
    }


    public static class Attacher {
        public static Attacher create(Predicate<Object> predicate, ResourceLocation key, Supplier<ICapabilityProvider> capabilityProviderSupplier) {
            return new Attacher(predicate, key, capabilityProviderSupplier);
        }
        private final Predicate<Object> predicate;
        private final ResourceLocation key;
        private final Supplier<ICapabilityProvider> capabilityProviderSupplier;
        private Attacher(Predicate<Object> predicate, ResourceLocation key, Supplier<ICapabilityProvider> capabilityProviderSupplier) {
            this.predicate = predicate;
            this.key = key;
            this.capabilityProviderSupplier = capabilityProviderSupplier;
        }

        public boolean canAttach(Object object) {
            return predicate.test(object);
        }

        public ResourceLocation getKey() {
            return key;
        }

        public ICapabilityProvider getProvider() {
            return capabilityProviderSupplier.get();
        }
    }
    private final HashMap<Capability<?>, Attacher> CAPS = new HashMap<>();

    private CapabilityAttacher() {}
    public <T> void register(Capability<T> capability, Attacher attacher) {
        if (CAPS.containsKey(capability)) return;
        CAPS.put(capability, attacher);
    }

    @SuppressWarnings("all")
    public void capAttach(AttachCapabilitiesEvent<?> event) {
        var a = event;
        CAPS.forEach((cap, attach) -> {
            if (attach.canAttach(event.getObject()))
                event.addCapability(attach.getKey(), attach.getProvider());
        });
    }

    public void addAttachEvents(Class<?>... types) {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        for (Class<?> type : types) {
            @SuppressWarnings("unchecked")
            Class<Object> eventType = (Class<Object>) type;
            Consumer<AttachCapabilitiesEvent<Object>> event = this::capAttach;
            bus.addGenericListener(eventType, event);
        }
    }

    public void register(IEventBus bus) {
        addAttachEvents(Entity.class, BlockEntity.class, Item.class, Level.class, ItemStack.class);
    }
}
