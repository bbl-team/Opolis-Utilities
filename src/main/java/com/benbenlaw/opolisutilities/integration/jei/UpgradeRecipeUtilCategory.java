package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.CatalogueRecipe;
import com.benbenlaw.opolisutilities.recipe.UpgradeRecipeUtil;
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
import net.minecraft.world.item.ItemStack;

public class UpgradeRecipeUtilCategory implements IRecipeCategory<UpgradeRecipeUtil> {

    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "upgrades");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_shop.png");

    static final RecipeType<CatalogueRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "upgrades",
            CatalogueRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public UpgradeRecipeUtilCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 70, 19);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CATALOGUE.get()));
    }

    @Override
    public RecipeType<UpgradeRecipeUtil> getRecipeType() {
        return JEIOpolisUtilitiesPlugin.UPGRADE_RECIPE_UTIL;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Upgrades");
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
    public void setRecipe(IRecipeLayoutBuilder builder, UpgradeRecipeUtil recipe, IFocusGroup focusGroup) {

        builder.addSlot(RecipeIngredientRole.INPUT, 4, 2).addItemStack(recipe.getUpgradeItem().setHoverName(Component.literal("Upgrade Item loaded")));

    }
}