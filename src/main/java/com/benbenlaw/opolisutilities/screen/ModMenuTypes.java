package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, OpolisUtilities.MOD_ID);


    public static final RegistryObject<MenuType<BlockBreakerMenu>> BLOCK_BREAKER_MENU =
            registerMenuType(BlockBreakerMenu::new, "block_breaker_menu");

    public static final RegistryObject<MenuType<BlockPlacerMenu>> BLOCK_PLACER_MENU =
            registerMenuType(BlockPlacerMenu::new, "block_placer_menu");

    public static final RegistryObject<MenuType<DryingTableMenu>> DRYING_TABLE_MENU =
            registerMenuType(DryingTableMenu::new, "drying_table_menu");

    public static final RegistryObject<MenuType<ItemRepairerMenu>> ITEM_REPAIRER_MENU =
            registerMenuType(ItemRepairerMenu::new, "item_repairer_menu");

    public static final RegistryObject<MenuType<ResourceGeneratorMenu>> RESOURCE_GENERATOR_MENU =
            registerMenuType(ResourceGeneratorMenu::new, "resource_generator_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }



//SHOP


    public static DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, OpolisUtilities.MOD_ID);

    public static final RegistryObject<MenuType<ShopMenu>> SHOP_MENU =
            CONTAINERS.register("shop_menu",
                    () -> IForgeMenuType.create(((windowId, inv, data) -> {
                        return new ShopMenu(windowId, inv);
                    })));


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
        CONTAINERS.register(eventBus);

    }


}
