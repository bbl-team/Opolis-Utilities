package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public class FluidGeneratorRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String fluid;
    private final int fluidAmount;

    public FluidGeneratorRecipe(ResourceLocation id, String fluid, int fluidAmount) {
        this.id = id;
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
    }

    public String getFluid() {
        return fluid;
    }

    public int getFluidAmount() {
        return fluidAmount;
    }

    @Override
    public boolean matches(@NotNull NoInventoryRecipe inv, @NotNull Level pLevel) {
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull NoInventoryRecipe inv, RegistryAccess p_267165_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FluidGeneratorRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return FluidGeneratorRecipe.Type.INSTANCE;
    }
    public static class Type implements RecipeType<FluidGeneratorRecipe> {
        private Type() { }
        public static final FluidGeneratorRecipe.Type INSTANCE = new FluidGeneratorRecipe.Type();
        public static final String ID = "fluid_generator";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Serializer implements RecipeSerializer<FluidGeneratorRecipe> {
        public static final FluidGeneratorRecipe.Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(OpolisUtilities.MOD_ID, "fluid_generator");

        @Override
        public FluidGeneratorRecipe fromJson(ResourceLocation id, JsonObject json) {
            String fluid = GsonHelper.getAsString(json, "fluid");
            int fluidAmount = GsonHelper.getAsInt(json, "fluidAmount", 100);

            return new FluidGeneratorRecipe(id, fluid, fluidAmount);
        }

        @Override
        public FluidGeneratorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String fluid = buf.readUtf();
            int fluidAmount = buf.readInt();

            return new FluidGeneratorRecipe(id, fluid, fluidAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FluidGeneratorRecipe recipe) {

            buf.writeUtf(recipe.getFluid(), Short.MAX_VALUE);
            buf.writeInt(recipe.getFluidAmount());
        }
    }
}
