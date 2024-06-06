package com.benbenlaw.opolisutilities.sound;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;

public class ModSounds {


    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, OpolisUtilities.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> DOORBELL =
            createEvent("doorbell");

    private static DeferredHolder<SoundEvent, SoundEvent> createEvent(String sound) {
        return SOUND_EVENTS.register(sound, () ->
                SoundEvent.createVariableRangeEvent(Objects.requireNonNull(ResourceLocation.tryParse(OpolisUtilities.MOD_ID))));
    }
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
