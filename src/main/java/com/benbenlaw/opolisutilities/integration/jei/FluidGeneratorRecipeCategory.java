package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.FluidGeneratorRecipe;
import com.benbenlaw.opolisutilities.recipe.ModRecipes;
import com.benbenlaw.opolisutilities.recipe.RG2BlocksRecipe;
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
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

public class FluidGeneratorRecipeCategory implements IRecipeCategory<FluidGeneratorRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "fluid_generator");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_resource_generator_2.png");

    static final RecipeType<FluidGeneratorRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "fluid_generator",
            FluidGeneratorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FluidGeneratorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()));
    }

    @Override
    public @NotNull RecipeType<FluidGeneratorRecipe> getRecipeType() {
        return JEIOpolisUtilitiesPlugin.FLUID_GENERATOR;
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
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidGeneratorRecipe recipe, @NotNull IFocusGroup focusGroup) {

        String fluidAsString = recipe.getFluid();
        @Deprecated
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidAsString));

        builder.addSlot(RecipeIngredientRole.INPUT, 140, 5).addItemStack(
                new ItemStack(Items.SUGAR.asItem()).setHoverName(Component.literal("Speed blocks can be placed on top to increase speed!")));



            assert fluid != null;
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 24).addFluidStack(fluid, recipe.getFluidAmount()).setFluidRenderer(recipe.getFluidAmount(), true, 16,16);

        builder.addSlot(RecipeIngredientRole.INPUT, 140, 43).addItemStack(new ItemStack(ModBlocks.FLUID_GENERATOR.get()));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 62).addItemStack(new ItemStack(Blocks.GLASS).setHoverName(Component.literal("Tank")));

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addFluidStack(fluid, recipe.getFluidAmount());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addFluidStack(fluid, recipe.getFluidAmount());

    }

    @Override
    public void draw(FluidGeneratorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei.recipes.resource_generator_2_line_1"), 5, 7, Color.WHITE.getRGB());
        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei.recipes.resource_generator_2_line_2"), 5, 15,  Color.WHITE.getRGB());
        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei.recipes.resource_generator_2_tank_1"), 5, 60,  Color.WHITE.getRGB());
        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei.recipes.resource_generator_2_tank_2"), 5, 68,  Color.WHITE.getRGB());

    }


}

