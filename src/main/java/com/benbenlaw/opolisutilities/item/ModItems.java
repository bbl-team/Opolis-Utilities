package com.benbenlaw.opolisutilities.item;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.item.custom.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(OpolisUtilities.MOD_ID);

    public static final DeferredItem<Item> JEI_NULL_ITEM = ITEMS .register("jei_null_item",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MINI_COAL = ITEMS.register("mini_coal",
            () -> new MiniCoalItem(true));

    public static final DeferredItem<Item> MINI_CHARCOAL = ITEMS.register("mini_charcoal",
            () -> new MiniCoalItem(true));
    public static final DeferredItem<Item> BASIC_LOOT_BOX = ITEMS.register("basic_loot_box",
            () -> new BasicLootBoxItem(new Item.Properties()));

    public static final DeferredItem<Item> B_BUCKS = ITEMS.register("b_bucks",
            () -> new Item(new Item.Properties().stacksTo(99)));

    public static final DeferredItem<Item> LEAFY_STRING = ITEMS.register("leafy_string",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COPPER_NUGGET = ITEMS.register("copper_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WOODEN_SHEARS = ITEMS.register("wooden_shears",
            () -> new ShearsItem(new Item.Properties().durability(96)));

    public static final DeferredItem<Item> SUPER_HOME_STONE = ITEMS.register("super_home_stone",
            () -> new SuperHomeStoneItem(new Item.Properties().durability(32)));

    public static final DeferredItem<Item> DEATH_STONE = ITEMS.register("death_stone",
            () -> new DeathStoneItem(new Item.Properties().durability(32)));

    public static final DeferredItem<Item> FLOATING_BLOCK = ITEMS.register("floating_block_item",
            () -> new FloatingBlockItem(new Item.Properties()));

    public static final DeferredItem<Item> ENDER_PEARL_FRAGMENT = ITEMS.register("ender_pearl_fragment",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> JERKY = ITEMS.register("jerky",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(4).saturationModifier(7.0F).build())));

    public static final DeferredItem<Item> SOAKED_PAPER = ITEMS.register("soaked_paper",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LOG_SHEET = ITEMS.register("log_sheet",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CROOK = ITEMS.register("crook",
            () -> new CrookItem(new Item.Properties().durability(54)));

    public static final DeferredItem<Item> CATALOGUE_BOOK = ITEMS.register("catalogue_book",
            () -> new CatalogueBook(new Item.Properties()));
        public static final DeferredItem<Item> SAPLING_GROWER = ITEMS.register("sapling_grower",
            () -> new SaplingGrower(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> UPGRADE_BASE = ITEMS.register("upgrade_base",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ANIMAL_NET =  ITEMS.register("animal_net",
            () -> new AnimalNetItem(new Item.Properties().durability(8)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }



}
