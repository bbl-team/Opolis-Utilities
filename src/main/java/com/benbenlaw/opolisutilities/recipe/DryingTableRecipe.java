package com.benbenlaw.opolisutilities.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

public record DryingTableRecipe(SizedIngredient input, ItemStack output, int duration) implements Recipe<SimpleContainer> {

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(1);
        ingredients.add(input.ingredient());
        return ingredients;
    }

    @Override
    public boolean matches(SimpleContainer container, @NotNull Level level) {
        return input.test(container.getItem(0)) && duration >= 0;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.output.copy();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer container, HolderLookup.@NotNull Provider provider) {
        return this.output.copy();
    }

    public int getDuration() {
        return this.duration;
    }

    public int getIngredientStackCount() {
        return this.input.count();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return DryingTableRecipe.Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return DryingTableRecipe.Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Type implements RecipeType<DryingTableRecipe> {
        private Type() {}
        public static final DryingTableRecipe.Type INSTANCE = new DryingTableRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<DryingTableRecipe> {
        public static final DryingTableRecipe.Serializer INSTANCE = new DryingTableRecipe.Serializer();

        public final MapCodec<DryingTableRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        SizedIngredient.FLAT_CODEC.fieldOf("input").forGetter(DryingTableRecipe::input),
                        ItemStack.CODEC.fieldOf("output").forGetter(DryingTableRecipe::output),
                        Codec.INT.fieldOf("duration").forGetter(DryingTableRecipe::duration)
                ).apply(instance, Serializer::createDryingTableRecipe)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, DryingTableRecipe> STREAM_CODEC = StreamCodec.of(
                DryingTableRecipe.Serializer::write, DryingTableRecipe.Serializer::read);

        @Override
        public @NotNull MapCodec<DryingTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, DryingTableRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static DryingTableRecipe read(RegistryFriendlyByteBuf buffer) {
            SizedIngredient input = SizedIngredient.STREAM_CODEC.decode(buffer);
            ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
            int duration = buffer.readInt();
            return new DryingTableRecipe(input, output, duration);
        }

        private static void write(RegistryFriendlyByteBuf buffer, DryingTableRecipe recipe) {
            SizedIngredient.STREAM_CODEC.encode(buffer, recipe.input);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
            buffer.writeInt(recipe.duration);
        }

        private static DryingTableRecipe createDryingTableRecipe(SizedIngredient input, ItemStack output, int duration) {
            return new DryingTableRecipe(input, output, duration);
        }
    }
}
