package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.FluidGeneratorRecipe;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public class FluidGeneratorRecipeCategory implements IRecipeCategory<FluidGeneratorRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "fluid_generator");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_rg.png");

    static final RecipeType<FluidGeneratorRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "fluid_generator",
            FluidGeneratorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final int tabs = 1;
    private int tabs_used = 0;

    public FluidGeneratorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 57);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()));
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
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidGeneratorRecipe recipe, @NotNull IFocusGroup focusGroup) {

        tabs_used++;
        List<FluidGeneratorRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(FluidGeneratorRecipe.Type.INSTANCE);

        for (int i = 0; i < recipes.size(); i++) {
            final int slotX = 4 + (i % 9 * 19);
            final int slotY = 2 + i / 9 * 19;

            String fluidAsString = recipes.get(i).getFluid();
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidAsString));

            assert fluid != null;
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, slotX, slotY).addFluidStack(fluid, recipes.get(i).getFluidAmount())
                    .setFluidRenderer(recipes.get(i).getFluidAmount(), true, 16, 16);
        }
    }
}
