package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public class DryingTableRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient recipeInput;
    private final int count;
    private final int duration;

    public DryingTableRecipe(ResourceLocation id, ItemStack output, Ingredient recipeInput, int count, int duration) {
        this.id = id;
        this.output = output;
        this.recipeInput = recipeInput;
        this.count = count;
        this.duration = duration;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, @NotNull Level pLevel) {

        if(recipeInput.test(pContainer.getItem(0))){
            return duration >= 0;
        }
        return false;
    }

    public Ingredient getRecipeInput() {
        return recipeInput;
    }

    public int getCount() {
        return count;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess p_267052_) {
        return output.copy();
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Type implements RecipeType<DryingTableRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "drying_table";
    }

    public static class Serializer implements RecipeSerializer<DryingTableRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(OpolisUtilities.MOD_ID,"drying_table");

        @Override
        public DryingTableRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            JsonElement jsonelement = (GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient"));
            Ingredient ingredient = Ingredient.fromJson(jsonelement);
            int count = GsonHelper.getAsInt(json, "count", 1);

            int duration = GsonHelper.getAsInt(json, "duration");

            return new DryingTableRecipe(id, output, ingredient, count, duration);
        }

        @Override
        public DryingTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);

            int count = buf.readInt();
            int duration = buf.readInt();

            ItemStack output = buf.readItem();
            return new DryingTableRecipe(id, output, ingredient, count, duration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, DryingTableRecipe recipe) {

            buf.writeInt(recipe.getDuration());
            buf.writeInt(recipe.getCount());
            recipe.getRecipeInput().toNetwork(buf);

            buf.writeItemStack(recipe.output, false);
        }
    }
}