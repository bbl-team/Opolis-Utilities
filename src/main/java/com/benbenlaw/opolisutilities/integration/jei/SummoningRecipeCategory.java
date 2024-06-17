package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.SummoningBlockRecipe;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SummoningRecipeCategory implements IRecipeCategory<SummoningBlockRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "summoning_block");
    public final static ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/jei_summoning_block.png");

    static final RecipeType<SummoningBlockRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "summoning_block",
            SummoningBlockRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public SummoningRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 103, 19);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SUMMONING_BLOCK.get()));
    }

    @Override
    public RecipeType<SummoningBlockRecipe> getRecipeType() {
        return JEIOpolisUtilitiesPlugin.SUMMOMING_BLOCK;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Summoning");
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
    public void setRecipe(IRecipeLayoutBuilder builder, SummoningBlockRecipe recipe, @NotNull IFocusGroup focusGroup) {

        builder.addSlot(RecipeIngredientRole.INPUT, 40, 2).addIngredients(recipe.input().ingredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 4, 2).addIngredients(recipe.catalyst());
        @Nullable EntityType<?> entity = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(recipe.mob()));
        ItemStack spawnEgg = Objects.requireNonNull(SpawnEggItem.byId(entity)).getDefaultInstance();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 84, 2).addItemStack(new ItemStack(spawnEgg.getItem()));
    }
}
