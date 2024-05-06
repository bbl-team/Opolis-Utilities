package com.benbenlaw.opolisutilities;


import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
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
    //    ModCreativeTab.register(modEventBus);
        ModBlockEntities.register(modEventBus);
    //    ModMenuTypes.register(modEventBus);
    //    ModRecipes.register(modEventBus);
    //    ModParticles.register(modEventBus);
    //    ModEnchantments.register(modEventBus);
    //    ModSounds.register(modEventBus);
//
    //    Capabilities.register(MinecraftForge.EVENT_BUS);
//
    //    modEventBus.addListener(this::commonSetup);
//
    //    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigFile.SPEC, "opolis_utilities.toml");
//
    //    MinecraftForge.EVENT_BUS.register(this);

    }

    /*
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            event.enqueueWork(() -> {

                MenuScreens.register(ModMenuTypes.BLOCK_PLACER_MENU.get(), BlockPlacerScreen::new);
                MenuScreens.register(ModMenuTypes.BLOCK_BREAKER_MENU.get(), BlockBreakerScreen::new);
                MenuScreens.register(ModMenuTypes.DRYING_TABLE_MENU.get(), DryingTableScreen::new);
                MenuScreens.register(ModMenuTypes.RESOURCE_GENERATOR_MENU.get(), ResourceGeneratorScreen::new);
                MenuScreens.register(ModMenuTypes.ITEM_REPAIRER_MENU.get(), ItemRepairerScreen::new);
                MenuScreens.register(ModMenuTypes.CATALOGUE_MENU.get(), CatalogueScreen::new);
                MenuScreens.register(ModMenuTypes.CRAFTER_MENU.get(), CrafterScreen::new);

            });
        }
    }

     */
}
