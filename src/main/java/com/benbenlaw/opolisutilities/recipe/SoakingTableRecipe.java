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

public record SoakingTableRecipe(SizedIngredient input, ItemStack output, int duration) implements Recipe<SimpleContainer> {

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
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Type implements RecipeType<SoakingTableRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<SoakingTableRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public final MapCodec<SoakingTableRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        SizedIngredient.FLAT_CODEC.fieldOf("input").forGetter(SoakingTableRecipe::input),
                        ItemStack.CODEC.fieldOf("output").forGetter(SoakingTableRecipe::output),
                        Codec.INT.fieldOf("duration").forGetter(SoakingTableRecipe::getDuration)
                ).apply(instance, Serializer::createSoakingTableRecipe)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, SoakingTableRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read);

        @Override
        public @NotNull MapCodec<SoakingTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, SoakingTableRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SoakingTableRecipe read(RegistryFriendlyByteBuf buffer) {
            SizedIngredient input = SizedIngredient.STREAM_CODEC.decode(buffer);
            ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
            int duration = buffer.readInt();
            return new SoakingTableRecipe(input, output, duration);
        }

        private static void write(RegistryFriendlyByteBuf buffer, SoakingTableRecipe recipe) {
            SizedIngredient.STREAM_CODEC.encode(buffer, recipe.input);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
            buffer.writeInt(recipe.duration);
        }

        private static SoakingTableRecipe createSoakingTableRecipe(SizedIngredient input, ItemStack output, int duration) {
            return new SoakingTableRecipe(input, output, duration);
        }
    }
}
