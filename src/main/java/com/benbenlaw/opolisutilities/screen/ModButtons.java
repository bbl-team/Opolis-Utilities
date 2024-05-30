package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModButtons {

    @OnlyIn(Dist.CLIENT)
    public static final WidgetSprites ON_BUTTONS = new WidgetSprites(
            new ResourceLocation(OpolisUtilities.MOD_ID, "machine/on"),
            new ResourceLocation(OpolisUtilities.MOD_ID, "machine/on_hover")
    );

    public static final WidgetSprites OFF_BUTTONS = new WidgetSprites(
            new ResourceLocation(OpolisUtilities.MOD_ID, "machine/off"),
            new ResourceLocation(OpolisUtilities.MOD_ID, "machine/off_hover")
    );



}


