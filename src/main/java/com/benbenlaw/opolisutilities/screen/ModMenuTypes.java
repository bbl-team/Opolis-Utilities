package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, OpolisUtilities.MOD_ID);



    public static final Registry<MenuType<BlockBreakerMenu>> BLOCK_BREAKER_MENU =
            registerMenuType(BlockBreakerMenu::new, "block_breaker_menu");

    public static final Registry<MenuType<BlockPlacerMenu>> BLOCK_PLACER_MENU =
            registerMenuType(BlockPlacerMenu::new, "block_placer_menu");

    public static final Registry<MenuType<DryingTableMenu>> DRYING_TABLE_MENU =
            registerMenuType(DryingTableMenu::new, "drying_table_menu");

    public static final Registry<MenuType<ItemRepairerMenu>> ITEM_REPAIRER_MENU =
            registerMenuType(ItemRepairerMenu::new, "item_repairer_menu");

    public static final Registry<MenuType<ResourceGeneratorMenu>> RESOURCE_GENERATOR_MENU =
            registerMenuType(ResourceGeneratorMenu::new, "resource_generator_menu");

    public static final Registry<MenuType<CrafterMenu>> CRAFTER_MENU =
            registerMenuType(CrafterMenu::new, "crafter_menu");


    private static <T extends AbstractContainerMenu> Registry<MenuType<T>> registerMenuType(Supplier<? extends T> menuSupplier, String name) {
        return MENUS.register(name, () -> MenuTypeBuilder.builder(menuSupplier));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);


    }
}
