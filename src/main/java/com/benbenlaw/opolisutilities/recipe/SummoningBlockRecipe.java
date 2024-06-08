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

public record SummoningBlockRecipe(SizedIngredient input, Ingredient catalyst, String mob) implements Recipe<SimpleContainer> {

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(1);
        ingredients.add(input.ingredient());
        return ingredients;
    }

    @Override
    public boolean matches(SimpleContainer container, @NotNull Level level) {
        return input.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer container, HolderLookup.Provider provider) {
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
        return SummoningBlockRecipe.Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return SummoningBlockRecipe.Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Type implements RecipeType<SummoningBlockRecipe> {
        private Type() {}
        public static final SummoningBlockRecipe.Type INSTANCE = new SummoningBlockRecipe.Type();
    }


    public static class Serializer implements RecipeSerializer<SummoningBlockRecipe> {
        public static final SummoningBlockRecipe.Serializer INSTANCE = new Serializer();

        public final MapCodec<SummoningBlockRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        SizedIngredient.FLAT_CODEC.fieldOf("input").forGetter(SummoningBlockRecipe::input),
                        Ingredient.CODEC.fieldOf("catalyst").forGetter(SummoningBlockRecipe::catalyst),
                        Codec.STRING.fieldOf("mob").forGetter(SummoningBlockRecipe::mob)
                ).apply(instance, Serializer::createSummoningBlockRecipe)
        );

        private final StreamCodec<RegistryFriendlyByteBuf, SummoningBlockRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read);

        @Override
        public @NotNull MapCodec<SummoningBlockRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, SummoningBlockRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SummoningBlockRecipe read(RegistryFriendlyByteBuf buffer) {
            SizedIngredient input = SizedIngredient.STREAM_CODEC.decode(buffer);
            Ingredient catalyst = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            String mob = buffer.readUtf();

            return new SummoningBlockRecipe(input, catalyst, mob);
        }

        private static void write(RegistryFriendlyByteBuf buffer, SummoningBlockRecipe recipe) {
            SizedIngredient.STREAM_CODEC.encode(buffer, recipe.input);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.catalyst);
            buffer.writeUtf(recipe.mob);
        }
        private static SummoningBlockRecipe createSummoningBlockRecipe(SizedIngredient input, Ingredient catalyst, String mob) {
            return new SummoningBlockRecipe(input, catalyst, mob);
        }
    }
}

