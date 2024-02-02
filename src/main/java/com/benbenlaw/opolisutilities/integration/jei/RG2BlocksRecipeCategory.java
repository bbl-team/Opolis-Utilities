package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.RG2BlocksRecipe;
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
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RG2BlocksRecipeCategory implements IRecipeCategory<RG2BlocksRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "rg2_blocks");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_dynamic.png");

    static final RecipeType<RG2BlocksRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "rg2_blocks",
            RG2BlocksRecipe.class);

    private IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper helper;
    private final int tabs = 1;
    private int tabs_used = 0;

    public RG2BlocksRecipeCategory(IGuiHelper helper) {
        this.helper = helper;
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

            String blockName = recipes.get(i).getBlock();
            Block rgBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
            TagKey<Item> itemTag = ItemTags.create(new ResourceLocation(blockName));

            if (rgBlock == Blocks.AIR ) {
                builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY).addIngredients(Ingredient.of(itemTag));
                builder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addIngredients(Ingredient.of(itemTag))
                        .setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, slotX - (i % 9 * 19) - 5, slotY - (2 + i / 9 * 19)  - 1);
            } else {
                assert rgBlock != null;
                builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY).addItemStack(new ItemStack(rgBlock.asItem()));
                builder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addItemStack(new ItemStack(rgBlock.asItem()))
                        .setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, slotX - (i % 9 * 19) - 5, slotY - (2 + i / 9 * 19)  - 1);
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

