package com.benbenlaw.opolisutilities.recipe;

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

public record ResourceGeneratorRecipe(Ingredient input) implements Recipe<NoInventoryRecipe> {

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
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull NoInventoryRecipe inv, HolderLookup.@NotNull Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return ItemStack.EMPTY;
    }


    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ResourceGeneratorRecipe.Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ResourceGeneratorRecipe.Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Type implements RecipeType<ResourceGeneratorRecipe> {
        private Type() {}
        public static final ResourceGeneratorRecipe.Type INSTANCE = new ResourceGeneratorRecipe.Type();
    }


    public static class Serializer implements RecipeSerializer<ResourceGeneratorRecipe> {
        public static final ResourceGeneratorRecipe.Serializer INSTANCE = new Serializer();

        public final MapCodec<ResourceGeneratorRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                                Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(ResourceGeneratorRecipe::input))
                        .apply(instance, ResourceGeneratorRecipe::new)
        );

        private final StreamCodec<RegistryFriendlyByteBuf, ResourceGeneratorRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read);

        @Override
        public @NotNull MapCodec<ResourceGeneratorRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ResourceGeneratorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ResourceGeneratorRecipe read(RegistryFriendlyByteBuf buffer) {
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);

            return new ResourceGeneratorRecipe(input);
        }

        private static void write(RegistryFriendlyByteBuf buffer, ResourceGeneratorRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input);
        }

    }
}

