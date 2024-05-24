package com.benbenlaw.opolisutilities.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record SpeedUpgradesRecipe(Ingredient input, int tickRate) implements Recipe<NoInventoryRecipe> {

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(1);
        ingredients.add(input);
        return ingredients;
    }

    @Override
    public boolean matches(@NotNull NoInventoryRecipe inv, @NotNull Level pLevel) {
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull NoInventoryRecipe inv, HolderLookup.@NotNull Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }


    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return ItemStack.EMPTY;
    }


    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SpeedUpgradesRecipe.Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return SpeedUpgradesRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<SpeedUpgradesRecipe> {
        private Type() {
        }

        public static final SpeedUpgradesRecipe.Type INSTANCE = new SpeedUpgradesRecipe.Type();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<SpeedUpgradesRecipe> {
        public static final SpeedUpgradesRecipe.Serializer INSTANCE = new Serializer();

        public final MapCodec<SpeedUpgradesRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                                Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(SpeedUpgradesRecipe::input),
                                Codec.INT.fieldOf("tickRate").forGetter(SpeedUpgradesRecipe::tickRate))
                        .apply(instance, SpeedUpgradesRecipe::new)
        );

        private final StreamCodec<RegistryFriendlyByteBuf, SpeedUpgradesRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read);

        @Override
        public @NotNull MapCodec<SpeedUpgradesRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, SpeedUpgradesRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SpeedUpgradesRecipe read(RegistryFriendlyByteBuf buffer) {
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int tickRate = buffer.readInt();

            return new SpeedUpgradesRecipe(input, tickRate);
        }

        private static void write(RegistryFriendlyByteBuf buffer, SpeedUpgradesRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input);
            buffer.writeInt(recipe.tickRate);
        }
    }
}


