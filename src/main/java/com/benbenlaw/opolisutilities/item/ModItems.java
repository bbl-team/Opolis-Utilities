package com.benbenlaw.opolisutilities.item;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.item.custom.HomeStoneItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, OpolisUtilities.MOD_ID);

    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget",
            () -> new Item(new Item.Properties().tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> HOME_STONE = ITEMS.register("home_stone",
            () -> new HomeStoneItem(new Item.Properties().stacksTo(1).durability(16).tab(ModCreativeModTab.OPOLIS_UTILITIES)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }



}
