package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.RG2SpeedBlocksRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RG2SpeedBlocksRecipeCategory implements IRecipeCategory<RG2SpeedBlocksRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(OpolisUtilities.MOD_ID, "rg2_speed_blocks");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/jei_dynamic.png");

    static final RecipeType<RG2SpeedBlocksRecipe> RECIPE_TYPE = RecipeType.create(OpolisUtilities.MOD_ID, "rg2_speed_blocks",
            RG2SpeedBlocksRecipe.class);

    private IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper helper;
    private int tabs_used = 0;

    public RG2SpeedBlocksRecipeCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 114);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()));
    }

    @Override
    public @NotNull RecipeType<RG2SpeedBlocksRecipe> getRecipeType() {
        return JEIOpolisUtilitiesPlugin.RG2_SPEED_BLOCKS;
    }

    @Override
    public boolean isHandled(RG2SpeedBlocksRecipe recipe) {
        return tabs_used == 0;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Speed Blocks");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RG2SpeedBlocksRecipe recipe, IFocusGroup focusGroup) {
        tabs_used++;

        List<RG2SpeedBlocksRecipe> recipes = new ArrayList<>(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RG2SpeedBlocksRecipe.Type.INSTANCE));

        // Sort the recipes by duration
        Collections.sort(recipes, Comparator.comparingInt(RG2SpeedBlocksRecipe::getTickRate));

        // Background Size
        int numRows = (int) Math.ceil((double) recipes.size() / 9);
        int numCols = Math.min(9, recipes.size()); // Maximum of 9 columns
        int backgroundWidth = 4 + numCols * 19;
        int backgroundHeight = 2 + numRows * 19;

        background = helper.createDrawable(TEXTURE, 0, 0, backgroundWidth, backgroundHeight);

        for (int i = 0; i < recipes.size(); i++) {
            final int slotX = 4 + (i % 9 * 19);
            final int slotY = 2 + i / 9 * 19;

            String blockName = recipes.get(i).getBlock();
            Block rgBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
            TagKey<Item> itemTag = ItemTags.create(new ResourceLocation(blockName));

            int duration = recipes.get(i).getTickRate();

            if (rgBlock == Blocks.AIR ) {
                builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY).addIngredients(Ingredient.of(itemTag)).addTooltipCallback(durationTime(duration));
                builder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addIngredients(Ingredient.of(itemTag)).addTooltipCallback(durationTime(duration))

                        .setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, slotX - (i % 9 * 19) - 5, slotY - (2 + i / 9 * 19)  - 1);
            } else {
                assert rgBlock != null;
                builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY).addItemStack(new ItemStack(rgBlock.asItem())).addTooltipCallback(durationTime(duration));
                builder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addItemStack(new ItemStack(rgBlock.asItem())).addTooltipCallback(durationTime(duration))

                        .setBackground(JEIOpolisUtilitiesPlugin.slotDrawable, slotX - (i % 9 * 19) - 5, slotY - (2 + i / 9 * 19)  - 1);
            }
        }
    }



    @Contract(pure = true)
    private @NotNull IRecipeSlotTooltipCallback durationTime(int duration) {
        return (chance, addTooltip) -> {
            addTooltip.add(Component.literal(duration / 20 + "s / " + duration + " ticks"));
        };
    }

        /*

        //Old Method
        String blockName = recipe.getBlock();
        Block rgBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
        TagKey<Item> itemTag = ItemTags.create(new ResourceLocation(blockName));

        if (rgBlock == Blocks.AIR) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 4, 2).addIngredients(Ingredient.of(itemTag));
        } else {
            assert rgBlock != null;
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 4, 2).addItemStack(new ItemStack(rgBlock.asItem()));

        }

        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(new ItemStack(rgBlock.asItem()));
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(itemTag));
    }

     */

    /*

    @Override
    public void draw(RG2SpeedBlocksRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        int duration = recipe.getTickRate();

        guiGraphics.drawString(minecraft.font.self(), Component.literal(duration / 20 + "s / " + duration + " ticks"), 40, 6, Color.WHITE.getRGB());

    }

     */
}


