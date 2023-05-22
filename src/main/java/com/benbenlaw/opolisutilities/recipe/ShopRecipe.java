package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ShopRecipe extends SingleItemRecipe {

    public ShopRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, int ingredientCount) {
        super(Type.INSTANCE, ModRecipes.SHOP_SERIALIZER.get(), pId, pGroup, pIngredient, pResult);
        this.ingredientCount = ingredientCount;
    }

    public int ingredientCount;


    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return this.ingredient.test(pContainer.getItem(0));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ShopRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return ShopRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<ShopRecipe> {
        private Type() { }
        public static final Type INSTANCE = new ShopRecipe.Type();
        public static final String ID = "shop";
    }

    public static class Serializer implements RecipeSerializer<ShopRecipe> {
        public static final ShopRecipe.Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(OpolisUtilities.MOD_ID,"shop");

        @Override
        public @NotNull ShopRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
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

        @Override
        public ShopRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {

            String group = pBuffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            int ingredient_count = pBuffer.readInt();

            return new ShopRecipe(pRecipeId, group, ingredient, result, ingredient_count);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ShopRecipe pRecipe) {

            pBuffer.writeUtf(pRecipe.group);
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeInt(pRecipe.ingredientCount);
        }
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    @SuppressWarnings("unchecked") // Need this wrapper, because generics
    private static <G> Class<G> castClass(Class<?> cls) {
        return (Class<G>) cls;
    }
}