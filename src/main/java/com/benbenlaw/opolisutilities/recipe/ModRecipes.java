package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER=
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OpolisUtilities.MOD_ID);


    public static final RegistryObject<RecipeSerializer<DryingTableRecipe>> DRYING_TABLE_SERIALIZER =
            SERIALIZER.register("drying_table", () -> DryingTableRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<ResourceGeneratorRecipe>> RESOURCE_GENERATOR_SERIALIZER =
            SERIALIZER.register("resource_generator", () -> ResourceGeneratorRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<ShopRecipe>> SHOP_SERIALIZER =
            SERIALIZER.register("shop", () -> ShopRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<RG2SpeedBlocksRecipe>> RG2_SPEED_BLOCKS_SERIALIZER =
            SERIALIZER.register("rg2_speed_blocks", () -> RG2SpeedBlocksRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<RG2BlocksRecipe>> RG2_BLOCKS_SERIALIZER =
            SERIALIZER.register("rg2_blocks", () -> RG2BlocksRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZER.register(eventBus);
    }


}
