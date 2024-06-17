package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.recipe.CatalogueRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CatalogueRecipeCategory implements IRecipeCategory<CatalogueRecipe> {

    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "catalogue");
    public final static ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/jei_shop.png");

    static final RecipeType<CatalogueRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "catalogue",
            CatalogueRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public CatalogueRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 70, 19);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CATALOGUE.get()));
    }

    @Override
    public RecipeType<CatalogueRecipe> getRecipeType() {
        return JEIOpolisUtilitiesPlugin.CATALOGUE_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Catalogue");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CatalogueRecipe recipe, IFocusGroup focusGroup) {

        builder.addSlot(RecipeIngredientRole.INPUT, 4, 2).addIngredients(recipe.input().ingredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 51, 2).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));

    }
}