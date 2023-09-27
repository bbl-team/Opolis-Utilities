package com.benbenlaw.opolisutilities.capabillties;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.HashMap;

public class CapabillitySyncronizer {
    private static final HashMap<Capability<?>, ResourceLocation> CAPS = new HashMap<>();
    private static final HashMap<ResourceLocation, Capability<?>> REVERSE_CAPS = new HashMap<>();

    public static void register(ResourceLocation resourceLocation, Capability<?> capability) {
        CAPS.put(capability, resourceLocation);
        REVERSE_CAPS.put(resourceLocation, capability);
    }

    static {
        register(new ResourceLocation("forge", "item"), ForgeCapabilities.ITEM_HANDLER);
        register(new ResourceLocation("forge", "fluid"), ForgeCapabilities.FLUID_HANDLER);
        register(new ResourceLocation("forge", "energy"), ForgeCapabilities.ENERGY);
        register(new ResourceLocation("forge", "fluid_item"), ForgeCapabilities.FLUID_HANDLER_ITEM);
    }

    public static ResourceLocation get(Capability<?> capability) {
        return CAPS.getOrDefault(capability, new ResourceLocation("empty"));
    }

    public static Capability<?> get(ResourceLocation location) {
        return REVERSE_CAPS.getOrDefault(location, null);
    }
}
