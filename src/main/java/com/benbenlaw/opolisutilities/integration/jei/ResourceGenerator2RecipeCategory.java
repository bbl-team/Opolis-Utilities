package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.ModRecipes;
import com.benbenlaw.opolisutilities.recipe.ResourceGenerator2Recipe;
import com.benbenlaw.opolisutilities.recipe.ResourceGeneratorRecipe;
import com.benbenlaw.opolisutilities.util.ModTags;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import java.awt.*;

public class ResourceGenerator2RecipeCategory implements IRecipeCategory<ResourceGenerator2Recipe> {
    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "resource_generator_2");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_resource_generator_2.png");

    static final RecipeType<ResourceGenerator2Recipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "resource_generator_2",
            ResourceGenerator2Recipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ResourceGenerator2RecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()));
    }

    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public RecipeType<ResourceGenerator2Recipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.RESOURCE_GENERATOR_2_SERIALIZER.getId(), ResourceGenerator2Recipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.literal("Resource Generator 2");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ResourceGenerator2Recipe recipe, IFocusGroup focusGroup) {

        builder.addSlot(RecipeIngredientRole.CATALYST, 140, 5).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 24).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 140, 43).addItemStack(new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 62).addItemStack(new ItemStack(Blocks.CHEST));

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(recipe.getIngredients().get(0));
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(recipe.getIngredients().get(0));

    }

    @Override
    public void draw(ResourceGenerator2Recipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {

        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_line_1"), 5, 7, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_line_2"), 5, 15, Color.black.getRGB());


        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_chest_1"), 5, 60, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_chest_2"), 5, 68, Color.black.getRGB());


    }
}
