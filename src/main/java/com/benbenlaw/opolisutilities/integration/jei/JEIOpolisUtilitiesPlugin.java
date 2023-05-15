package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
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
public class JEIOpolisUtilitiesPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(OpolisUtilities.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DRYING_TABLE.get()), DryingTableRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RESOURCE_GENERATOR.get()), ResourceGeneratorRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()), RG2BlocksRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()), RG2SpeedBlocksRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SHOP.get()), ShopRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new
                DryingTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                ResourceGeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                RG2BlocksRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                RG2SpeedBlocksRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                ShopRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
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

        List<ShopRecipe> recipes6 = rm.getAllRecipesFor(ShopRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(ShopRecipeCategory.UID, ShopRecipe.class), recipes6);

    }
}
