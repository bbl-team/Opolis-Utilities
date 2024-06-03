package com.benbenlaw.opolisutilities;


import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.item.ModCreativeTab;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.networking.ModMessages;
import com.benbenlaw.opolisutilities.particles.ModParticles;
import com.benbenlaw.opolisutilities.recipe.CatalogueRecipe;
import com.benbenlaw.opolisutilities.recipe.ModRecipes;
import com.benbenlaw.opolisutilities.screen.*;
import com.benbenlaw.opolisutilities.util.ModAttachments;
import net.minecraft.client.gui.components.WidgetSprites;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Mod(OpolisUtilities.MOD_ID)
public class OpolisUtilities {
    public static final String MOD_ID = "opolisutilities";
    public static final Logger LOGGER = LogManager.getLogger();

    public OpolisUtilities(IEventBus modEventBus) {

      //  IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModAttachments.register(modEventBus);


        modEventBus.addListener(this::registerCapabilities);




        ModRecipes.register(modEventBus);
        ModParticles.register(modEventBus);
    //    ModEnchantments.register(modEventBus);
    //    ModSounds.register(modEventBus);
//
    //    Capabilities.register(MinecraftForge.EVENT_BUS);
//
        modEventBus.addListener(this::commonSetup);
//
    //    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigFile.SPEC, "opolis_utilities.toml");
//
    //    MinecraftForge.EVENT_BUS.register(this);

    }



    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent


    public void registerCapabilities(RegisterCapabilitiesEvent event) {

        ModBlockEntities.registerCapabilities(event);
    }

    public void commonSetup(RegisterPayloadHandlersEvent event) {
        ModMessages.registerNetworking(event);
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)

    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {

            event.register(ModMenuTypes.BLOCK_BREAKER_MENU.get(), BlockBreakerScreen::new);
            event.register(ModMenuTypes.BLOCK_PLACER_MENU.get(), BlockPlacerScreen::new);
            event.register(ModMenuTypes.DRYING_TABLE_MENU.get(), DryingTableScreen::new);
            event.register(ModMenuTypes.RESOURCE_GENERATOR_MENU.get(), ResourceGeneratorScreen::new);
            event.register(ModMenuTypes.ITEM_REPAIRER_MENU.get(), ItemRepairerScreen::new);
            event.register(ModMenuTypes.CATALOGUE_MENU.get(), CatalogueScreen::new);
            event.register(ModMenuTypes.CRAFTER_MENU.get(), CrafterScreen::new);
            event.register(ModMenuTypes.REDSTONE_CLOCK_MENU.get(), RedstoneClockScreen::new);

        }


        /*
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            event.enqueueWork(() -> {

//

                MenuScreens.create(ModMenuTypes.BLOCK_PLACER_MENU.get(),
                MenuScreens.create(ModMenuTypes.BLOCK_BREAKER_MENU.get(), BlockBreakerScreen::new);
                MenuScreens.create(ModMenuTypes.DRYING_TABLE_MENU.get(), DryingTableScreen::new);
                MenuScreens.create(ModMenuTypes.RESOURCE_GENERATOR_MENU.get(), ResourceGeneratorScreen::new);
                //    MenuScreens.register(ModMenuTypes.ITEM_REPAIRER_MENU.get(), ItemRepairerScreen::new);
                MenuScreens.create(ModMenuTypes.CATALOGUE_MENU.get(), CatalogueScreen::new);
                MenuScreens.create(ModMenuTypes.CRAFTER_MENU.get(), CrafterScreen::new);

            });
        }

         */
    }
}
