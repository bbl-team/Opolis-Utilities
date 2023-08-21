package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.google.gson.JsonArray;
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
import org.jetbrains.annotations.NotNull;

public class SoakingTableRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> inputItems;
    public final int count;
    public final int duration;

    public SoakingTableRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> inputItems, int count, int duration) {
        this.id = id;
        this.output = output;
        this.inputItems = inputItems;
        this.count = count;
        this.duration = duration;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, @NotNull Level pLevel) {

        if(inputItems.get(0).test(pContainer.getItem(0))){
            return duration >= 0;
        }
        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
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
        return this.duration;
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

    public static class Type implements RecipeType<SoakingTableRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "soaking_table";
    }

    public static class Serializer implements RecipeSerializer<SoakingTableRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(OpolisUtilities.MOD_ID,"soaking_table");

        @Override
        public SoakingTableRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredient");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            int count = GsonHelper.getAsInt(json, "count", 1);
            int duration = GsonHelper.getAsInt(json, "duration");

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new SoakingTableRecipe(id, output, inputs, count, duration);
        }

        @Override
        public SoakingTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            int duration = buf.readInt();
            int count = buf.readInt();

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new SoakingTableRecipe(id, output, inputs, count, duration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SoakingTableRecipe recipe) {

            buf.writeInt(recipe.getIngredients().size());
            buf.writeInt(recipe.getDuration());
            buf.writeInt(recipe.getCount());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }

            buf.writeItemStack(recipe.output, false);
        }
    }
}