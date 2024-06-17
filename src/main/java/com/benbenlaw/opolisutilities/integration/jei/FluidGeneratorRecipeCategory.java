package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.FluidGeneratorRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FluidGeneratorRecipeCategory implements IRecipeCategory<FluidGeneratorRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "fluid_generator");
    public final static ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/jei_dynamic.png");

    static final RecipeType<FluidGeneratorRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "fluid_generator",
            FluidGeneratorRecipe.class);

    private IDrawable background;
   // private final IDrawable icon;
    private final IGuiHelper helper;
    private int tabs_used = 0;

    public FluidGeneratorRecipeCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 57);
   //     this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()));
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
        return null; /* this.icon; */
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidGeneratorRecipe recipe, @NotNull IFocusGroup focusGroup) {

        tabs_used++;

        /*

     //   List<FluidGeneratorRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(FluidGeneratorRecipe.Type.INSTANCE);

        // Calculate the number of rows and columns based on the number of recipes
        int numRows = (int) Math.ceil((double) recipes.size() / 9);
        int numCols = Math.min(9, recipes.size()); // Maximum of 9 columns

        // Calculate the background size based on the number of rows and columns
        int backgroundWidth = 4 + numCols * 19;
        int backgroundHeight = 2 + numRows * 19;

        // Set the background size

        background = helper.createDrawable(TEXTURE, 0, 0, backgroundWidth, backgroundHeight);
        for (int i = 0; i < recipes.size(); i++) {
            final int slotX = 4 + (i % 9 * 19);
            final int slotY = 2 + i / 9 * 19;

            String fluidAsString = recipes.get(i).getFluid();
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.fromNamespaceAndPath(fluidAsString));

            assert fluid != null;
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, slotX, slotY).addFluidStack(fluid, recipes.get(i).getFluidAmount())
                    .setFluidRenderer(recipes.get(i).getFluidAmount(), true, 16, 16);
        }

         */
    }
}
