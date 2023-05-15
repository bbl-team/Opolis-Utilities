package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.google.gson.JsonObject;
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

public class RG2BlocksRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String block;

    public RG2BlocksRecipe(ResourceLocation id, String block) {
        this.id = id;
        this.block = block;
    }

    public String getBlock() {
        return block;

    }

    @Override
    public boolean matches(@NotNull NoInventoryRecipe inv, @NotNull Level pLevel) {
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull NoInventoryRecipe inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RG2BlocksRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RG2BlocksRecipe.Type.INSTANCE;
    }
    public static class Type implements RecipeType<RG2BlocksRecipe> {
        private Type() { }
        public static final RG2BlocksRecipe.Type INSTANCE = new RG2BlocksRecipe.Type();
        public static final String ID = "rg2_blocks";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Serializer implements RecipeSerializer<RG2BlocksRecipe> {
        public static final RG2BlocksRecipe.Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(OpolisUtilities.MOD_ID,"rg2_blocks");

        @Override
        public RG2BlocksRecipe fromJson(ResourceLocation id, JsonObject json) {
            String block = GsonHelper.getAsString(json, "block");

            return new RG2BlocksRecipe(id, block);
        }

        @Override
        public RG2BlocksRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String block = buf.readUtf();

            return new RG2BlocksRecipe(id, block);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RG2BlocksRecipe recipe) {

            buf.writeUtf(recipe.getGroup(), Short.MAX_VALUE);
        }

        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        public ResourceLocation getRegistryName() {
            return ID;
        }

        public Class<RecipeSerializer<?>> getRegistryType() {
            return RG2BlocksRecipe.Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }

}
