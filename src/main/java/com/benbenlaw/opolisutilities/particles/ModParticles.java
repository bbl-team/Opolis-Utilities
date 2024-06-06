package com.benbenlaw.opolisutilities.particles;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, OpolisUtilities.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ENDER_ORE_PARTICLES =
            PARTICLE_TYPES.register("ender_ore_particles", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
