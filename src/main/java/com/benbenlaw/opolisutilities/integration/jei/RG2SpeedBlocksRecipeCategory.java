package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.ModRecipes;
import com.benbenlaw.opolisutilities.recipe.RG2SpeedBlocksRecipe;
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
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import java.awt.*;

public class RG2SpeedBlocksRecipeCategory implements IRecipeCategory<RG2SpeedBlocksRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "rg2_speed_blocks");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_resource_generator_2.png");

    static final RecipeType<RG2SpeedBlocksRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "rg2_speed_blocks",
            RG2SpeedBlocksRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public RG2SpeedBlocksRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()));
    }

    @Override
    public RecipeType<RG2SpeedBlocksRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.RG2_SPEED_BLOCKS_SERIALIZER.getId(), RG2SpeedBlocksRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.literal("Speed Blocks");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RG2SpeedBlocksRecipe recipe, IFocusGroup focusGroup) {

        String blockName = recipe.getBlock();
        Block rgBlock = Registry.BLOCK.get(new ResourceLocation(blockName));
        TagKey<Item> itemTag = ItemTags.create(new ResourceLocation(blockName));

        if (rgBlock == Blocks.AIR) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 5).addIngredients(Ingredient.of(itemTag));
        } else {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 5).addItemStack(new ItemStack(rgBlock.asItem()));

        }

        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(new ItemStack(rgBlock.asItem()));
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(itemTag));

    }

    @Override
    public void draw(RG2SpeedBlocksRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {

        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_speed_blocks_line_1"), 5, 7, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_speed_blocks_line_2"), 5, 15, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_speed_blocks_line_3"), 5, 23, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_speed_blocks_line_4"), 5, 31, Color.black.getRGB());


        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_speed_blocks_line"), 5, 60, Color.black.getRGB());
        minecraft.font.draw(stack, Component.literal(recipe.getTickRate() + ""), 5, 68, Color.black.getRGB());


    }
}
