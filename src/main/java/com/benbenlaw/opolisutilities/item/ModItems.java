package com.benbenlaw.opolisutilities.item;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.item.custom.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, OpolisUtilities.MOD_ID);

    public static final RegistryObject<Item> JEI_NULL_ITEM = ITEMS.register("jei_null_item",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MINI_COAL = ITEMS.register("mini_coal",
            () -> new MiniCoalItem(true));

    public static final RegistryObject<Item> MINI_CHARCOAL = ITEMS.register("mini_charcoal",
            () -> new MiniCoalItem(true));

    public static final RegistryObject<Item> WALLET = ITEMS.register("wallet",
            () -> new WalletItem(new Item.Properties()));

    public static final RegistryObject<Item> BASIC_LOOT_BOX = ITEMS.register("basic_loot_box",
            () -> new BasicLootBoxItem(new Item.Properties()));

    public static final RegistryObject<Item> ADVANCED_LOOT_BOX = ITEMS.register("advanced_loot_box",
            () -> new AdvancedLootBoxItem(new Item.Properties()));

    public static final RegistryObject<Item> ELITE_LOOT_BOX = ITEMS.register("elite_loot_box",
            () -> new EliteLootBoxItem(new Item.Properties()));

    public static final RegistryObject<Item> B_BUCKS = ITEMS.register("b_bucks",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LEAFY_STRING = ITEMS.register("leafy_string",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_SHEARS = ITEMS.register("wooden_shears",
            () -> new ShearsItem(new Item.Properties().durability(96)
                    ));

    public static final RegistryObject<Item> HOME_STONE = ITEMS.register("home_stone",
            () -> new HomeStoneItem(new Item.Properties().durability(32)
                    ));

    public static final RegistryObject<Item> ADVANCED_HOME_STONE = ITEMS.register("advanced_home_stone",
            () -> new AdvancedHomeStoneItem(new Item.Properties().durability(32)
                    ));

    public static final RegistryObject<Item> SUPER_HOME_STONE = ITEMS.register("super_home_stone",
            () -> new SuperHomeStoneItem(new Item.Properties().durability(32)
                    ));

    public static final RegistryObject<Item> DEATH_STONE = ITEMS.register("death_stone",
            () -> new DeathStoneItem(new Item.Properties().durability(32)
                    ));

    public static final RegistryObject<Item> FLOATING_BLOCK = ITEMS.register("floating_block_item",
            () -> new FloatingBlockItem(new Item.Properties()
                    ));

    public static final RegistryObject<Item> BUNDLED_FLESH = ITEMS.register("bundled_flesh",
            () -> new Item(new Item.Properties()
                    ));

    public static final RegistryObject<Item> ENDER_PEARL_FRAGMENT = ITEMS.register("ender_pearl_fragment",
            () -> new Item(new Item.Properties()
                    ));

    public static final RegistryObject<Item> JERKY = ITEMS.register("jerky",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(4).saturationMod(7.0F).build())
                    ));

    public static final RegistryObject<Item> SOAKED_PAPER = ITEMS.register("soaked_paper",
            () -> new Item(new Item.Properties()
                    ));

    public static final RegistryObject<Item> LOG_SHEET = ITEMS.register("log_sheet",
            () -> new Item(new Item.Properties()
                    ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }



}
