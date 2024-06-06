package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.screen.custom.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, OpolisUtilities.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<BlockBreakerMenu>> BLOCK_BREAKER_MENU;
    public static final DeferredHolder<MenuType<?>, MenuType<BlockPlacerMenu>> BLOCK_PLACER_MENU;
    public static final DeferredHolder<MenuType<?>, MenuType<DryingTableMenu>> DRYING_TABLE_MENU;
    public static final DeferredHolder<MenuType<?>, MenuType<ItemRepairerMenu>> ITEM_REPAIRER_MENU;
    public static final DeferredHolder<MenuType<?>, MenuType<ResourceGeneratorMenu>> RESOURCE_GENERATOR_MENU;
    public static final DeferredHolder<MenuType<?>, MenuType<CrafterMenu>> CRAFTER_MENU;
    public static final DeferredHolder<MenuType<?>, MenuType<CatalogueMenu>> CATALOGUE_MENU;
    public static final DeferredHolder<MenuType<?>, MenuType<RedstoneClockMenu>> REDSTONE_CLOCK_MENU;
    public static final DeferredHolder<MenuType<?>, MenuType<EnderScramblerMenu>> ENDER_SCRAMBLER_MENU;
    public static final DeferredHolder<MenuType<?>, MenuType<FluidGeneratorMenu>> FLUID_GENERATOR_MENU;


    static {
        BLOCK_BREAKER_MENU = MENUS.register("block_breaker_menu", () ->
                IMenuTypeExtension.create(BlockBreakerMenu::new));

        BLOCK_PLACER_MENU = MENUS.register("block_placer_menu", () ->
                IMenuTypeExtension.create(BlockPlacerMenu::new));

        DRYING_TABLE_MENU = MENUS.register("drying_table_menu", () ->
                IMenuTypeExtension.create(DryingTableMenu::new));

        ITEM_REPAIRER_MENU = MENUS.register("item_repairer_menu", () ->
                IMenuTypeExtension.create(ItemRepairerMenu::new));

        RESOURCE_GENERATOR_MENU = MENUS.register("resource_generator_menu", () ->
                IMenuTypeExtension.create(ResourceGeneratorMenu::new));

        CRAFTER_MENU = MENUS.register("crafter_menu", () ->
                IMenuTypeExtension.create(CrafterMenu::new));

        CATALOGUE_MENU = MENUS.register("catalogue_menu", () ->
                IMenuTypeExtension.create(CatalogueMenu::new));

        REDSTONE_CLOCK_MENU = MENUS.register("redstone_clock_menu", () ->
                IMenuTypeExtension.create(RedstoneClockMenu::new));

        ENDER_SCRAMBLER_MENU = MENUS.register("ender_scrambler_menu", () ->
                IMenuTypeExtension.create(EnderScramblerMenu::new));

        FLUID_GENERATOR_MENU = MENUS.register("fluid_generator_menu", () ->
                IMenuTypeExtension.create(FluidGeneratorMenu::new));


    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);


    }
}
