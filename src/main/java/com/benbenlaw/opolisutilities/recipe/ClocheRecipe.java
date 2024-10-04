package com.benbenlaw.opolisutilities.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public record ClocheRecipe(Ingredient seed, Ingredient catalyst, Ingredient soil, ItemStack mainOutput,
                           ItemStack chanceOutput1, double chanceOutputChance1,
                           ItemStack chanceOutput2, double chanceOutputChance2,
                           ItemStack chanceOutput3, double chanceOutputChance3,
                           double durationModifier) implements Recipe<RecipeInput> {

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(1);
        ingredients.add(seed);
        return ingredients;
    }

    @Override
    public boolean matches(RecipeInput container, @NotNull Level level) {

        boolean containsCatalystInSlot = !container.getItem(2).isEmpty();
        boolean needCatalyst = !catalyst.isEmpty();

        if (containsCatalystInSlot) {
            if (catalyst.test(container.getItem(2))) {
                return seed.test(container.getItem(0)) && soil.test(container.getItem(1));
            } else {
                return false;
            }
        }

        return seed.test(container.getItem(0)) && soil.test(container.getItem(1));

    }






    @Override
    public ItemStack assemble(RecipeInput container, HolderLookup.Provider provider) {
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
        return ClocheRecipe.Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ClocheRecipe.Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Type implements RecipeType<ClocheRecipe> {
        private Type() {}
        public static final ClocheRecipe.Type INSTANCE = new ClocheRecipe.Type();
    }


    public static class Serializer implements RecipeSerializer<ClocheRecipe> {
        public static final ClocheRecipe.Serializer INSTANCE = new Serializer();

        public final MapCodec<ClocheRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        Ingredient.CODEC.fieldOf("seed").forGetter(ClocheRecipe::seed),
                        Ingredient.CODEC.fieldOf("catalyst").orElse(Ingredient.EMPTY).forGetter(ClocheRecipe::catalyst),
                        Ingredient.CODEC.fieldOf("soil").forGetter(ClocheRecipe::soil),
                        ItemStack.CODEC.fieldOf("mainOutput").forGetter(ClocheRecipe::mainOutput),
                        ItemStack.OPTIONAL_CODEC.optionalFieldOf("chanceOutput1", ItemStack.EMPTY).forGetter(ClocheRecipe::chanceOutput1),
                        Codec.DOUBLE.optionalFieldOf("chanceOutputChance1", 0.0).orElse(0.0).forGetter(ClocheRecipe::chanceOutputChance1),
                        ItemStack.OPTIONAL_CODEC.optionalFieldOf("chanceOutput2", ItemStack.EMPTY).forGetter(ClocheRecipe::chanceOutput2),
                        Codec.DOUBLE.optionalFieldOf("chanceOutputChance2", 0.0).forGetter(ClocheRecipe::chanceOutputChance2),
                        ItemStack.OPTIONAL_CODEC.optionalFieldOf("chanceOutput3", ItemStack.EMPTY).forGetter(ClocheRecipe::chanceOutput3),
                        Codec.DOUBLE.optionalFieldOf("chanceOutputChance3", 0.0).forGetter(ClocheRecipe::chanceOutputChance3),
                        Codec.DOUBLE.fieldOf("durationModifier").forGetter(ClocheRecipe::durationModifier)
                ).apply(instance, Serializer::createClocheRecipe)
        );

        private final StreamCodec<RegistryFriendlyByteBuf, ClocheRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read);

        @Override
        public @NotNull MapCodec<ClocheRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ClocheRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ClocheRecipe read(RegistryFriendlyByteBuf buffer) {
            Ingredient seed = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient catalyst = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient soil = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack mainOutput = ItemStack.STREAM_CODEC.decode(buffer);
            ItemStack chanceOutput1 = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            double chanceOutputChance1 = buffer.readDouble();
            ItemStack chanceOutput2 = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            double chanceOutputChance2 = buffer.readDouble();
            ItemStack chanceOutput3 = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            double chanceOutputChance3 = buffer.readDouble();
            double durationModifier = buffer.readDouble();

            return new ClocheRecipe(seed, catalyst, soil, mainOutput, chanceOutput1, chanceOutputChance1, chanceOutput2, chanceOutputChance2, chanceOutput3, chanceOutputChance3, durationModifier);



        }

        private static void write(RegistryFriendlyByteBuf buffer, ClocheRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.seed);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.catalyst);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.soil);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.mainOutput);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.chanceOutput1);
            buffer.writeDouble(recipe.chanceOutputChance1);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.chanceOutput2);
            buffer.writeDouble(recipe.chanceOutputChance2);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.chanceOutput3);
            buffer.writeDouble(recipe.chanceOutputChance3);
            buffer.writeDouble(recipe.durationModifier);

        }
        private static ClocheRecipe createClocheRecipe(Ingredient input, Ingredient catalyst, Ingredient soil, ItemStack mainOutput, ItemStack chanceOutput1, double chanceOutputChance1, ItemStack chanceOutput2, double chanceOutputChance2, ItemStack chanceOutput3, double chanceOutputChance3, double durationModifier) {
            return new ClocheRecipe(input, catalyst, soil, mainOutput, chanceOutput1, chanceOutputChance1, chanceOutput2, chanceOutputChance2, chanceOutput3, chanceOutputChance3, durationModifier);
        }
    }
}

