package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.SpeedUpgradesRecipe;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SpeedUpgradesRecipeCategory implements IRecipeCategory<SpeedUpgradesRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "speed_upgrades");
    public final static ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/jei_dynamic.png");

    public static final RecipeType<SpeedUpgradesRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "speed_upgrades",
            SpeedUpgradesRecipe.class);

    private IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper helper;
    private int tabs_used = 0;

    public SpeedUpgradesRecipeCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 114);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RESOURCE_GENERATOR.get()));
    }

    @Override
    public RecipeType<SpeedUpgradesRecipe> getRecipeType() {
        return JEIOpolisUtilitiesPlugin.SPEED_UPGRADES;
    }

    @Override
    public boolean isHandled(SpeedUpgradesRecipe recipe) {
        return tabs_used == 0;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Speed Blocks");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(SpeedUpgradesRecipe recipe) {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(SpeedUpgradesRecipe.Type.INSTANCE).stream()
                .filter(recipeHolder -> recipeHolder.value().equals(recipe))
                .map(RecipeHolder::id)
                .findFirst()
                .orElse(null);
    }

    private final Map<Point, SpeedUpgradesRecipe> slotRecipes = new HashMap<>();
    private int backgroundWidth;


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SpeedUpgradesRecipe recipe, IFocusGroup focusGroup) {
        tabs_used++;

        List<SpeedUpgradesRecipe> recipes = new ArrayList<>(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(SpeedUpgradesRecipe.Type.INSTANCE)).stream().map(RecipeHolder::value).toList();

        List<SpeedUpgradesRecipe> mutableRecipes = new ArrayList<>(recipes);

        mutableRecipes.sort(Comparator.comparingInt(SpeedUpgradesRecipe::tickRate));

        // Background Size

        int yOffset = 20;

        int numRows = (int) Math.ceil((double) mutableRecipes.size() / 9);
        int numCols = Math.min(9, mutableRecipes.size()); // Maximum of 9 columns
        backgroundWidth = 4 + numCols * 19;
        int backgroundHeight = 2 + numRows * 19 + yOffset;

        background = helper.createDrawable(TEXTURE, 0, 0, backgroundWidth, backgroundHeight);

        slotRecipes.clear();

         // Shifts everything down by 20 pixels

        for (int i = 0; i < mutableRecipes.size(); i++) {
            final int slotX = 4 + (i % 9 * 19);
            final int slotY = yOffset + 2 + i / 9 * 19; // Add yOffset to the Y position

            // Create slots and track their positions along with the corresponding recipe
            builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY)
                    .addIngredients(mutableRecipes.get(i).input())
                    .setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, slotX - (i % 9 * 19) - 5, slotY - (2 + i / 9 * 19) - 1 - 20);

            builder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY)
                    .addIngredients(mutableRecipes.get(i).input());

            // Store the position of this slot with its corresponding recipe
            slotRecipes.put(new Point(slotX, slotY), mutableRecipes.get(i));
        }
    }

    @Override
    public void draw(SpeedUpgradesRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        String durationText = null;

        for (Map.Entry<Point, SpeedUpgradesRecipe> entry : slotRecipes.entrySet()) {
            Point position = entry.getKey();
            SpeedUpgradesRecipe hoveredRecipe = entry.getValue();

            int slotX = position.x;
            int slotY = position.y;
            int slotWidth = 18; // Standard slot size in pixels
            int slotHeight = 18;

            // Check if the mouse is within the bounds of the slot
            if (mouseX >= slotX && mouseX < slotX + slotWidth && mouseY >= slotY && mouseY < slotY + slotHeight) {
                // Get the duration of the hovered recipe and prepare the text
                int duration = hoveredRecipe.tickRate();
                durationText = "Duration: " + duration / 20 + "s / " + duration + " ticks";
                break; // Stop the loop once the hovered slot is found
            }
        }

        if (durationText != null) {
            int screenWidth = backgroundWidth; // Get the width of the GUI
            int textWidth = Minecraft.getInstance().font.width(durationText); // Get the width of the text
            int textX = (screenWidth - textWidth) / 2; // Calculate X to center the text
            int textY = 4;

            // Draw the text centered at the top
            guiGraphics.drawString(Minecraft.getInstance().font, durationText, textX, textY, 0xFFFFFFFF, true);
        }
    }

}


