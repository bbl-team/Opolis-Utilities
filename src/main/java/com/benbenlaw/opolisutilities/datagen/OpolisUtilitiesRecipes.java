package com.benbenlaw.opolisutilities.datagen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.datagen.recipes.*;
import com.benbenlaw.opolisutilities.item.ModItems;
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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "block_breaker"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "charcoal"));

        //Coal
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COAL)
                .requires(ModItems.MINI_COAL, 8)
                .unlockedBy("has_item", has(ModItems.MINI_COAL))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "coal"));

        //Chests
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHEST, 4)
                .pattern("LLL")
                .pattern("L L")
                .pattern("LLL")
                .define('L', ItemTags.LOGS)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ItemTags.LOGS))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "chests"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "copper_ingot"));

        //Crafter
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CRAFTER.get())
                .pattern("SSS")
                .pattern("ACA")
                .pattern("SSS")
                .define('S', Tags.Items.STONES)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('C', Items.CRAFTING_TABLE)
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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "diamond_horse_armour"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "ender_pearl"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "golden_horse_armour"));

        //Green Wool
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.GREEN_WOOL)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModItems.LEAFY_STRING)
                .group("opolisutilities")
                .unlockedBy("has_item", has(ModItems.LEAFY_STRING))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "green_wool"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "iron_horse_armour"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "leather_horse_armour"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "name_tag"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "saddle"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "sticks"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "drying_table/cracked_stone_bricks"));

        //Dead Bush
        DryingTableRecipeBuilder.dryingTableRecipe(new SizedIngredient(Ingredient.of(ItemTags.SAPLINGS), 1),
                        new ItemStack(Items.DEAD_BUSH), 100)
                .unlockedBy("has_item", has(Items.DEAD_BUSH))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "drying_table/dead_bush"));

        //Jerky
        DryingTableRecipeBuilder.dryingTableRecipe(new SizedIngredient(Ingredient.of(Tags.Items.FOODS_RAW_MEATS), 1),
                        new ItemStack(ModItems.JERKY.get()), 200)
                .unlockedBy("has_item", has(Tags.Items.FOODS_RAW_MEATS))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "drying_table/jerky"));

        //Paper
        DryingTableRecipeBuilder.dryingTableRecipe(new SizedIngredient(Ingredient.of(ModItems.SOAKED_PAPER), 1),
                        new ItemStack(Items.PAPER), 200)
                .unlockedBy("has_item", has(ModItems.SOAKED_PAPER))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "drying_table/paper"));

        //Sponge
        DryingTableRecipeBuilder.dryingTableRecipe(new SizedIngredient(Ingredient.of(Items.WET_SPONGE), 1),
                        new ItemStack(Items.SPONGE), 200)
                .unlockedBy("has_item", has(Items.WET_SPONGE))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "drying_table/sponge"));

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
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "resource_generator/stones"));

        //Cobblestone
        ResourceGeneratorRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Tags.Items.COBBLESTONES))
                .unlockedBy("has_item", has(Tags.Items.COBBLESTONES))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "resource_generator/cobblestone"));

        // ********** Soaking Table ********** //

        //Mud
        SoakingTableRecipeBuilder.soakingTableRecipe(new SizedIngredient(Ingredient.of(ItemTags.DIRT), 1),
                        new ItemStack(Items.MUD), 200)
                .unlockedBy("has_item", has(ItemTags.DIRT))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "soaking_table/mud"));

        //Soaked Paper
        SoakingTableRecipeBuilder.soakingTableRecipe(new SizedIngredient(Ingredient.of(ModItems.LOG_SHEET), 1),
                        new ItemStack(ModItems.SOAKED_PAPER.get()), 200)
                .unlockedBy("has_item", has(ModItems.LOG_SHEET))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "soaking_table/soaked_paper"));

        //Water Bottle
        Holder<Potion> water = (Potions.WATER);
        ItemStack itemStack = new ItemStack(Items.POTION);
        itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(water));

        SoakingTableRecipeBuilder.soakingTableRecipe(new SizedIngredient(Ingredient.of(Items.GLASS_BOTTLE), 1),
                        itemStack, 200)
                .unlockedBy("has_item", has(Items.GLASS_BOTTLE))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "soaking_table/water_bottle"));

        //Wet Sponge
        SoakingTableRecipeBuilder.soakingTableRecipe(new SizedIngredient(Ingredient.of(Items.SPONGE), 1),
                        new ItemStack(Items.WET_SPONGE), 200)
                .unlockedBy("has_item", has(Items.SPONGE))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "soaking_table/wet_sponge"));

        // ********** Speed Upgrades (Vanilla) ********** //

        //Coal
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Tags.Items.STORAGE_BLOCKS_COAL), 200)
                .unlockedBy("has_item", has(Tags.Items.STORAGE_BLOCKS_COAL))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/coal"));

        //Amethyst
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Items.AMETHYST_BLOCK), 120)
                .unlockedBy("has_item", has(Items.AMETHYST_BLOCK))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/amethyst"));

        //Beacon
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Items.BEACON), 20)
                .unlockedBy("has_item", has(Items.BEACON))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/beacon"));

        //Glowstone
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Items.GLOWSTONE), 120)
                .unlockedBy("has_item", has(Items.GLOWSTONE))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/glowstone"));

        //Fences
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Tags.Items.FENCES_WOODEN), 210)
                .unlockedBy("has_item", has(Tags.Items.FENCES_WOODEN))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/wooden_fences"));

        //Stone
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Tags.Items.STONES), 200)
                .unlockedBy("has_item", has(Tags.Items.STONES))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/stone"));

        //Obsidian
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Items.OBSIDIAN), 100)
                .unlockedBy("has_item", has(Items.OBSIDIAN))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/obsidian"));

        //Nether Star
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(Items.NETHER_STAR), 30)
                .unlockedBy("has_item", has(Items.NETHER_STAR))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/nether_star"));

        // ********** Speed Upgrades (Tags) ********** //

        //Aluminum

        TagKey<Item> aluminumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/aluminum")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(aluminumBlockTag), 100)
                .unlockedBy("has_item", has(aluminumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(aluminumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/aluminum"));

        //Dark Steel
        TagKey<Item> darkSteelBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/dark_steel")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(darkSteelBlockTag), 60)
                .unlockedBy("has_item", has(darkSteelBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(darkSteelBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/dark_steel"));

        //Cyanite
        TagKey<Item> cyaniteBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/cyanite")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(cyaniteBlockTag), 50)
                .unlockedBy("has_item", has(cyaniteBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(cyaniteBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/cyanite"));

        //Copper
        TagKey<Item> copperBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/copper")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(copperBlockTag), 140)
                .unlockedBy("has_item", has(copperBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(copperBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/copper"));

        //Constantan
        TagKey<Item> constantanBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/constantan")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(constantanBlockTag), 90)
                .unlockedBy("has_item", has(constantanBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(constantanBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/constantan"));

        //Conductive Iron
        TagKey<Item> conductiveIronBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/conductive_iron")))));

        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(conductiveIronBlockTag), 60)
                .unlockedBy("has_item", has(conductiveIronBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(conductiveIronBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/conductive_iron"));

        //Compressed Iron
        TagKey<Item> compressedIronBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/compressed_iron")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(compressedIronBlockTag), 80)
                .unlockedBy("has_item", has(compressedIronBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(compressedIronBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/compressed_iron"));

        //Calorite
        TagKey<Item> caloriteBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/calorite")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(caloriteBlockTag), 60)
                .unlockedBy("has_item", has(caloriteBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(caloriteBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/calorite"));

        //Bronze
        TagKey<Item> bronzeBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/bronze")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(bronzeBlockTag), 120)
                .unlockedBy("has_item", has(bronzeBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(bronzeBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/bronze"));

        //Brass
        TagKey<Item> brassBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/brass")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(brassBlockTag), 100)
                .unlockedBy("has_item", has(brassBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(brassBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/brass"));

        //Blutonium
        TagKey<Item> blutoniumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/blutonium")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(blutoniumBlockTag), 50)
                .unlockedBy("has_item", has(blutoniumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(blutoniumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/blutonium"));

        //Lumium
        TagKey<Item> lumiumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/lumium")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(lumiumBlockTag), 40)
                .unlockedBy("has_item", has(lumiumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(lumiumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/lumium"));

        //Lead
        TagKey<Item> leadBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/lead")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(leadBlockTag), 150)
                .unlockedBy("has_item", has(leadBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(leadBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/lead"));

        //Lapis
        TagKey<Item> lapisBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/lapis")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(lapisBlockTag), 160)
                .unlockedBy("has_item", has(lapisBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(lapisBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/lapis"));

        //Iron
        TagKey<Item> ironBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/iron")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(ironBlockTag), 190)
                .unlockedBy("has_item", has(ironBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(ironBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/iron"));

        //Invar
        TagKey<Item> invarBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/invar")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(invarBlockTag), 130)
                .unlockedBy("has_item", has(invarBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(invarBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/invar"));

        //Graphite
        TagKey<Item> graphiteBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/graphite")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(graphiteBlockTag), 150)
                .unlockedBy("has_item", has(graphiteBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(graphiteBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/graphite"));

        //Gold
        TagKey<Item> goldBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/gold")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(goldBlockTag), 160)
                .unlockedBy("has_item", has(goldBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(goldBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/gold"));

        //Energetic Alloy
        TagKey<Item> energeticAlloyBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/energetic_alloy")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(energeticAlloyBlockTag), 70)
                .unlockedBy("has_item", has(energeticAlloyBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(energeticAlloyBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/energetic_alloy"));

        //End Steel
        TagKey<Item> endSteelBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/end_steel")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(endSteelBlockTag), 50)
                .unlockedBy("has_item", has(endSteelBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(endSteelBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/end_steel"));

        //Enderium
        TagKey<Item> enderiumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/enderium")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(enderiumBlockTag), 40)
                .unlockedBy("has_item", has(enderiumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(enderiumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/enderium"));

        //Emerald
        TagKey<Item> emeraldBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/emerald")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(emeraldBlockTag), 80)
                .unlockedBy("has_item", has(emeraldBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(emeraldBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/emerald"));

        //Electrum
        TagKey<Item> electrumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/electrum")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(electrumBlockTag), 115)
                .unlockedBy("has_item", has(electrumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(electrumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/electrum"));

        //Diamond
        TagKey<Item> diamondBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/diamond")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(diamondBlockTag), 100)
                .unlockedBy("has_item", has(diamondBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(diamondBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/diamond"));
        //Desh
        TagKey<Item> deshBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/desh")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(deshBlockTag), 60)
                .unlockedBy("has_item", has(deshBlockTag))
                .save(consumer.withConditions(new NotCondition (new TagEmptyCondition(deshBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/desh"));

        //Signalum
        TagKey<Item> signalumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/signalum")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(signalumBlockTag), 70)
                .unlockedBy("has_item", has(signalumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(signalumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/signalum"));

        //Sapphire
        TagKey<Item> sapphireBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/sapphire")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(sapphireBlockTag), 80)
                .unlockedBy("has_item", has(sapphireBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(sapphireBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/sapphire"));

        //Ruby
        TagKey<Item> rubyBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/ruby")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(rubyBlockTag), 80)
                .unlockedBy("has_item", has(rubyBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(rubyBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/ruby"));

        //Redstone Alloy
        TagKey<Item> redstoneAlloyBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/redstone_alloy")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(redstoneAlloyBlockTag), 100)
                .unlockedBy("has_item", has(redstoneAlloyBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(redstoneAlloyBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/redstone_alloy"));

        //Redstone
        TagKey<Item> redstoneBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/redstone")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(redstoneBlockTag), 170)
                .unlockedBy("has_item", has(redstoneBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(redstoneBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/redstone"));

        //Quartz
        TagKey<Item> quartzBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/quartz")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(quartzBlockTag), 140)
                .unlockedBy("has_item", has(quartzBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(quartzBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/quartz"));

        //Pulsating Alloy
        TagKey<Item> pulsatingAlloyBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/pulsating_alloy")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(pulsatingAlloyBlockTag), 70)
                .unlockedBy("has_item", has(pulsatingAlloyBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(pulsatingAlloyBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/pulsating_alloy"));

        //Peridot
        TagKey<Item> peridotBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/peridot")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(peridotBlockTag), 80)
                .unlockedBy("has_item", has(peridotBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(peridotBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/peridot"));

        //Ostrum
        TagKey<Item> ostrumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/ostrum")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(ostrumBlockTag), 80)
                .unlockedBy("has_item", has(ostrumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(ostrumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/ostrum"));

        //Osmium
        TagKey<Item> osmiumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/osmium")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(osmiumBlockTag), 100)
                .unlockedBy("has_item", has(osmiumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(osmiumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/osmium"));

        //Nickel
        TagKey<Item> nickelBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/nickel")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(nickelBlockTag), 130)
                .unlockedBy("has_item", has(nickelBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(nickelBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/nickel"));

        //Netherite
        TagKey<Item> netheriteBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/netherite")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(netheriteBlockTag), 50)
                .unlockedBy("has_item", has(netheriteBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(netheriteBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/netherite"));

        //Zinc
        TagKey<Item> zincBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/zinc")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(zincBlockTag), 120)
                .unlockedBy("has_item", has(zincBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(zincBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/zinc"));

        //Yellorium
        TagKey<Item> yelloriumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/yellorium")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(yelloriumBlockTag), 85)
                .unlockedBy("has_item", has(yelloriumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(yelloriumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/yellorium"));

        //Vibrant Alloy
        TagKey<Item> vibrantAlloyBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/vibrant_alloy")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(vibrantAlloyBlockTag), 35)
                .unlockedBy("has_item", has(vibrantAlloyBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(vibrantAlloyBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/vibrant_alloy"));

        //uranium
        TagKey<Item> uraniumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/uranium")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(uraniumBlockTag), 85)
                .unlockedBy("has_item", has(uraniumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(uraniumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/uranium"));

        //Tin
        TagKey<Item> tinBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/tin")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(tinBlockTag), 140)
                .unlockedBy("has_item", has(tinBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(tinBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/tin"));

        //Steel
        TagKey<Item> steelBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/steel")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(steelBlockTag), 130)
                .unlockedBy("has_item", has(steelBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(steelBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/steel"));

        //Soularium
        TagKey<Item> solariumBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/solarium")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(solariumBlockTag), 65)
                .unlockedBy("has_item", has(solariumBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(solariumBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/solarium"));

        //Silver
        TagKey<Item> silverBlockTag = ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(new ResourceLocation("c", "storage_blocks/silver")))));
        SpeedUpgradesRecipeBuilder.resourceGeneratorRecipeBuilder(Ingredient.of(silverBlockTag), 110)
                .unlockedBy("has_item", has(silverBlockTag))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(silverBlockTag))),
                        new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/silver"));

        // ********** Fluid Generator ********** //

        //Water
        FluidGeneratorRecipeBuilder.resourceGeneratorRecipeBuilder(new FluidStack(Fluids.WATER, 100))
                .unlockedBy("has_item", has(Items.WATER_BUCKET))
                .save(consumer);

        //Lava
        FluidGeneratorRecipeBuilder.resourceGeneratorRecipeBuilder(new FluidStack(Fluids.LAVA, 50))
                .unlockedBy("has_item", has(Items.LAVA_BUCKET))
                .save(consumer);

        // ********** Summoning Block ********** //

        //Chicken
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Tags.Items.SEEDS), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:chicken")
                .unlockedBy("has_item", has(Tags.Items.SEEDS))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/chicken"));

        //Cow
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.LEATHER), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:cow")
                .unlockedBy("has_item", has(Items.LEATHER))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/cow"));

        //Pig
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.POTATO), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:pig")
                .unlockedBy("has_item", has(Items.POTATO))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/pig"));

        //Sheep
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(ItemTags.WOOL), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:sheep")
                .unlockedBy("has_item", has(ItemTags.WOOL))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/sheep"));

        //Wolf
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.BONE), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:wolf")
                .unlockedBy("has_item", has(Items.BONE))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/wolf"));

        //Cat
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.COD), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:cat")
                .unlockedBy("has_item", has(Items.COD))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/cat"));

        //Fox
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.SWEET_BERRIES), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:fox")
                .unlockedBy("has_item", has(Items.SWEET_BERRIES))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/fox"));

        //Panda
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.BAMBOO), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:panda")
                .unlockedBy("has_item", has(Items.BAMBOO))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/panda"));

        //Polar Bear
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.SALMON), 1),
                        Ingredient.of( new ItemStack(Items.SNOW)), "minecraft:polar_bear")
                .unlockedBy("has_item", has(Items.SALMON))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/polar_bear"));

        //Bee
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(ItemTags.SMALL_FLOWERS), 1),
                        Ingredient.of( new ItemStack(Items.BEEHIVE)), "minecraft:bee")
                .unlockedBy("has_item", has(ItemTags.SMALL_FLOWERS))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/bee"));

        //Armadillo
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.SAND), 1),
                        Ingredient.of( new ItemStack(Items.SAND)), "minecraft:armadillo")
                .unlockedBy("has_item", has(Items.SAND))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/armadillo"));

        //Goat
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.WHEAT), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:goat")
                .unlockedBy("has_item", has(Items.WHEAT))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/goat"));

        //Horse
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.HAY_BLOCK), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:horse")
                .unlockedBy("has_item", has(Items.HAY_BLOCK))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/horse"));

        //Donkey
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.GOLDEN_CARROT), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:donkey")
                .unlockedBy("has_item", has(Items.GOLDEN_CARROT))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/donkey"));

        //Bat
        /*
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.PUMPKIN_PIE), 1),
                        Ingredient.of( new ItemStack(Items.CAVE_AIR)), "minecraft:bat")
                .unlockedBy("has_item", has(Items.PUMPKIN_PIE))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/bat"));
         */

        //Rabbit
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.CARROT), 1),
                        Ingredient.of( new ItemStack(Items.GRASS_BLOCK)), "minecraft:rabbit")
                .unlockedBy("has_item", has(Items.CARROT))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/rabbit"));

        //Squid
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.INK_SAC), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:squid")
                .unlockedBy("has_item", has(Items.INK_SAC))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/squid"));

        //Glow Squid
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.GLOW_INK_SAC), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:glow_squid")
                .unlockedBy("has_item", has(Items.GLOW_INK_SAC))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/glow_squid"));

        //Dolphin
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.COD), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:dolphin")
                .unlockedBy("has_item", has(Items.COD))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/dolphin"));

        //Turtle
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.SEAGRASS), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:turtle")
                .unlockedBy("has_item", has(Items.SEAGRASS))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/turtle"));

        //Axolotl
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.TROPICAL_FISH), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:axolotl")
                .unlockedBy("has_item", has(Items.TROPICAL_FISH))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/axolotl"));

        //Parrot
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.COCOA_BEANS), 1),
                        Ingredient.of(new ItemStack(Items.JUNGLE_LEAVES)), "minecraft:parrot")
                .unlockedBy("has_item", has(Items.COCOA_BEANS))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/parrot"));

        //Camel
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.CACTUS), 1),
                        Ingredient.of(new ItemStack(Items.SAND)), "minecraft:camel")
                .unlockedBy("has_item", has(Items.CACTUS))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/camel"));

        //Frog
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.LILY_PAD), 1),
                        Ingredient.of(new ItemStack(Items.WATER_BUCKET)), "minecraft:frog")
                .unlockedBy("has_item", has(Items.LILY_PAD))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/frog"));

        //Mooshroom
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Tags.Items.MUSHROOMS), 1),
                        Ingredient.of(new ItemStack(Items.MYCELIUM)), "minecraft:mooshroom")
                .unlockedBy("has_item", has(Tags.Items.MUSHROOMS))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/mooshroom"));

        //Llama
        SummoningRecipeBuilder.CatalogueRecipeBuilder(new SizedIngredient(Ingredient.of(Items.LEAD), 1),
                        Ingredient.of(new ItemStack(Items.GRASS_BLOCK)), "minecraft:llama")
                .unlockedBy("has_item", has(Items.LEAD))
                .save(consumer, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning_block/llama"));





























    }

}
