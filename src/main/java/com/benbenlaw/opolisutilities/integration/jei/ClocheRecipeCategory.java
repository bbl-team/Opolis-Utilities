package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.ClocheRecipe;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.SummoningBlockRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;

public class ClocheRecipeCategory implements IRecipeCategory<ClocheRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche");
    public final static ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/jei_cloche.png");

    static final RecipeType<ClocheRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "cloche",
            ClocheRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    @Override
    public @Nullable ResourceLocation getRegistryName(ClocheRecipe recipe) {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ClocheRecipe.Type.INSTANCE).stream()
                .filter(recipeHolder -> recipeHolder.value().equals(recipe))
                .map(RecipeHolder::id)
                .findFirst()
                .orElse(null);
    }

    public ClocheRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 140, 55);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CLOCHE.get()));
    }

    @Override
    public RecipeType<ClocheRecipe> getRecipeType() {
        return JEIOpolisUtilitiesPlugin.CLOCHE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Cloche");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ClocheRecipe recipe, @NotNull IFocusGroup focusGroup) {

        builder.addSlot(RecipeIngredientRole.INPUT, 4, 2).addIngredients(recipe.seed());
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 20).addIngredients(recipe.soil());

        if (!recipe.catalyst().hasNoItems()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 4, 38).addIngredients(recipe.catalyst())
                    .setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, -1, -1);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 67, 20).addItemStack(new ItemStack(recipe.mainOutput().getItem(), recipe.mainOutput().getCount()));

        if (recipe.chanceOutput1().getItem() != Items.AIR) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 20).addItemStack(new ItemStack(recipe.chanceOutput1().getItem(), recipe.chanceOutput1().getCount()))
                    .setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, -1, -1);
        }

        if (recipe.chanceOutput2().getItem() != Items.AIR) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 20).addItemStack(new ItemStack(recipe.chanceOutput2().getItem(), recipe.chanceOutput2().getCount()))
                    .setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, -1, -1);
        }

        if (recipe.chanceOutput3().getItem() != Items.AIR) {

            builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 20).addItemStack(new ItemStack(recipe.chanceOutput3().getItem(), recipe.chanceOutput3().getCount()))
                    .addTooltipCallback(new OpolisIRecipeSlotTooltipCallback() {
                        @Override
                        public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltipBuilder) {
                            tooltipBuilder.add(Component.literal("Chance: " + (recipe.chanceOutputChance3() * 100) + "%"));
                        }
                    }).setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, -1, -1);
        }
    }

    @Override
    public void draw(ClocheRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        final Minecraft minecraft = Minecraft.getInstance();

        int[][] slotAreas = {
                {67, 20, 16, 16},
                {85, 20, 16, 16},
                {103, 20, 16, 16},
                {121, 20, 16, 16}
        };

        double[] chance = {
                1.0,
                recipe.chanceOutputChance1(),
                recipe.chanceOutputChance2(),
                recipe.chanceOutputChance3()
        };

        for (int i = 0; i < slotAreas.length; i++) {
            int[] area = slotAreas[i];
            int slotX = area[0];
            int slotY = area[1];
            int slotWidth = area[2];
            int slotHeight = area[3];

            // Default "Chance: " text without the percentage
            String chanceText = "Chance: ";

            // Add the percentage only if the mouse is hovering over the slot and the chance is greater than 0.0
            if (mouseX >= slotX && mouseX < slotX + slotWidth && mouseY >= slotY && mouseY < slotY + slotHeight) {
                if (chance[i] > 0.0) {
                    chanceText += (chance[i] * 100) + "%";
                }
            }

            // Render the chance text
            guiGraphics.drawString(minecraft.font.self(), Component.literal(chanceText), 30, 2, Color.WHITE.getRGB());
        }


        int finalDuration = (int) (220 * recipe.durationModifier());
        guiGraphics.drawString(minecraft.font.self(), Component.literal(String.valueOf(finalDuration)), 38, 40, Color.WHITE.getRGB());

    }
}
