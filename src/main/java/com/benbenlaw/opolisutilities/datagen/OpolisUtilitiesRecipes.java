package com.benbenlaw.opolisutilities.datagen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.datagen.recipes.*;
import com.benbenlaw.opolisutilities.item.ModDataComponents;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.recipe.ClocheRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class OpolisUtilitiesRecipes extends RecipeProvider {

    public OpolisUtilitiesRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {

        // Portable GUI
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PORTABLE_GUI)
                .pattern("E E")
                .pattern("SCS")
                .pattern("SCS")
                .define('S', Tags.Items.STONES)
                .define('E', Items.ENDER_PEARL)
                .define('C', Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.LEATHER))
                .save(consumer);

        //Animal Net
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ANIMAL_NET.get())
                .pattern("SLS")
                .pattern("L L")
                .pattern("SLS")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('L', Items.LEATHER)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Tags.Items.RODS_WOODEN))
                .save(consumer);

        //Summoning Block
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SUMMONING_BLOCK.get())
                .pattern("SSS")
                .pattern("AMA")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('M', Items.HAY_BLOCK)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.DISPENSER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block"));


        //Block Breaker
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLOCK_BREAKER.get())
                .pattern("SSS")
                .pattern("AMA")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('M', Items.DISPENSER)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.DISPENSER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "block_breaker"));

        //Block Placer
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLOCK_PLACER.get())
                .pattern("SSS")
                .pattern("A A")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('A', Tags.Items.INGOTS_IRON)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        //Catalogue
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CATALOGUE.get())
                .pattern("SSS")
                .pattern("SBS")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('B', ModItems.B_BUCKS)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ModItems.B_BUCKS))
                .save(consumer);

        //Catalogue
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CLOCHE.get())
                .pattern("SSS")
                .pattern("WRW")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('W', Tags.Items.BUCKETS_WATER)
                .define('R',  ModBlocks.RESOURCE_GENERATOR.get())
                .group("opolisutilities")
                .unlockedBy("has_item", has(ModItems.B_BUCKS))
                .save(consumer);

        //Catalogue Book
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CATALOGUE_BOOK.get())
                .requires(ModBlocks.CATALOGUE.get())
                .requires(Items.BOOK)
                .unlockedBy("has_item", has(ModBlocks.CATALOGUE.get()))
                .save(consumer);

        //Mini Charcoal
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MINI_CHARCOAL.get(), 8)
                .requires(Items.CHARCOAL)
                .unlockedBy("has_item", has(Items.CHARCOAL))
                .save(consumer);

        //Mini Coal
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MINI_COAL.get(), 8)
                .requires(Items.COAL)
                .unlockedBy("has_item", has(Items.COAL))
                .save(consumer);

        //Charcoal
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CHARCOAL)
                .requires(ModItems.MINI_CHARCOAL, 8)
                .unlockedBy("has_item", has(ModItems.MINI_CHARCOAL))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "charcoal"));

        //Coal
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COAL)
                .requires(ModItems.MINI_COAL, 8)
                .unlockedBy("has_item", has(ModItems.MINI_COAL))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "coal"));

        //Chests
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHEST, 4)
                .pattern("LLL")
                .pattern("L L")
                .pattern("LLL")
                .define('L', ItemTags.LOGS)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ItemTags.LOGS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "chests"));

        //Copper Nugget
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COPPER_NUGGET.get(), 9)
                .requires(Items.COPPER_INGOT)
                .unlockedBy("has_item", has(Items.COPPER_INGOT))
                .save(consumer);

        //Copper Ingot
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COPPER_INGOT)
                .pattern("CCC")
                .pattern("CCC")
                .pattern("CCC")
                .define('C', ModItems.COPPER_NUGGET)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ModItems.COPPER_NUGGET))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "copper_ingot"));

        //Crafter
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CRAFTER.get())
                .pattern("SSS")
                .pattern("ACA")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('C', Items.CRAFTER)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.CRAFTING_TABLE))
                .save(consumer);

        //Crook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CROOK.get())
                .pattern(" SS")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Tags.Items.RODS_WOODEN)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Tags.Items.RODS_WOODEN))
                .save(consumer);

        //Diamond Horse Armour
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.DIAMOND_HORSE_ARMOR)
                .pattern("DDD")
                .pattern("DSD")
                .define('D', Items.DIAMOND)
                .define('S', Items.SADDLE)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.DIAMOND))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "diamond_horse_armour"));

        //Drying Table
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DRYING_TABLE.get())
                .pattern("LSL")
                .pattern("LSL")
                .pattern("L L")
                .define('L', ItemTags.LOGS)
                .define('S', Tags.Items.STRINGS)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Tags.Items.STRINGS))
                .save(consumer);

        //Ender Pearl
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ENDER_PEARL)
                .requires(ModItems.ENDER_PEARL_FRAGMENT, 8)
                .unlockedBy("has_item", has(ModItems.ENDER_PEARL_FRAGMENT))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "ender_pearl"));

        //Ender Pearl Fragment
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ENDER_PEARL_FRAGMENT, 8)
                .requires(Items.ENDER_PEARL)
                .unlockedBy("has_item", has(Items.ENDER_PEARL))
                .save(consumer);

        //Ender Scrambler
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENDER_SCRAMBLER.get())
                .pattern("SSS")
                .pattern("EAE")
                .pattern("SSS")
                .define('E', Items.ENDER_PEARL)
                .define('S', Tags.Items.STONES)
                .define('A', Tags.Items.INGOTS_IRON)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.ENDER_PEARL))
                .save(consumer);

        //Floating Block
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLOATING_BLOCK.get())
                .pattern(" F ")
                .pattern("FWF")
                .pattern(" F ")
                .define('F', Tags.Items.FEATHERS)
                .define('W', ItemTags.WOOL)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Tags.Items.FEATHERS))
                .save(consumer);

        //Fluid Generator
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FLUID_GENERATOR.get())
                .pattern("SSS")
                .pattern("ABA")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('B', Items.BUCKET)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.BUCKET))
                .save(consumer);

        //Golden Horse Armour
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.GOLDEN_HORSE_ARMOR)
                .pattern("GGG")
                .pattern("GSG")
                .define('G', Items.GOLD_INGOT)
                .define('S', Items.SADDLE)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.GOLD_INGOT))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "golden_horse_armour"));

        //Green Wool
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.GREEN_WOOL)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModItems.LEAFY_STRING)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ModItems.LEAFY_STRING))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "green_wool"));

        //Guide Book

        //TODO//

        //Home Stone
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HOME_STONE.get())
                .pattern("ECE")
                .pattern("CDC")
                .pattern("ECE")
                .define('E', ModItems.ENDER_PEARL_FRAGMENT)
                .define('C', Items.COMPASS)
                .define('D', ItemTags.BEDS)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ModItems.ENDER_PEARL_FRAGMENT))
                .save(consumer);

        //Iron Horse Armour
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.IRON_HORSE_ARMOR)
                .pattern("III")
                .pattern("ISI")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.SADDLE)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "iron_horse_armour"));

        //Item Repairer
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ITEM_REPAIRER.get())
                .pattern("SSS")
                .pattern("BBB")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('B', Items.ANVIL)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.ANVIL))
                .save(consumer);

        //Leafy String
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LEAFY_STRING.get())
                .pattern("LLL")
                .define('L', ItemTags.LEAVES)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ItemTags.LEAVES))
                .save(consumer);

        //Leather Horse Armour
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER_HORSE_ARMOR)
                .pattern("LLL")
                .pattern("LSL")
                .define('L', Items.LEATHER)
                .define('S', Items.SADDLE)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.LEATHER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "leather_horse_armour"));

        //Log Sheets
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LOG_SHEET.get(), 6)
                .pattern(" L ")
                .pattern("L L")
                .pattern(" L ")
                .define('L', ItemTags.LOGS)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ItemTags.LOGS))
                .save(consumer);

        //Name Tag
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.NAME_TAG)
                .pattern("  S")
                .pattern(" P ")
                .pattern("P  ")
                .define('P', Items.PAPER)
                .define('S', Tags.Items.STRINGS)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.PAPER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "name_tag"));

        //Redstone Clock
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REDSTONE_CLOCK.get())
                .pattern("SSS")
                .pattern("ARA")
                .pattern("SSS")
                .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.STONES)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.REDSTONE))
                .save(consumer);

        //Saddle
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SADDLE)
                .pattern("LLL")
                .pattern("S S")
                .define('L', Items.LEATHER)
                .define('S', Tags.Items.STRINGS)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.LEATHER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "saddle"));

        //Sapling Grower
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SAPLING_GROWER.get())
                .pattern("  B")
                .pattern(" S ")
                .pattern("S  ")
                .define('B', Items.BONE_BLOCK)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.BONE_BLOCK))
                .save(consumer);

        //Sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.STICK, 16)
                .pattern("W")
                .pattern("W")
                .define('W', ItemTags.LOGS)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ItemTags.LOGS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "sticks"));

        //Wooden Shears
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WOODEN_SHEARS.get())
                .pattern(" W")
                .pattern("W ")
                .define('W', ItemTags.PLANKS)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ItemTags.PLANKS))
                .save(consumer);

        //Resource Generator
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RESOURCE_GENERATOR.get())
                .pattern("SSS")
                .pattern("W L")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('W', Tags.Items.BUCKETS_WATER)
                .define('L', Tags.Items.BUCKETS_LAVA)
                .group("opolisutilities")
                .unlockedBy("has_item", has(Items.BUCKET))
                .save(consumer);


        // ********** Drying Tables Recipes ********** //

        //Cracked Stone Bricks
        DryingTableRecipeBuilder.dryingTableRecipe(new SizedIngredient(Ingredient.of(Items.STONE_BRICKS), 1),
                        new ItemStack(Items.CRACKED_STONE_BRICKS), 200)
                .unlockedBy("has_item", has(Tags.Items.STONES))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "drying_table/cracked_stone_bricks"));

        //Dead Bush
        DryingTableRecipeBuilder.dryingTableRecipe(new SizedIngredient(Ingredient.of(ItemTags.SAPLINGS), 1),
                        new ItemStack(Items.DEAD_BUSH), 100)
                .unlockedBy("has_item", has(Items.DEAD_BUSH))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "drying_table/dead_bush"));

        //Jerky
        DryingTableRecipeBuilder.dryingTableRecipe(new SizedIngredient(Ingredient.of(Tags.Items.FOODS_RAW_MEAT), 1),
                        new ItemStack(ModItems.JERKY.get()), 200)
                .unlockedBy("has_item", has(Tags.Items.FOODS_RAW_MEAT))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "drying_table/jerky"));

        //Paper
        DryingTableRecipeBuilder.dryingTableRecipe(new SizedIngredient(Ingredient.of(ModItems.SOAKED_PAPER), 1),
                        new ItemStack(Items.PAPER), 200)
                .unlockedBy("has_item", has(ModItems.SOAKED_PAPER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "drying_table/paper"));

        //Sponge
        DryingTableRecipeBuilder.dryingTableRecipe(new SizedIngredient(Ingredient.of(Items.WET_SPONGE), 1),
                        new ItemStack(Items.SPONGE), 200)
                .unlockedBy("has_item", has(Items.WET_SPONGE))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "drying_table/sponge"));

        // ********** Catalogue ********** //

        //Catalogue Book
        CatalogueRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(ModItems.B_BUCKS), 2),
                        new ItemStack(ModItems.CATALOGUE_BOOK.get()))
                .unlockedBy("has_item", has(ModItems.B_BUCKS))
                .save(consumer);

        //Loot Bag
        CatalogueRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(ModItems.B_BUCKS), 4),
                        new ItemStack(ModItems.BASIC_LOOT_BOX.get()))
                .unlockedBy("has_item", has(ModItems.B_BUCKS))
                .save(consumer);

        //Resource Generator

        //Stone
        ResourceGeneratorRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Tags.Items.STONES))
                .unlockedBy("has_item", has(Tags.Items.STONES))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "resource_generator/stones"));

        //Cobblestone
        ResourceGeneratorRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Tags.Items.COBBLESTONES))
                .unlockedBy("has_item", has(Tags.Items.COBBLESTONES))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "resource_generator/cobblestone"));

        // ********** Soaking Table ********** //

        //Mud
        SoakingTableRecipeBuilder.soakingTableRecipe(new SizedIngredient(Ingredient.of(ItemTags.DIRT), 1),
                        new ItemStack(Items.MUD), 200)
                .unlockedBy("has_item", has(ItemTags.DIRT))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "soaking_table/mud"));

        //Soaked Paper
        SoakingTableRecipeBuilder.soakingTableRecipe(new SizedIngredient(Ingredient.of(ModItems.LOG_SHEET), 1),
                        new ItemStack(ModItems.SOAKED_PAPER.get()), 200)
                .unlockedBy("has_item", has(ModItems.LOG_SHEET))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "soaking_table/soaked_paper"));

        //Water Bottle
        Holder<Potion> water = (Potions.WATER);
        ItemStack itemStack = new ItemStack(Items.POTION);
        itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(water));

        SoakingTableRecipeBuilder.soakingTableRecipe(new SizedIngredient(Ingredient.of(Items.GLASS_BOTTLE), 1),
                        itemStack, 200)
                .unlockedBy("has_item", has(Items.GLASS_BOTTLE))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "soaking_table/water_bottle"));

        //Wet Sponge
        SoakingTableRecipeBuilder.soakingTableRecipe(new SizedIngredient(Ingredient.of(Items.SPONGE), 1),
                        new ItemStack(Items.WET_SPONGE), 200)
                .unlockedBy("has_item", has(Items.SPONGE))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "soaking_table/wet_sponge"));

        // ********** Speed Upgrades (Vanilla) ********** //

        //Coal
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(Tags.Items.STORAGE_BLOCKS_COAL), 200)
                .unlockedBy("has_item", has(Tags.Items.STORAGE_BLOCKS_COAL))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/coal"));

        //Amethyst
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(Items.AMETHYST_BLOCK), 120)
                .unlockedBy("has_item", has(Items.AMETHYST_BLOCK))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/amethyst"));

        //Beacon
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(Items.BEACON), 20)
                .unlockedBy("has_item", has(Items.BEACON))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/beacon"));

        //Glowstone
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(Items.GLOWSTONE), 120)
                .unlockedBy("has_item", has(Items.GLOWSTONE))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/glowstone"));

        //Fences
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(Tags.Items.FENCES_WOODEN), 210)
                .unlockedBy("has_item", has(Tags.Items.FENCES_WOODEN))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/wooden_fences"));

        //Stone
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(Tags.Items.STONES), 200)
                .unlockedBy("has_item", has(Tags.Items.STONES))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/stone"));

        //Obsidian
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(Items.OBSIDIAN), 100)
                .unlockedBy("has_item", has(Items.OBSIDIAN))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/obsidian"));

        //Nether Star
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(Items.NETHER_STAR), 30)
                .unlockedBy("has_item", has(Items.NETHER_STAR))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/nether_star"));

        // ********** Speed Upgrades (Tags) ********** //

        //Aluminum

        TagKey<Item> aluminumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/aluminum")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(aluminumBlockTag), 100)
                .unlockedBy("has_item", has(aluminumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(aluminumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/aluminum"));

        //Dark Steel
        TagKey<Item> darkSteelBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/dark_steel")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(darkSteelBlockTag), 60)
                .unlockedBy("has_item", has(darkSteelBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(darkSteelBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/dark_steel"));

        //Cyanite
        TagKey<Item> cyaniteBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/cyanite")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(cyaniteBlockTag), 50)
                .unlockedBy("has_item", has(cyaniteBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(cyaniteBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/cyanite"));

        //Copper
        TagKey<Item> copperBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/copper")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(copperBlockTag), 140)
                .unlockedBy("has_item", has(copperBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(copperBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/copper"));

        //Constantan
        TagKey<Item> constantanBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/constantan")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(constantanBlockTag), 90)
                .unlockedBy("has_item", has(constantanBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(constantanBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/constantan"));

        //Conductive Iron
        TagKey<Item> conductiveIronBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/conductive_iron")))));

        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(conductiveIronBlockTag), 60)
                .unlockedBy("has_item", has(conductiveIronBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(conductiveIronBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/conductive_iron"));

        //Compressed Iron
        TagKey<Item> compressedIronBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/compressed_iron")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(compressedIronBlockTag), 80)
                .unlockedBy("has_item", has(compressedIronBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(compressedIronBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/compressed_iron"));

        //Calorite
        TagKey<Item> caloriteBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/calorite")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(caloriteBlockTag), 60)
                .unlockedBy("has_item", has(caloriteBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(caloriteBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/calorite"));

        //Bronze
        TagKey<Item> bronzeBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/bronze")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(bronzeBlockTag), 120)
                .unlockedBy("has_item", has(bronzeBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(bronzeBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/bronze"));

        //Brass
        TagKey<Item> brassBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/brass")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(brassBlockTag), 100)
                .unlockedBy("has_item", has(brassBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(brassBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/brass"));

        //Blutonium
        TagKey<Item> blutoniumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/blutonium")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(blutoniumBlockTag), 50)
                .unlockedBy("has_item", has(blutoniumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(blutoniumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/blutonium"));

        //Lumium
        TagKey<Item> lumiumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/lumium")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(lumiumBlockTag), 40)
                .unlockedBy("has_item", has(lumiumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(lumiumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/lumium"));

        //Lead
        TagKey<Item> leadBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/lead")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(leadBlockTag), 150)
                .unlockedBy("has_item", has(leadBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(leadBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/lead"));

        //Lapis
        TagKey<Item> lapisBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/lapis")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(lapisBlockTag), 160)
                .unlockedBy("has_item", has(lapisBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(lapisBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/lapis"));

        //Iron
        TagKey<Item> ironBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/iron")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(ironBlockTag), 190)
                .unlockedBy("has_item", has(ironBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(ironBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/iron"));

        //Invar
        TagKey<Item> invarBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/invar")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(invarBlockTag), 130)
                .unlockedBy("has_item", has(invarBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(invarBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/invar"));

        //Graphite
        TagKey<Item> graphiteBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/graphite")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(graphiteBlockTag), 150)
                .unlockedBy("has_item", has(graphiteBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(graphiteBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/graphite"));

        //Gold
        TagKey<Item> goldBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/gold")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(goldBlockTag), 160)
                .unlockedBy("has_item", has(goldBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(goldBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/gold"));

        //Energetic Alloy
        TagKey<Item> energeticAlloyBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/energetic_alloy")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(energeticAlloyBlockTag), 70)
                .unlockedBy("has_item", has(energeticAlloyBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(energeticAlloyBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/energetic_alloy"));

        //End Steel
        TagKey<Item> endSteelBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/end_steel")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(endSteelBlockTag), 50)
                .unlockedBy("has_item", has(endSteelBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(endSteelBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/end_steel"));

        //Enderium
        TagKey<Item> enderiumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/enderium")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(enderiumBlockTag), 40)
                .unlockedBy("has_item", has(enderiumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(enderiumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/enderium"));

        //Emerald
        TagKey<Item> emeraldBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/emerald")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(emeraldBlockTag), 80)
                .unlockedBy("has_item", has(emeraldBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(emeraldBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/emerald"));

        //Electrum
        TagKey<Item> electrumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/electrum")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(electrumBlockTag), 115)
                .unlockedBy("has_item", has(electrumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(electrumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/electrum"));

        //Diamond
        TagKey<Item> diamondBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/diamond")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(diamondBlockTag), 100)
                .unlockedBy("has_item", has(diamondBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(diamondBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/diamond"));
        //Desh
        TagKey<Item> deshBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/desh")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(deshBlockTag), 60)
                .unlockedBy("has_item", has(deshBlockTag))
                .save(consumer.withConditions(new NotCondition (new TagEmptyCondition(deshBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/desh"));

        //Signalum
        TagKey<Item> signalumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/signalum")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(signalumBlockTag), 70)
                .unlockedBy("has_item", has(signalumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(signalumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/signalum"));

        //Sapphire
        TagKey<Item> sapphireBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/sapphire")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(sapphireBlockTag), 80)
                .unlockedBy("has_item", has(sapphireBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(sapphireBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/sapphire"));

        //Ruby
        TagKey<Item> rubyBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/ruby")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(rubyBlockTag), 80)
                .unlockedBy("has_item", has(rubyBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(rubyBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/ruby"));

        //Redstone Alloy
        TagKey<Item> redstoneAlloyBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/redstone_alloy")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(redstoneAlloyBlockTag), 100)
                .unlockedBy("has_item", has(redstoneAlloyBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(redstoneAlloyBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/redstone_alloy"));

        //Redstone
        TagKey<Item> redstoneBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/redstone")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(redstoneBlockTag), 170)
                .unlockedBy("has_item", has(redstoneBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(redstoneBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/redstone"));

        //Quartz
        TagKey<Item> quartzBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/quartz")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(quartzBlockTag), 140)
                .unlockedBy("has_item", has(quartzBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(quartzBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/quartz"));

        //Pulsating Alloy
        TagKey<Item> pulsatingAlloyBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/pulsating_alloy")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(pulsatingAlloyBlockTag), 70)
                .unlockedBy("has_item", has(pulsatingAlloyBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(pulsatingAlloyBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/pulsating_alloy"));

        //Peridot
        TagKey<Item> peridotBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/peridot")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(peridotBlockTag), 80)
                .unlockedBy("has_item", has(peridotBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(peridotBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/peridot"));

        //Ostrum
        TagKey<Item> ostrumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/ostrum")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(ostrumBlockTag), 80)
                .unlockedBy("has_item", has(ostrumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(ostrumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/ostrum"));

        //Osmium
        TagKey<Item> osmiumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/osmium")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(osmiumBlockTag), 100)
                .unlockedBy("has_item", has(osmiumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(osmiumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/osmium"));

        //Nickel
        TagKey<Item> nickelBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/nickel")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(nickelBlockTag), 130)
                .unlockedBy("has_item", has(nickelBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(nickelBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/nickel"));

        //Netherite
        TagKey<Item> netheriteBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/netherite")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(netheriteBlockTag), 50)
                .unlockedBy("has_item", has(netheriteBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(netheriteBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/netherite"));

        //Zinc
        TagKey<Item> zincBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/zinc")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(zincBlockTag), 120)
                .unlockedBy("has_item", has(zincBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(zincBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/zinc"));

        //Yellorium
        TagKey<Item> yelloriumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/yellorium")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(yelloriumBlockTag), 85)
                .unlockedBy("has_item", has(yelloriumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(yelloriumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/yellorium"));

        //Vibrant Alloy
        TagKey<Item> vibrantAlloyBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/vibrant_alloy")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(vibrantAlloyBlockTag), 35)
                .unlockedBy("has_item", has(vibrantAlloyBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(vibrantAlloyBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/vibrant_alloy"));

        //uranium
        TagKey<Item> uraniumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/uranium")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(uraniumBlockTag), 85)
                .unlockedBy("has_item", has(uraniumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(uraniumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/uranium"));

        //Tin
        TagKey<Item> tinBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/tin")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(tinBlockTag), 140)
                .unlockedBy("has_item", has(tinBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(tinBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/tin"));

        //Steel
        TagKey<Item> steelBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/steel")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(steelBlockTag), 130)
                .unlockedBy("has_item", has(steelBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(steelBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/steel"));

        //Soularium
        TagKey<Item> solariumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/solarium")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(solariumBlockTag), 65)
                .unlockedBy("has_item", has(solariumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(solariumBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/solarium"));

        //Silver
        TagKey<Item> silverBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/silver")))));
        SpeedUpgradesRecipeBuilder.SpeedUpgradesRecipeBuilder(Ingredient.of(silverBlockTag), 110)
                .unlockedBy("has_item", has(silverBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(silverBlockTag))),
                        ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades/silver"));

        // ********** Fluid Generator ********** //

        //Water
        FluidGeneratorRecipeBuilder.FluidGeneratorRecipeBuilder(new FluidStack(Fluids.WATER, 100))
                .unlockedBy("has_item", has(Items.WATER_BUCKET))
                .save(consumer);

        //Lava
        FluidGeneratorRecipeBuilder.FluidGeneratorRecipeBuilder(new FluidStack(Fluids.LAVA, 50))
                .unlockedBy("has_item", has(Items.LAVA_BUCKET))
                .save(consumer);

        // ********** Summoning Block ********** //

        //Armadillo
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.SPIDER_EYE), 1),
                        Ingredient.of(ItemTags.TERRACOTTA), "minecraft:armadillo")
                .unlockedBy("has_item", has(ItemTags.TERRACOTTA))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/armadillo"));

        //Chicken
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Tags.Items.SEEDS), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:chicken")
                .unlockedBy("has_item", has(Tags.Items.SEEDS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/chicken"));

        //Cow
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.LEATHER), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:cow")
                .unlockedBy("has_item", has(Items.LEATHER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/cow"));

        //Pig
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.POTATO), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:pig")
                .unlockedBy("has_item", has(Items.POTATO))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/pig"));

        //Sheep
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(ItemTags.WOOL), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:sheep")
                .unlockedBy("has_item", has(ItemTags.WOOL))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/sheep"));

        //Wolf
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.BONE), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:wolf")
                .unlockedBy("has_item", has(Items.BONE))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/wolf"));

        //Cat
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.COD), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:cat")
                .unlockedBy("has_item", has(Items.COD))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/cat"));

        //Fox
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.SWEET_BERRIES), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:fox")
                .unlockedBy("has_item", has(Items.SWEET_BERRIES))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/fox"));

        //Panda
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.BAMBOO), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:panda")
                .unlockedBy("has_item", has(Items.BAMBOO))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/panda"));

        //Polar Bear
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.SALMON), 1),
                        Ingredient.of( new ItemStack(Blocks.SNOW_BLOCK)), "minecraft:polar_bear")
                .unlockedBy("has_item", has(Items.SALMON))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/polar_bear"));

        //Bee
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(ItemTags.SMALL_FLOWERS), 1),
                        Ingredient.of( new ItemStack(Items.BEEHIVE)), "minecraft:bee")
                .unlockedBy("has_item", has(ItemTags.SMALL_FLOWERS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/bee"));

        //Goat
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.WHEAT), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:goat")
                .unlockedBy("has_item", has(Items.WHEAT))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/goat"));

        //Horse
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.HAY_BLOCK), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:horse")
                .unlockedBy("has_item", has(Items.HAY_BLOCK))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/horse"));

        //Donkey
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.GOLDEN_CARROT), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:donkey")
                .unlockedBy("has_item", has(Items.GOLDEN_CARROT))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/donkey"));

        //Bat
        /*
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.PUMPKIN_PIE), 1),
                        Ingredient.of( new ItemStack(Items.CAVE_AIR)), "minecraft:bat")
                .unlockedBy("has_item", has(Items.PUMPKIN_PIE))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/bat"));
         */

        //Rabbit
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.CARROT), 1),
                        Ingredient.of(ItemTags.DIRT), "minecraft:rabbit")
                .unlockedBy("has_item", has(Items.CARROT))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/rabbit"));

        //Squid
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.INK_SAC), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:squid")
                .unlockedBy("has_item", has(Items.INK_SAC))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/squid"));

        //Glow Squid
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.GLOW_INK_SAC), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:glow_squid")
                .unlockedBy("has_item", has(Items.GLOW_INK_SAC))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/glow_squid"));

        //Dolphin
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.COD), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:dolphin")
                .unlockedBy("has_item", has(Items.COD))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/dolphin"));

        //Turtle
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.SEAGRASS), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:turtle")
                .unlockedBy("has_item", has(Items.SEAGRASS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/turtle"));

        //Axolotl
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.TROPICAL_FISH), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:axolotl")
                .unlockedBy("has_item", has(Items.TROPICAL_FISH))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/axolotl"));

        //Parrot
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.COCOA_BEANS), 1),
                        Ingredient.of(new ItemStack(Items.JUNGLE_LEAVES)), "minecraft:parrot")
                .unlockedBy("has_item", has(Items.COCOA_BEANS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/parrot"));

        //Camel
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.CACTUS), 1),
                        Ingredient.of(new ItemStack(Items.SAND)), "minecraft:camel")
                .unlockedBy("has_item", has(Items.CACTUS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/camel"));

        //Frog
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.LILY_PAD), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:frog")
                .unlockedBy("has_item", has(Items.LILY_PAD))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/frog"));

        //Mooshroom
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Tags.Items.MUSHROOMS), 1),
                        Ingredient.of(new ItemStack(Items.MYCELIUM)), "minecraft:mooshroom")
                .unlockedBy("has_item", has(Tags.Items.MUSHROOMS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/mooshroom"));

        //Llama
        SummoningRecipeBuilder.SummoningRecipeBuilder(new SizedIngredient(Ingredient.of(Items.LEAD), 1),
                        Ingredient.of(new ItemStack(Items.GRASS_BLOCK)), "minecraft:llama")
                .unlockedBy("has_item", has(Items.LEAD))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block/llama"));

        // ********** Cloche ********** //

        //Wheat
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.WHEAT_SEEDS), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.WHEAT, 1),
                        new ItemStack(Items.WHEAT_SEEDS), 0.15,
                        null, 0,
                        null, 0,
                        1.8)
                .unlockedBy("has_item", has(Items.WHEAT_SEEDS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/wheat"));

        //Beetroot
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.BEETROOT_SEEDS), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.BEETROOT, 1),
                        new ItemStack(Items.BEETROOT_SEEDS), 0.15,
                        null, 0,
                        null, 0,
                        1.8)
                .unlockedBy("has_item", has(Items.BEETROOT_SEEDS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/beetroot"));

        //Carrot
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.CARROT), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.CARROT, 1),
                        null, 0,
                        null, 0,
                        null, 0,
                        1.8)
                .unlockedBy("has_item", has(Items.CARROT))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/carrot"));

        //Potato
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.POTATO), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.POTATO, 1),
                        new ItemStack(Items.POISONOUS_POTATO), 0.01,
                        null, 0,
                        null, 0,
                        1.8)
                .unlockedBy("has_item", has(Items.POTATO))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/potato"));

        //Melon
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.MELON_SEEDS), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.MELON, 1),
                        new ItemStack(Items.MELON_SEEDS), 0.15,
                        null, 0,
                        null, 0,
                        3.0)
                .unlockedBy("has_item", has(Items.MELON_SEEDS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/melon"));

        //Pumpkin
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.PUMPKIN_SEEDS), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.PUMPKIN, 1),
                        new ItemStack(Items.PUMPKIN_SEEDS), 0.15,
                        null, 0,
                        null, 0,
                        3.0)
                .unlockedBy("has_item", has(Items.PUMPKIN_SEEDS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/pumpkin"));

        //Sugar Cane
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.SUGAR_CANE), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.SUGAR_CANE, 1),
                        null, 0,
                        null, 0,
                        null, 0,
                        2.6)
                .unlockedBy("has_item", has(Items.SUGAR_CANE))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/sugar_cane"));

        //Cactus
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.CACTUS), null, Ingredient.of(ItemTags.SAND),
                        new ItemStack(Items.CACTUS, 1),
                        null, 0,
                        null, 0,
                        null, 0,
                        2.6)
                .unlockedBy("has_item", has(Items.CACTUS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/cactus"));

        //Bamboo
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.BAMBOO), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.BAMBOO, 1),
                        null, 0,
                        null, 0,
                        null, 0,
                        2.6)
                .unlockedBy("has_item", has(Items.BAMBOO))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/bamboo"));

        //Oak Sapling
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.OAK_SAPLING), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.OAK_LOG, 2),
                        new ItemStack(Items.OAK_SAPLING), 0.15,
                        new ItemStack(Items.APPLE), 0.05,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.OAK_SAPLING))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/oak_sapling"));

        //Spruce Sapling
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.SPRUCE_SAPLING), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.SPRUCE_LOG, 2),
                        new ItemStack(Items.SPRUCE_SAPLING), 0.15,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.SPRUCE_SAPLING))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/spruce_sapling"));

        //Birch Sapling
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.BIRCH_SAPLING), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.BIRCH_LOG, 2),
                        new ItemStack(Items.BIRCH_SAPLING), 0.15,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.BIRCH_SAPLING))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/birch_sapling"));

        //Jungle Sapling
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.JUNGLE_SAPLING), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.JUNGLE_LOG, 2),
                        new ItemStack(Items.JUNGLE_SAPLING), 0.15,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.JUNGLE_SAPLING))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/jungle_sapling"));

        //Acacia Sapling
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.ACACIA_SAPLING), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.ACACIA_LOG, 2),
                        new ItemStack(Items.ACACIA_SAPLING), 0.15,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.ACACIA_SAPLING))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/acacia_sapling"));

        //Dark Oak Sapling
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.DARK_OAK_SAPLING), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.DARK_OAK_LOG, 2),
                        new ItemStack(Items.DARK_OAK_SAPLING), 0.15,
                        new ItemStack(Items.APPLE), 0.05,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.DARK_OAK_SAPLING))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/dark_oak_sapling"));

        //Cherry Sapling
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.CHERRY_SAPLING), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.CHERRY_LOG, 2),
                        new ItemStack(Items.CHERRY_SAPLING), 0.15,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.CHERRY_SAPLING))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/cherry_sapling"));

        //Mangrove Sapling
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.MANGROVE_PROPAGULE), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.MANGROVE_LOG, 2),
                        new ItemStack(Items.MANGROVE_PROPAGULE), 0.15,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.MANGROVE_PROPAGULE))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/mangrove_sapling"));

        //Warped Fungus
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.WARPED_FUNGUS), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.WARPED_STEM, 2),
                        new ItemStack(Items.WARPED_FUNGUS), 0.15,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.WARPED_FUNGUS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/warped_fungus"));

        //Crimson Fungus
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.CRIMSON_FUNGUS), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.CRIMSON_STEM, 2),
                        new ItemStack(Items.CRIMSON_FUNGUS), 0.15,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.CRIMSON_FUNGUS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/crimson_fungus"));

        //Glow Lichen
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.GLOW_LICHEN), null, Ingredient.of(Tags.Items.STONES),
                        new ItemStack(Items.GLOW_LICHEN, 1),
                        null, 0,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.GLOW_LICHEN))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/glow_lichen"));

        //Cocoa Beans
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.COCOA_BEANS), null, Ingredient.of(ItemTags.JUNGLE_LOGS),
                        new ItemStack(Items.COCOA_BEANS, 1),
                        null, 0,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.COCOA_BEANS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/cocoa_beans"));

        //Vines
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.VINE), null, Ingredient.of(ItemTags.JUNGLE_LOGS),
                        new ItemStack(Items.VINE, 1),
                        null, 0,
                        null, 0,
                        null, 0,
                        2.0)
                .unlockedBy("has_item", has(Items.VINE))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/vines"));

        //Brown Mushroom
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.BROWN_MUSHROOM), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.BROWN_MUSHROOM, 1),
                        null, 0,
                        null, 0,
                        null, 0,
                        3.0)
                .unlockedBy("has_item", has(Items.BROWN_MUSHROOM))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/brown_mushroom"));

        //Red Mushroom
        ClocheRecipeBuilder.ClocheBuilder(Ingredient.of(Items.RED_MUSHROOM), null, Ingredient.of(ItemTags.DIRT),
                        new ItemStack(Items.RED_MUSHROOM, 1),
                        null, 0,
                        null, 0,
                        null, 0,
                        3.0)
                .unlockedBy("has_item", has(Items.RED_MUSHROOM))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/red_mushroom"));




























    }

}
