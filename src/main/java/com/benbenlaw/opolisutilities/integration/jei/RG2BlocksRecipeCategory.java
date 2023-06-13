package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.ModRecipes;
import com.benbenlaw.opolisutilities.recipe.RG2BlocksRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class RG2BlocksRecipeCategory implements IRecipeCategory<RG2BlocksRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "rg2_blocks");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_resource_generator_2.png");

    static final RecipeType<RG2BlocksRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "rg2_blocks",
            RG2BlocksRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public RG2BlocksRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()));
    }

    @Override
    public @NotNull RecipeType<RG2BlocksRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.RG2_BLOCKS_SERIALIZER.getId(), RG2BlocksRecipe.class);
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

        String blockName = recipe.getBlock();
        @Deprecated
        Block rgBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
        TagKey<Item> itemTag = ItemTags.create(new ResourceLocation(blockName));

        builder.addSlot(RecipeIngredientRole.INPUT, 140, 5).addItemStack(
                new ItemStack(Items.SUGAR.asItem()).setHoverName(Component.literal("Speed blocks can be placed on top to increase speed!")));

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

    /*

    @Override
    public void draw(@NotNull RG2BlocksRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {

        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_line_1"), 5, 7, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_line_2"), 5, 15, Color.black.getRGB());


        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_chest_1"), 5, 60, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_chest_2"), 5, 68, Color.black.getRGB());


    }
    */
}

