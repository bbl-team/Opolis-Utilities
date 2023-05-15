package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public class ShopRecipe extends SingleItemRecipe {

    public int ingredientCount;
    public ShopRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, int ingredientCount) {
        super(Type.INSTANCE, ModRecipes.SHOP_SERIALIZER.get(), pId, pGroup, pIngredient, pResult);
        this.ingredientCount = ingredientCount;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(Container pInv, Level pLevel) {
        return this.ingredient.test(pInv.getItem(0));
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.SHOP.get());
    }


    @Override
    public RecipeType<?> getType() {
        return ShopRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<ShopRecipe> {
        private Type() { }
        public static final ShopRecipe.Type INSTANCE = new ShopRecipe.Type();
        public static final String ID = "shop";
    }


    public static class Serializer implements RecipeSerializer<ShopRecipe> {
        public static final ShopRecipe.Serializer INSTANCE = new ShopRecipe.Serializer();

        public ShopRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            String group = GsonHelper.getAsString(pJson, "group", "");
            Ingredient ingredient;
            int ingredient_count = GsonHelper.getAsInt(pJson, "ingredient_count", 1);
            if (GsonHelper.isArrayNode(pJson, "ingredient")) {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(pJson, "ingredient"));
            } else {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pJson, "ingredient"));
            }

            String result = GsonHelper.getAsString(pJson, "result");
            int count = GsonHelper.getAsInt(pJson, "count", 1);
            ItemStack itemstack = new ItemStack(Registry.ITEM.get(new ResourceLocation(result)), count);
            return new ShopRecipe(pRecipeId, group, ingredient, itemstack, ingredient_count);
        }

        public ShopRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            String group = pBuffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            int ingredient_count = pBuffer.readInt();
            return new ShopRecipe(pRecipeId, group, ingredient, result, ingredient_count);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, ShopRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.group);
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeInt(pRecipe.ingredientCount);
        }
    }

}