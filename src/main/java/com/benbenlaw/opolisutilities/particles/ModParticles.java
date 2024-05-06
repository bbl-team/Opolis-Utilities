package com.benbenlaw.opolisutilities.particles;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, OpolisUtilities.MOD_ID);

    /*
    public static final Registry<SimpleParticleType> ENDER_ORE_PARTICLES =
            Registry.register(BuiltInRegistries.PARTICLE_TYPE,
                    OpolisUtilities.MOD_ID + ":ender_ore_particles", new SimpleParticleType(true));


     */


    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
