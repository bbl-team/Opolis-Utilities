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

public class UpgradeRecipeUtil implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack upgradeItem;
    private final double outputIncrease;
    private final double duration;
    private final double rfPerTick;
    private final double meshUseChance;
    private final double inputItemConsumeChance;


    public UpgradeRecipeUtil(ResourceLocation id, ItemStack upgradeItem, double outputIncrease, double duration, double rfPerTick,  double meshUseChance, double inputItemConsumeChance) {
        this.id = id;
        this.upgradeItem = upgradeItem;
        this.outputIncrease = outputIncrease;
        this.duration = duration;
        this.rfPerTick = rfPerTick;
        this.meshUseChance = meshUseChance;
        this.inputItemConsumeChance = inputItemConsumeChance;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, @NotNull Level pLevel) {
        return true;
    }

    public ItemStack getUpgradeItem() {
        return upgradeItem;
    }

    public double getOutputIncrease() {
        return outputIncrease;
    }

    public double getRFPerTick() {
        return rfPerTick;
    }

    public double getMeshUseChance() {
        return meshUseChance;
    }

    public double getInputItemConsumeChance() {
        return inputItemConsumeChance;
    }

    public double getDuration() {
        return this.duration;
    }

    @Override
    public @NotNull ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UpgradeRecipeUtil.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return UpgradeRecipeUtil.Type.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Type implements RecipeType<UpgradeRecipeUtil> {
        private Type() { }
        public static final UpgradeRecipeUtil.Type INSTANCE = new UpgradeRecipeUtil.Type();
        public static final String ID = "upgrades";
    }

    public static class Serializer implements RecipeSerializer<UpgradeRecipeUtil> {
        public static final UpgradeRecipeUtil.Serializer INSTANCE = new UpgradeRecipeUtil.Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(OpolisUtilities.MOD_ID,"upgrades");

        @Override
        public UpgradeRecipeUtil fromJson(ResourceLocation id, JsonObject json) {
            ItemStack upgradeItem = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "item"));
            double outputIncrease = GsonHelper.getAsDouble(json, "output_increase", 1);
            double duration = GsonHelper.getAsDouble(json, "duration", 1);
            double rfPerTick = GsonHelper.getAsDouble(json, "rf_per_tick", 1);
            double meshUseChance = GsonHelper.getAsDouble(json, "mesh_use_chance", 1);
            double inputItemConsumeChance = GsonHelper.getAsDouble(json, "input_item_consume_chance", 1);

            return new UpgradeRecipeUtil(id, upgradeItem, outputIncrease, duration, rfPerTick, meshUseChance, inputItemConsumeChance);
        }

        @Override
        public UpgradeRecipeUtil fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ItemStack upgradeItem = buf.readItem();
            double outputIncrease = buf.readDouble();
            double duration = buf.readDouble();
            double rfPerTick = buf.readDouble();
            double meshUseChance = buf.readDouble();
            double inputItemConsumeChance = buf.readDouble();

            return new UpgradeRecipeUtil(id, upgradeItem, outputIncrease, duration, rfPerTick, meshUseChance, inputItemConsumeChance);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, UpgradeRecipeUtil recipe) {
            buf.writeItem(recipe.upgradeItem);
            buf.writeDouble(recipe.outputIncrease);
            buf.writeDouble(recipe.duration);
            buf.writeDouble(recipe.rfPerTick);
            buf.writeDouble(recipe.meshUseChance);
            buf.writeDouble(recipe.inputItemConsumeChance);
        }
    }
}