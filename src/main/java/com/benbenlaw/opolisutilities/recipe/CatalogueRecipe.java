package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class CatalogueRecipe extends SingleItemRecipe {

    public int itemInCount;
    public CatalogueRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, int itemInCount) {
        super(Type.INSTANCE, ModRecipes.CATALOGUE_SERIALIZER.get(), pId, pGroup, pIngredient, pResult);
        this.itemInCount = itemInCount;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CatalogueRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CatalogueRecipe> {
        private Type() { }
        public static final CatalogueRecipe.Type INSTANCE = new CatalogueRecipe.Type();
        public static final String ID = "catalogue";
    }

    public static class Serializer implements RecipeSerializer<CatalogueRecipe> {
        public static final CatalogueRecipe.Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(OpolisUtilities.MOD_ID,"catalogue");

        @Override
        public @NotNull CatalogueRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            String group = GsonHelper.getAsString(pJson, "group", "");

            Ingredient ingredient;
            int itemInCount = GsonHelper.getAsInt(pJson, "itemInCount", 1);
            if (GsonHelper.isArrayNode(pJson, "ingredient")) {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(pJson, "ingredient"));
            } else {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pJson, "ingredient"));
            }

            String result = GsonHelper.getAsString(pJson, "result");
            int count = GsonHelper.getAsInt(pJson, "itemOutCount", 1);
            ItemStack itemstack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(result))), count);

            return new CatalogueRecipe(pRecipeId, group, ingredient, itemstack, itemInCount);
        }

        @Override
        public CatalogueRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {

            String group = pBuffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            int itemInCount = pBuffer.readInt();

            return new CatalogueRecipe(pRecipeId, group, ingredient, result, itemInCount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CatalogueRecipe pRecipe) {

            pBuffer.writeUtf(pRecipe.group);
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeInt(pRecipe.itemInCount);
        }
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public boolean matches(Container p_44483_, Level p_44484_) {
        return this.ingredient.test(p_44483_.getItem(0));
    }
}