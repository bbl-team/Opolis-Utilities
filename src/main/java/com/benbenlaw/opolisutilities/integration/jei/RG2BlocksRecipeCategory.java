package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.ModRecipes;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.RG2BlocksRecipe;
import com.benbenlaw.opolisutilities.recipe.RG2SpeedBlocksRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RG2BlocksRecipeCategory implements IRecipeCategory<RG2BlocksRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "rg2_blocks");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_rg.png");

    static final RecipeType<RG2BlocksRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "rg2_blocks",
            RG2BlocksRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final int tabs = 1;
    private int tabs_used = 0;

    public RG2BlocksRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 57);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()));
    }

    @Override
    public @NotNull RecipeType<RG2BlocksRecipe> getRecipeType() {
        return JEIOpolisUtilitiesPlugin.RG2_BLOCKS;
    }

    @Override
    public boolean isHandled(RG2BlocksRecipe recipe) {
        return tabs_used == 0;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Resource Generator 2");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RG2BlocksRecipe recipe, @NotNull IFocusGroup focusGroup) {
        tabs_used++;

        List<RG2BlocksRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RG2BlocksRecipe.Type.INSTANCE);

        for (int i = 0; i < recipes.size(); i++) {
            final int slotX = 4 + (i % 9 * 19);
            final int slotY = 2 + i / 9 * 19;

            String blockName = recipes.get(i).getBlock();
            Block rgBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
            TagKey<Item> itemTag = ItemTags.create(new ResourceLocation(blockName));

            if (rgBlock == Blocks.AIR ) {
                builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY).addIngredients(Ingredient.of(itemTag));
                builder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addIngredients(Ingredient.of(itemTag));
            } else {
                assert rgBlock != null;
                builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY).addItemStack(new ItemStack(rgBlock.asItem()));
                builder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addItemStack(new ItemStack(rgBlock.asItem()));
            }
        }
    }
        //OLD SET RECIPE METHOD
    /*
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RG2BlocksRecipe recipe, @NotNull IFocusGroup focusGroup) {

        String blockName = recipe.getBlock();
        Block rgBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
        TagKey<Item> itemTag = ItemTags.create(new ResourceLocation(blockName));

        Collection<RG2SpeedBlocksRecipe> rg2SpeedRecipes = Minecraft.getInstance().getConnection().getRecipeManager()
                .getRecipesFor(RG2SpeedBlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, Minecraft.getInstance().level);

        int slotX = 140; // X-coordinate for the slot
        int slotY = 5;   // Y-coordinate for the slot

        IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.CATALYST, slotX, slotY);

        for (RG2SpeedBlocksRecipe rg2SpeedRecipe : rg2SpeedRecipes) {
            String speedBlockName = rg2SpeedRecipe.getBlock();
            Block speedBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(speedBlockName));
            TagKey<Item> speedBlockTag = ItemTags.create(new ResourceLocation(speedBlockName));

            if (speedBlock == Blocks.AIR) {
                inputSlot.addIngredients(Ingredient.of(speedBlockTag));

            } else {
                inputSlot.addItemStack(new ItemStack(speedBlock.asItem()));
            }
            slotY += 20;
        }

        if (rgBlock == Blocks.AIR) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 24).addIngredients(Ingredient.of(itemTag));
        }

        else {
            assert rgBlock != null;
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 24).addItemStack(new ItemStack(rgBlock.asItem()));

        }

        builder.addSlot(RecipeIngredientRole.INPUT, 140, 43).addItemStack(new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 62).addItemStack(new ItemStack(Blocks.CHEST));

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(rgBlock.asItem()));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(Ingredient.of(itemTag));
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(new ItemStack(rgBlock.asItem()));
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(itemTag));

    }

    @Override
    public void draw(RG2BlocksRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei.recipes.resource_generator_2_line_1"), 5, 7, Color.WHITE.getRGB());
        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei.recipes.resource_generator_2_line_2"), 5, 15,  Color.WHITE.getRGB());
        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei.recipes.resource_generator_2_chest_1"), 5, 60,  Color.WHITE.getRGB());
        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei.recipes.resource_generator_2_chest_2"), 5, 68,  Color.WHITE.getRGB());

    }

     */
}

