package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.ModRecipes;
import com.benbenlaw.opolisutilities.recipe.ResourceGeneratorRecipe;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;

public class ResourceGeneratorRecipeCategory implements IRecipeCategory<ResourceGeneratorRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "resource_generator");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_drying_table.png");

    static final RecipeType<ResourceGeneratorRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "resource_generator",
            ResourceGeneratorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ResourceGeneratorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 120, 19);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RESOURCE_GENERATOR.get()));
    }

    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public RecipeType<ResourceGeneratorRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.RESOURCE_GENERATOR_SERIALIZER.getId(), ResourceGeneratorRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.literal("Resource Generator");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ResourceGeneratorRecipe recipe, IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 2).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 50, 2).addItemStack(recipe.getResultItem());

    }

    @Override
    public void draw(ResourceGeneratorRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();
        minecraft.font.draw(stack, Component.literal(String.valueOf("220s")), 95, 6, Color.gray.getRGB());
    }
}
