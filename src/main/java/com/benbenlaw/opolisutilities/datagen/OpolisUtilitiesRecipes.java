package com.benbenlaw.opolisutilities.datagen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.datagen.recipes.CatalogueRecipeBuilder;
import com.benbenlaw.opolisutilities.datagen.recipes.DryingTableRecipeBuilder;
import com.benbenlaw.opolisutilities.datagen.recipes.ResourceGeneratorRecipeBuilder;
import com.benbenlaw.opolisutilities.datagen.recipes.SoakingTableRecipeBuilder;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.recipe.CatalogueRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

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


        //Drying Tables Recipes

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

        //Catalogue

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

        //Soaking Table

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




    }

}
