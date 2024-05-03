package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.recipe.SoakingTableRecipe;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

public class SoakingTableRecipeCategory implements IRecipeCategory<SoakingTableRecipe>{

        public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "soaking_table");
        public final static ResourceLocation TEXTURE =
                new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_drying_table.png");

        static final RecipeType<SoakingTableRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "soaking_table",
                SoakingTableRecipe.class);

        private final IDrawable background;
        private final IDrawable icon;

        public SoakingTableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 120, 19);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.DRYING_TABLE.get()));
    }

        public ResourceLocation getUid () {
        return UID;
    }

        @Override
        public RecipeType<SoakingTableRecipe> getRecipeType () {
            return JEIOpolisUtilitiesPlugin.SOAKING_TABLE;
    }

        @Override
        public Component getTitle () {
        return Component.literal("Soaking Table");
    }

        @Override
        public IDrawable getBackground () {
        return this.background;
    }

        @Override
        public IDrawable getIcon () {
        return this.icon;
    }

        @Override
        public void setRecipe (IRecipeLayoutBuilder builder, SoakingTableRecipe recipe, IFocusGroup focusGroup){

            builder.addSlot(RecipeIngredientRole.INPUT, 4, 2).addIngredients(recipe.getIngredients().get(0));
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 4, 2).addItemStack(new ItemStack(ModItems.JEI_NULL_ITEM.get(), recipe.getCount()));

            assert false;
            builder.addSlot(RecipeIngredientRole.OUTPUT, 50, 2).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }



    @Override
    public void draw(SoakingTableRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        int duration = recipe.getDuration();

        guiGraphics.drawString(minecraft.font.self(), Component.literal(duration / 20 + "s"), 95, 6, Color.WHITE.getRGB());
    }
}

