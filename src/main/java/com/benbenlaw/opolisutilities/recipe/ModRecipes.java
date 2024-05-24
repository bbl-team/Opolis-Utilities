package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, OpolisUtilities.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, OpolisUtilities.MOD_ID);

/*
    public static final RegistryObject<RecipeSerializer<DryingTableRecipe>> DRYING_TABLE_SERIALIZER =
            SERIALIZER.register("drying_table", () -> DryingTableRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<SoakingTableRecipe>> SOAKING_TABLE_SERIALIZER =
            SERIALIZER.register("soaking_table", () -> SoakingTableRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<ResourceGeneratorRecipe>> RESOURCE_GENERATOR_SERIALIZER =
            SERIALIZER.register("resource_generator", () -> ResourceGeneratorRecipe.Serializer.INSTANCE);
            */

    //SPEED BLOCK
    public static final Supplier<RecipeSerializer<SpeedUpgradesRecipe>> RG2_SPEED_BLOCKS_SERIALIZER =
            SERIALIZER.register("speed_blocks", () -> SpeedUpgradesRecipe.Serializer.INSTANCE);
    public static final Supplier<RecipeType<SpeedUpgradesRecipe>> RG2_SPEED_BLOCKS_TYPE =
            TYPES.register("speed_blocks", () -> SpeedUpgradesRecipe.Type.INSTANCE);


    //Resource Generator Blocks
    public static final Supplier<RecipeSerializer<ResourceGeneratorRecipe>> RG2_BLOCKS_SERIALIZER =
            SERIALIZER.register("resource_generator", () -> ResourceGeneratorRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<ResourceGeneratorRecipe>> RG2_BLOCKS_TYPE =
            TYPES.register("resource_generator", () -> ResourceGeneratorRecipe.Type.INSTANCE);


    /*
    public static final RegistryObject<RecipeSerializer<FluidGeneratorRecipe>> FLUID_GENERATOR_SERIALIZER =
            SERIALIZER.register("fluid_generator", () -> FluidGeneratorRecipe.Serializer.INSTANCE);


    public static final RegistryObject<RecipeSerializer<UpgradeRecipeUtil>> UPGRADE_RECIPE_UTIL_SERIALIZER =
            SERIALIZER.register("upgrades", () -> UpgradeRecipeUtil.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<CatalogueRecipe>> CATALOGUE_SERIALIZER =
            SERIALIZER.register("catalogue", () -> CatalogueRecipe.Serializer.INSTANCE);
            */


    public static void register(IEventBus eventBus) {
        SERIALIZER.register(eventBus);
    }




}
