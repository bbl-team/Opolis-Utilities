package com.benbenlaw.opolisutilities.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType;
import org.jetbrains.annotations.NotNull;

public record FluidGeneratorRecipe(FluidStack input) implements Recipe<NoInventoryRecipe> {

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
        return FluidGeneratorRecipe.Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return FluidGeneratorRecipe.Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Type implements RecipeType<FluidGeneratorRecipe> {
        private Type() { }
        public static final FluidGeneratorRecipe.Type INSTANCE = new FluidGeneratorRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<FluidGeneratorRecipe> {
        public static final FluidGeneratorRecipe.Serializer INSTANCE = new Serializer();

        public final MapCodec<FluidGeneratorRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        FluidStack.CODEC.fieldOf("fluid").forGetter(FluidGeneratorRecipe::input)
                ).apply(instance, FluidGeneratorRecipe::new)
        );

        private final StreamCodec<RegistryFriendlyByteBuf, FluidGeneratorRecipe> STREAM_CODEC = StreamCodec.of(
                FluidGeneratorRecipe.Serializer::write, FluidGeneratorRecipe.Serializer::read);

        @Override
        public @NotNull MapCodec<FluidGeneratorRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, FluidGeneratorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static FluidGeneratorRecipe read(RegistryFriendlyByteBuf buffer) {
            FluidStack input = FluidStack.STREAM_CODEC.decode(buffer);
            return new FluidGeneratorRecipe(input);
        }

        private static void write(RegistryFriendlyByteBuf buffer, FluidGeneratorRecipe recipe) {
            FluidStack.STREAM_CODEC.encode(buffer, recipe.input);
        }
    }
}
