package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.recipe.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


@JeiPlugin
public class  JEIOpolisUtilitiesPlugin implements IModPlugin {

    public static IDrawableStatic slotDrawable;

    public static RecipeType<CatalogueRecipe> CATALOGUE_RECIPE =
            new RecipeType<>(CatalogueRecipeCategory.UID, CatalogueRecipe.class);

    public static RecipeType<ResourceGeneratorRecipe> RESOURCE_GENERATOR =
            new RecipeType<>(ResourceGeneratorRecipeCategory.UID, ResourceGeneratorRecipe.class);

    public static RecipeType<SpeedUpgradesRecipe> SPEED_UPGRADES =
            new RecipeType<>(SpeedUpgradesRecipeCategory.UID, SpeedUpgradesRecipe.class);

    public static RecipeType<FluidGeneratorRecipe> FLUID_GENERATOR =
            new RecipeType<>(FluidGeneratorRecipeCategory.UID, FluidGeneratorRecipe.class);

    public static RecipeType<DryingTableRecipe> DRYING_TABLE =
            new RecipeType<>(DryingTableRecipeCategory.UID, DryingTableRecipe.class);

    public static RecipeType<SoakingTableRecipe> SOAKING_TABLE =
            new RecipeType<>(SoakingTableRecipeCategory.UID, SoakingTableRecipe.class);
    public static RecipeType<SummoningBlockRecipe> SUMMOMING_BLOCK =
            new RecipeType<>(SummoningRecipeCategory.UID, SummoningBlockRecipe.class);

    public static RecipeType<ClocheRecipe> CLOCHE =
            new RecipeType<>(ClocheRecipeCategory.UID, ClocheRecipe.class);


    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DRYING_TABLE.get()), DryingTableRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DRYING_TABLE.get()), SoakingTableRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RESOURCE_GENERATOR.get()), ResourceGeneratorRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_GENERATOR.get()), FluidGeneratorRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CATALOGUE.get()), CatalogueRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.CATALOGUE_BOOK.get()), CatalogueRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SUMMONING_BLOCK.get()), SummoningRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CLOCHE.get()), ClocheRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RESOURCE_GENERATOR.get()), SpeedUpgradesRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ITEM_REPAIRER.get()), SpeedUpgradesRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_GENERATOR.get()), SpeedUpgradesRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CLOCHE.get()), SpeedUpgradesRecipeCategory.RECIPE_TYPE);

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new
                DryingTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                SoakingTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                ResourceGeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                SpeedUpgradesRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                FluidGeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                CatalogueRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                SummoningRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                ClocheRecipeCategory(registration.getJeiHelpers().getGuiHelper()));


        slotDrawable = guiHelper.getSlotDrawable();
    }



    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        final var recipeManager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(CatalogueRecipeCategory.RECIPE_TYPE,
                recipeManager.getAllRecipesFor(ModRecipes.CATALOGUE_TYPE.get()).stream().map(RecipeHolder::value).toList());

        registration.addRecipes(DryingTableRecipeCategory.RECIPE_TYPE,
                recipeManager.getAllRecipesFor(ModRecipes.DRYING_TABLE_TYPE.get()).stream().map(RecipeHolder::value).toList());

        registration.addRecipes(SoakingTableRecipeCategory.RECIPE_TYPE,
                recipeManager.getAllRecipesFor(ModRecipes.SOAKING_TABLE_TYPE.get()).stream().map(RecipeHolder::value).toList());

        registration.addRecipes(ResourceGeneratorRecipeCategory.RECIPE_TYPE,
                recipeManager.getAllRecipesFor(ModRecipes.RESOURCE_GENERATOR_TYPE.get()).stream().map(RecipeHolder::value).toList());

        registration.addRecipes(FluidGeneratorRecipeCategory.RECIPE_TYPE,
                recipeManager.getAllRecipesFor(ModRecipes.FLUID_GENERATOR_TYPE.get()).stream().map(RecipeHolder::value).toList());

        registration.addRecipes(SpeedUpgradesRecipeCategory.RECIPE_TYPE,
                recipeManager.getAllRecipesFor(ModRecipes.SPEED_UPGRADE_TYPE.get()).stream().map(RecipeHolder::value).toList());

        registration.addRecipes(SummoningRecipeCategory.RECIPE_TYPE,
                recipeManager.getAllRecipesFor(ModRecipes.SUMMONING_BLOCK_TYPE.get()).stream().map(RecipeHolder::value).toList());

        registration.addRecipes(ClocheRecipeCategory.RECIPE_TYPE,
                recipeManager.getAllRecipesFor(ModRecipes.CLOCHE_TYPE.get()).stream().map(RecipeHolder::value).toList());





    }

}


