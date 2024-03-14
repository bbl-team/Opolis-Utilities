package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.particles.ModParticles;
import com.benbenlaw.opolisutilities.particles.custom.EnderOreParticles;
import com.benbenlaw.opolisutilities.screen.CrafterMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.ENDER_ORE_PARTICLES.get(),
                EnderOreParticles.Provider::new);
    }
}
