package com.benbenlaw.opolisutilities.integration;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.ModRecipes;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class DryingTableRecipeCategory implements IRecipeCategory<DryingTableRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "drying_table");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_drying_table.png");

    static final RecipeType<DryingTableRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "drying_table",
            DryingTableRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public DryingTableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.DRYING_TABLE.get()));
    }

    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public RecipeType<DryingTableRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.DRYING_TABLE_SERIALIZER.getId(), DryingTableRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.literal("Drying Table");
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
    public void setRecipe(IRecipeLayoutBuilder builder, DryingTableRecipe recipe, IFocusGroup focusGroup) {
       // builder.addSlot(RecipeIngredientRole.INPUT, 34, 40).addIngredients(Ingredient.of((Items.POTION).getDefaultInstance()));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 12).addIngredients(recipe.getIngredients().get(0));
       // builder.addSlot(RecipeIngredientRole.INPUT, 103, 18).addIngredients(Ingredient.of(ModItems.COPPER_NUGGET.get()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 54).addItemStack(recipe.getResultItem());
    }
}
