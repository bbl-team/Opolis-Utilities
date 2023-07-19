package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.CatalogueRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CatalogueRecipeCategory implements IRecipeCategory<CatalogueRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "catalogue");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_shop.png");

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
        /*
        //    builder.addSlot(RecipeIngredientRole.INPUT, 26, 31).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 2).addItemStack(
                new ItemStack(recipe.getIngredients().get(0).getItems()[0].getItem(), recipe.itemInCount));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 51, 2).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));

    }
    /*

    @Override
    public void draw(CatalogueRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        //int duration = recipe.getDuration();

        //minecraft.font.draw(stack, Component.literal(String.valueOf(duration) + " ticks"), 92, 37, Color.black.getRGB());


*/

    }


}