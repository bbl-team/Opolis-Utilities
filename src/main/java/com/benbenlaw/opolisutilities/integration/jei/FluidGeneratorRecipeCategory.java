package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.FluidGeneratorRecipe;
import com.benbenlaw.opolisutilities.recipe.SpeedUpgradesRecipe;
import com.benbenlaw.opolisutilities.recipe.SummoningBlockRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidGeneratorRecipeCategory implements IRecipeCategory<FluidGeneratorRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "fluid_generator");
    public final static ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/jei_dynamic.png");

    static final RecipeType<FluidGeneratorRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "fluid_generator",
            FluidGeneratorRecipe.class);

    private IDrawable background;
   // private final IDrawable icon;

    private final IDrawable icon;

    @Override
    public @Nullable ResourceLocation getRegistryName(FluidGeneratorRecipe recipe) {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(FluidGeneratorRecipe.Type.INSTANCE).stream()
                .filter(recipeHolder -> recipeHolder.value().equals(recipe))
                .map(RecipeHolder::id)
                .findFirst()
                .orElse(null);
    }
    private final IGuiHelper helper;
    private int tabs_used = 0;

    public FluidGeneratorRecipeCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 57);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FLUID_GENERATOR.get()));
    }

    @Override
    public @NotNull RecipeType<FluidGeneratorRecipe> getRecipeType() {
        return JEIOpolisUtilitiesPlugin.FLUID_GENERATOR;
    }

    @Override
    public boolean isHandled(FluidGeneratorRecipe recipe) {
        return tabs_used == 0;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Fluid Generator");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    private final Map<Point, FluidGeneratorRecipe> slotRecipes = new HashMap<>();

    private int backgroundWidth;

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidGeneratorRecipe recipe, @NotNull IFocusGroup focusGroup) {
        tabs_used++;

        List<RecipeHolder<FluidGeneratorRecipe>> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(FluidGeneratorRecipe.Type.INSTANCE);

        // Calculate the number of rows and columns based on the number of recipes

        int yOffset = 20;

        int numRows = (int) Math.ceil((double) recipes.size() / 9);
        int numCols = 9;//Math.min(9, recipes.size()); // Maximum of 9 columns

        // Calculate the background size based on the number of rows and columns
        backgroundWidth = 4 + numCols * 19;
        int backgroundHeight = 2 + numRows * 19 + yOffset;

        // Set the background size
        background = helper.createDrawable(TEXTURE, 0, 0, backgroundWidth, backgroundHeight);
        slotRecipes.clear();

        for (int i = 0; i < recipes.size(); i++) {
            final int slotX = 4 + (i % 9 * 19);
            final int slotY =  yOffset + 2 + i / 9 * 19;

            int finalI = i;
            builder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY)
                    .addFluidStack(recipes.get(i).value().input().getFluid(), 1000);

            // Track positions and recipes for drawing
            slotRecipes.put(new Point(slotX, slotY), recipes.get(i).value());
        }
    }

    @Override
    public void draw(FluidGeneratorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        String infoText = null;

        for (Map.Entry<Point, FluidGeneratorRecipe> entry : slotRecipes.entrySet()) {
            Point position = entry.getKey();
            FluidGeneratorRecipe hoveredRecipe = entry.getValue();

            int slotX = position.x;
            int slotY = position.y;
            int slotWidth = 18; // Standard slot size in pixels
            int slotHeight = 18;

            // Check if the mouse is within the bounds of the slot
            if (mouseX >= slotX && mouseX < slotX + slotWidth && mouseY >= slotY && mouseY < slotY + slotHeight) {
                // Get the amount of fluid generated and prepare the text
                int amount = hoveredRecipe.input().getAmount();
                infoText = "Fluid Generated: " + amount + "mB";
                break; // Stop the loop once the hovered slot is found
            }
        }

        if (infoText != null) {
            int screenWidth = backgroundWidth; // Get the width of the GUI
            int textWidth = Minecraft.getInstance().font.width(infoText); // Get the width of the text
            int textX = (screenWidth - textWidth) / 2; // Calculate X to center the text
            int textY = 4;

            // Draw the text centered at the top
            guiGraphics.drawString(Minecraft.getInstance().font, infoText, textX, textY, 0xFFFFFFFF, true);
        }
    }



}
