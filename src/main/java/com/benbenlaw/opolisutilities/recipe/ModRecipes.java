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


    //Drying Table
    public static final Supplier<RecipeSerializer<DryingTableRecipe>> DRYING_TABLE_SERIALIZER =
            SERIALIZER.register("drying_table", () -> DryingTableRecipe.Serializer.INSTANCE);
    public static final Supplier<RecipeType<DryingTableRecipe>> DRYING_TABLE_TYPE =
            TYPES.register("drying_table", () -> DryingTableRecipe.Type.INSTANCE);

    //Soaking Table

    public static final Supplier<RecipeSerializer<SoakingTableRecipe>> SOAKING_TABLE_SERIALIZER =
            SERIALIZER.register("soaking_table", () -> SoakingTableRecipe.Serializer.INSTANCE);
    public static final Supplier<RecipeType<SoakingTableRecipe>> SOAKING_TABLE_TYPE =
            TYPES.register("soaking_table", () -> SoakingTableRecipe.Type.INSTANCE);


    //Speed Upgrades
    public static final Supplier<RecipeSerializer<SpeedUpgradesRecipe>> SPEED_UPGRADE_SERIALIZER =
            SERIALIZER.register("speed_upgrades", () -> SpeedUpgradesRecipe.Serializer.INSTANCE);
    public static final Supplier<RecipeType<SpeedUpgradesRecipe>> SPEED_UPGRADE_TYPE =
            TYPES.register("speed_upgrades", () -> SpeedUpgradesRecipe.Type.INSTANCE);

    //Resource Generator
    public static final Supplier<RecipeSerializer<ResourceGeneratorRecipe>> RESOURCE_GENERATOR_SERIALIZER =
            SERIALIZER.register("resource_generator", () -> ResourceGeneratorRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<ResourceGeneratorRecipe>> RESOURCE_GENERATOR_TYPE =
            TYPES.register("resource_generator", () -> ResourceGeneratorRecipe.Type.INSTANCE);


    //Fluid Generator
    public static final Supplier<RecipeSerializer<FluidGeneratorRecipe>> FLUID_GENERATOR_SERIALIZER =
            SERIALIZER.register("fluid_generator", () -> FluidGeneratorRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<FluidGeneratorRecipe>> FLUID_GENERATOR_TYPE =
            TYPES.register("fluid_generator", () -> FluidGeneratorRecipe.Type.INSTANCE);

    //Summoning Block
    public static final Supplier<RecipeSerializer<SummoningBlockRecipe>> SUMMONING_BLOCK_SERIALIZER =
            SERIALIZER.register("summoning_block", () -> SummoningBlockRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<SummoningBlockRecipe>> SUMMONING_BLOCK_TYPE =
            TYPES.register("summoning_block", () -> SummoningBlockRecipe.Type.INSTANCE);

    //Catalogue

    public static final Supplier<RecipeSerializer<CatalogueRecipe>> CATALOGUE_SERIALIZER =
        SERIALIZER.register("catalogue", () -> CatalogueRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<CatalogueRecipe>> CATALOGUE_TYPE =
            TYPES.register("catalogue", () -> CatalogueRecipe.Type.INSTANCE);

    //Cloche

    public static final Supplier<RecipeSerializer<ClocheRecipe>> CLOCHE_SERIALIZER =
            SERIALIZER.register("cloche", () -> ClocheRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<ClocheRecipe>> CLOCHE_TYPE =
            TYPES.register("cloche", () -> ClocheRecipe.Type.INSTANCE);




    public static void register(IEventBus eventBus) {
        SERIALIZER.register(eventBus);
        TYPES.register(eventBus);
    }




}
