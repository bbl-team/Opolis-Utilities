package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.recipe.RG2SpeedBlocksRecipe;
import com.benbenlaw.opolisutilities.recipe.UpgradeRecipeUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpgradeRecipeUtilCategory implements IRecipeCategory<UpgradeRecipeUtil> {

    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "upgrades");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_dynamic.png");

    public final static RecipeType<UpgradeRecipeUtil> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "upgrades",
            UpgradeRecipeUtil.class);

    private IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper helper;
    private int tabs_used = 0;


    public UpgradeRecipeUtilCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 114);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.UPGRADE_BASE.get()));
    }

    @Override
    public RecipeType<UpgradeRecipeUtil> getRecipeType() {
        return JEIOpolisUtilitiesPlugin.UPGRADE_RECIPE_UTIL;
    }

    @Override
    public boolean isHandled(UpgradeRecipeUtil recipe) {
        return tabs_used == 0;
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
        tabs_used++;

        List<UpgradeRecipeUtil> recipes = new ArrayList<>(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(UpgradeRecipeUtil.Type.INSTANCE));
        recipes.sort(Comparator.comparingDouble(UpgradeRecipeUtil::getDurationMultiplier));

        // Background Size
        int numRows = (int) Math.ceil((double) recipes.size() / 9);
        int numCols = Math.min(9, recipes.size()); // Maximum of 9 columns
        int backgroundWidth = 4 + numCols * 19;
        int backgroundHeight = 2 + numRows * 19;

        background = helper.createDrawable(TEXTURE, 0, 0, backgroundWidth, backgroundHeight);

        for (int i = 0; i < recipes.size(); i++) {
            final int slotX = 4 + (i % 9 * 19); // Calculate X position based on i % 9
            final int slotY = 2 + (i / 9 * 19); // Calculate Y position based on i / 9
            ItemStack stack = new ItemStack(recipes.get(i).getUpgradeItem().getItem());

            builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY)
                    .addItemStack(stack)
                    .addTooltipCallback(informationTooltip(recipe))
                    .setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, slotX - (i % 9 * 19) - 5, slotY - (2 + i / 9 * 19)  - 1);
        }
    }


    private IRecipeSlotTooltipCallback informationTooltip(UpgradeRecipeUtil recipe) {
        return (chance, addTooltip) -> {

            if (recipe.getDurationMultiplier() != 0.0 && recipe.getDurationMultiplier() < 1.0) {
                String string = ("Duration Modifier: x" + recipe.getDurationMultiplier() + (" (Faster)"));
                addTooltip.add(Component.literal(string).withStyle(ChatFormatting.GREEN));
            }

            if (recipe.getDurationMultiplier() != 0.0 && recipe.getDurationMultiplier() > 1.0) {
                String string = ("Duration Modifier: x" + recipe.getDurationMultiplier() + (" (Slower)"));
                addTooltip.add(Component.literal(string).withStyle(ChatFormatting.RED));
            }

            if (recipe.getDurationMultiplier() == 1.0) {
                String string = ("Duration Modifier: x" + recipe.getDurationMultiplier() + (" (Same)"));
                addTooltip.add(Component.literal(string).withStyle(ChatFormatting.WHITE));
            }

            if (recipe.getDurationSetAmount() != 0) {
                addTooltip.add(Component.literal("Duration Set Amount: " + recipe.getDurationSetAmount()));
            }


            int percentage = (int) (100 - (recipe.getOutputIncreaseChance() * 100));

            if (recipe.getOutputIncreaseChance() != 0.0 && percentage < 100) {
                String string = ("Additional Output Chance: " + percentage + ("%"));
                addTooltip.add(Component.literal(string).withStyle(ChatFormatting.GREEN));
            }

            if (recipe.getOutputIncreaseChance() != 0.0 && percentage > 100) {
                String string = ("Additional Output Chance: " + percentage + ("%"));
                addTooltip.add(Component.literal(string).withStyle(ChatFormatting.RED));
            }

            /*
            if (recipe.getOutputIncreaseChance() == 0.0) {
                String string = ("Additional Output Rolls: 0");
                addTooltip.add(Component.literal(string).withStyle(ChatFormatting.WHITE));
            }
             */

            if (recipe.getOutputIncreaseAmount() != 0) {
                addTooltip.add(Component.literal("Additional Output Rolls: " + recipe.getOutputIncreaseAmount()));
            }




            int percentageInputItem = (int) (recipe.getInputItemConsumeChance() * 100);

            if (recipe.getInputItemConsumeChance() != 1.0 && percentageInputItem < 100) {
                String string = ("Input Consume Chance: " + percentageInputItem + ("%"));
                addTooltip.add(Component.literal(string).withStyle(ChatFormatting.GREEN));
            }

            if (recipe.getInputItemConsumeChance() != 1.0 && percentageInputItem > 100) {
                String string = ("Input Consume Chance: " + percentageInputItem + ("%"));
                addTooltip.add(Component.literal(string).withStyle(ChatFormatting.RED));
            }

            /*
            if (recipe.getInputItemConsumeChance() == 1.0) {

                String string = ("Input Consume Chance: No Change");
                addTooltip.add(Component.literal(string).withStyle(ChatFormatting.WHITE));
            }
            */

            if (recipe.getInputItemExtraAmount() > 0) {
                addTooltip.add(Component.literal("Additional Consume Amount: " + recipe.getInputItemExtraAmount()).withStyle(ChatFormatting.RED));
            }

            if (recipe.getInputItemExtraAmount() == -1) {
                addTooltip.add(Component.literal("Additional Consume Amount: " + "Item Never Consumed!").withStyle(ChatFormatting.GREEN));
            }


            if (recipe.getRFPerTick() != 0.0) {
                addTooltip.add(Component.literal("RFPer Tick Multiplier: " + recipe.getRFPerTick() + ""));
            }

            if (recipe.getRfPerTickAmount() != 0) {
                addTooltip.add(Component.literal("RFPer Tick Amount: " + recipe.getRfPerTickAmount() + ""));
            }

            if (ModList.get().isLoaded("strainers")) {

                int percentageMeshDamage = (int) (recipe.getMeshUseChance() * 100);
                if (recipe.getMeshUseChance() != 0) {
                    addTooltip.add(Component.literal("Mesh Damage Chance: " + percentageMeshDamage + ("%")).withStyle(ChatFormatting.GREEN));
                }
                if (recipe.getMeshExtraDamage() > 0) {
                    addTooltip.add(Component.literal("Mesh Extra Damage: " + recipe.getMeshExtraDamage() + "").withStyle(ChatFormatting.RED));
                }
            }
        };
    }




}
