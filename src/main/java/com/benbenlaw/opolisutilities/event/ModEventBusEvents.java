package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.particles.ModParticles;
import com.benbenlaw.opolisutilities.particles.custom.EnderOreParticles;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.ResourceGeneratorRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {






    //@SubscribeEvent
    public static void registerRecipeTypes(final RegistryAccess.RegistryEntry<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, DryingTableRecipe.Type.ID, DryingTableRecipe.Type.INSTANCE);

        Registry.register(Registry.RECIPE_TYPE, ResourceGeneratorRecipe.Type.ID, ResourceGeneratorRecipe.Type.INSTANCE);

    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.ENDER_ORE_PARTICLES.get(),
                EnderOreParticles.Provider::new);
    }



}
