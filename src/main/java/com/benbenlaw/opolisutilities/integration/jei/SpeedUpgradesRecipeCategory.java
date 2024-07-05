package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.SpeedUpgradesRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public @NotNull RecipeType<SpeedUpgradesRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, SpeedUpgradesRecipe recipe, IFocusGroup focusGroup) {
        tabs_used++;

        List<SpeedUpgradesRecipe> recipes = new ArrayList<>(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(SpeedUpgradesRecipe.Type.INSTANCE)).stream().map(RecipeHolder::value).toList();

        List<SpeedUpgradesRecipe> mutableRecipes = new ArrayList<>(recipes);

        mutableRecipes.sort(Comparator.comparingInt(SpeedUpgradesRecipe::tickRate));

        // Background Size
        int numRows = (int) Math.ceil((double) mutableRecipes.size() / 9);
        int numCols = Math.min(9, mutableRecipes.size()); // Maximum of 9 columns
        int backgroundWidth = 4 + numCols * 19;
        int backgroundHeight = 2 + numRows * 19;

        background = helper.createDrawable(TEXTURE, 0, 0, backgroundWidth, backgroundHeight);

        for (int i = 0; i < mutableRecipes.size(); i++) {
            final int slotX = 4 + (i % 9 * 19);
            final int slotY = 2 + i / 9 * 19;

            int duration = mutableRecipes.get(i).tickRate();

            builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY).addIngredients(mutableRecipes.get(i).input()).addTooltipCallback(durationTime(duration));
            builder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addIngredients(mutableRecipes.get(i).input()).addTooltipCallback(durationTime(duration))
                    .setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, slotX - (i % 9 * 19) - 5, slotY - (2 + i / 9 * 19) - 1);

        }
    }

    @Contract(pure = true)
    private @NotNull IRecipeSlotTooltipCallback durationTime(int duration) {
        return (chance, addTooltip) -> {
            addTooltip.add(Component.literal(duration / 20 + "s / " + duration + " ticks"));
        };
    }
}



