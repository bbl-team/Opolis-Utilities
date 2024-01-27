package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER=
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OpolisUtilities.MOD_ID);


    public static final RegistryObject<RecipeSerializer<DryingTableRecipe>> DRYING_TABLE_SERIALIZER =
            SERIALIZER.register("drying_table", () -> DryingTableRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<SoakingTableRecipe>> SOAKING_TABLE_SERIALIZER =
            SERIALIZER.register("soaking_table", () -> SoakingTableRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<ResourceGeneratorRecipe>> RESOURCE_GENERATOR_SERIALIZER =
            SERIALIZER.register("resource_generator", () -> ResourceGeneratorRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<RG2SpeedBlocksRecipe>> RG2_SPEED_BLOCKS_SERIALIZER =
            SERIALIZER.register("rg2_speed_blocks", () -> RG2SpeedBlocksRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<RG2BlocksRecipe>> RG2_BLOCKS_SERIALIZER =
            SERIALIZER.register("rg2_blocks", () -> RG2BlocksRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<FluidGeneratorRecipe>> FLUID_GENERATOR_SERIALIZER =
            SERIALIZER.register("fluid_generator", () -> FluidGeneratorRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<UpgradeRecipeUtil>> UPGRADE_RECIPE_UTIL_SERIALIZER =
            SERIALIZER.register("upgrades", () -> UpgradeRecipeUtil.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<CatalogueRecipe>> CATALOGUE_SERIALIZER =
            SERIALIZER.register("catalogue", () -> CatalogueRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZER.register(eventBus);
    }


}
