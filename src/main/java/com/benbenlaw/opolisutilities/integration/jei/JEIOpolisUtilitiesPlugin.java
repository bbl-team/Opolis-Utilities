package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.ResourceGenerator2Recipe;
import com.benbenlaw.opolisutilities.recipe.ResourceGeneratorRecipe;
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
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()), ResourceGenerator2RecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new
                DryingTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                ResourceGeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                ResourceGenerator2RecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<DryingTableRecipe> recipes = rm.getAllRecipesFor(DryingTableRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(DryingTableRecipeCategory.UID, DryingTableRecipe.class), recipes);

        List<ResourceGeneratorRecipe> recipes2 = rm.getAllRecipesFor(ResourceGeneratorRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(ResourceGeneratorRecipeCategory.UID, ResourceGeneratorRecipe.class), recipes2);

        List<ResourceGenerator2Recipe> recipes3 = rm.getAllRecipesFor(ResourceGenerator2Recipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(ResourceGenerator2RecipeCategory.UID, ResourceGenerator2Recipe.class), recipes3);







    }
}
