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

    public static RecipeType<RG2BlocksRecipe> RG2_BLOCKS =
            new RecipeType<>(RG2BlocksRecipeCategory.UID, RG2BlocksRecipe.class);

    public static RecipeType<RG2SpeedBlocksRecipe> RG2_SPEED_BLOCKS =
            new RecipeType<>(RG2SpeedBlocksRecipeCategory.UID, RG2SpeedBlocksRecipe.class);

    public static RecipeType<FluidGeneratorRecipe> FLUID_GENERATOR =
            new RecipeType<>(FluidGeneratorRecipeCategory.UID, FluidGeneratorRecipe.class);

    public static RecipeType<DryingTableRecipe> DRYING_TABLE =
            new RecipeType<>(DryingTableRecipeCategory.UID, DryingTableRecipe.class);

    public static RecipeType<SoakingTableRecipe> SOAKING_TABLE =
            new RecipeType<>(SoakingTableRecipeCategory.UID, SoakingTableRecipe.class);

    public static RecipeType<UpgradeRecipeUtil> UPGRADE_RECIPE_UTIL =
            new RecipeType<>(UpgradeRecipeUtilCategory.UID, UpgradeRecipeUtil.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(OpolisUtilities.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DRYING_TABLE.get()), DryingTableRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DRYING_TABLE.get()), SoakingTableRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RESOURCE_GENERATOR.get()), ResourceGeneratorRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()), RG2BlocksRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()), RG2SpeedBlocksRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_GENERATOR.get()), FluidGeneratorRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_GENERATOR.get()), RG2SpeedBlocksRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CATALOGUE.get()), CatalogueRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.CATALOGUE_BOOK.get()), CatalogueRecipeCategory.RECIPE_TYPE);

        //Empty Catalyst (No upgrade compatible machines in opolis utilities yet)
        registration.addRecipeCatalyst(new ItemStack(ModItems.UPGRADE_BASE.get()) , UpgradeRecipeUtilCategory.RECIPE_TYPE);

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
                RG2BlocksRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                RG2SpeedBlocksRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                FluidGeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                CatalogueRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                UpgradeRecipeUtilCategory(registration.getJeiHelpers().getGuiHelper()));

        slotDrawable = guiHelper.getSlotDrawable();
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<DryingTableRecipe> recipes = rm.getAllRecipesFor(DryingTableRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(DryingTableRecipeCategory.UID, DryingTableRecipe.class), recipes);

        List<ResourceGeneratorRecipe> recipes2 = rm.getAllRecipesFor(ResourceGeneratorRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(ResourceGeneratorRecipeCategory.UID, ResourceGeneratorRecipe.class), recipes2);

        List<RG2BlocksRecipe> recipes4 = rm.getAllRecipesFor(RG2BlocksRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(RG2BlocksRecipeCategory.UID, RG2BlocksRecipe.class), recipes4);

        List<RG2SpeedBlocksRecipe> recipes5 = rm.getAllRecipesFor(RG2SpeedBlocksRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(RG2SpeedBlocksRecipeCategory.UID, RG2SpeedBlocksRecipe.class), recipes5);

        List<FluidGeneratorRecipe> recipes6 = rm.getAllRecipesFor(FluidGeneratorRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(FluidGeneratorRecipeCategory.UID, FluidGeneratorRecipe.class), recipes6);

        List<CatalogueRecipe> recipes7 = rm.getAllRecipesFor(CatalogueRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(CatalogueRecipeCategory.UID, CatalogueRecipe.class), recipes7);

        List<SoakingTableRecipe> recipes8 = rm.getAllRecipesFor(SoakingTableRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(SoakingTableRecipeCategory.UID, SoakingTableRecipe.class), recipes8);

        List<UpgradeRecipeUtil> recipes9 = rm.getAllRecipesFor(UpgradeRecipeUtil.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(UpgradeRecipeUtilCategory.UID, UpgradeRecipeUtil.class), recipes9);

    }
}


