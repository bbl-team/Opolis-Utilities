package com.benbenlaw.opolisutilities;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.DryingTableBlockEntity;
import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.networking.ModMessages;
import com.benbenlaw.opolisutilities.particles.ModParticles;
import com.benbenlaw.opolisutilities.recipe.ModRecipes;
import com.benbenlaw.opolisutilities.screen.*;
import com.benbenlaw.opolisutilities.world.feature.ModConfiguredFeatures;
import com.benbenlaw.opolisutilities.world.feature.ModPlacedFeatures;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(OpolisUtilities.MOD_ID)
public class OpolisUtilities {
    public static final String MOD_ID = "opolisutilities";
    private static final Logger LOGGER = LogUtils.getLogger();

    public OpolisUtilities() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModParticles.register(modEventBus);

        ModConfiguredFeatures.register(modEventBus);
        ModPlacedFeatures.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigFile.SPEC, "opolis_utilities.toml");

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
        });

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            event.enqueueWork(() -> {

                MenuScreens.register(ModMenuTypes.DRYING_TABLE_MENU.get(), DryingTableScreen::new);
                MenuScreens.register(ModMenuTypes.RESOURCE_GENERATOR_MENU.get(), ResourceGeneratorScreen::new);
                //BlockEntityRenderers.register(ModBlockEntities.DRYING_TABLE_BLOCK_ENTITY.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.DRYING_TABLE.get(), RenderType.cutout());

            });
        }
    }


}
