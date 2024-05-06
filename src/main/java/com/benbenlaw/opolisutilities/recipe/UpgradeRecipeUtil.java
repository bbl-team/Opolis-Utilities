package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class UpgradeRecipeUtil {} /*implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack upgradeItem;
    private final double outputIncreaseChance;
    private final int outputIncreaseAmount;
    private final double durationMultiplier;
    private final int durationSetAmount;
    private final double rfPerTickMultiplier;
    private final int rfPerTickAmount;
    private final double meshUseChance;
    private final int meshExtraDamage;
    private final double inputItemConsumeChance;
    private final int inputItemExtraAmount;

    public UpgradeRecipeUtil(ResourceLocation id, ItemStack upgradeItem,
                             double outputIncreaseChance, int outputIncreaseAmount,
                             double durationMultiplier, int durationSetAmount,
                             double rfPerTickMultiplier, int rfPerTickAmount,
                             double meshUseChance, int meshExtraDamage,
                             double inputItemConsumeChance, int inputItemExtraAmount) {
        this.id = id;
        this.upgradeItem = upgradeItem;
        this.outputIncreaseChance = outputIncreaseChance;
        this.outputIncreaseAmount = outputIncreaseAmount;
        this.durationMultiplier = durationMultiplier;
        this.durationSetAmount = durationSetAmount;
        this.rfPerTickMultiplier = rfPerTickMultiplier;
        this.rfPerTickAmount = rfPerTickAmount;
        this.meshUseChance = meshUseChance;
        this.meshExtraDamage = meshExtraDamage;
        this.inputItemConsumeChance = inputItemConsumeChance;
        this.inputItemExtraAmount = inputItemExtraAmount;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, @NotNull Level pLevel) {
        return true;
    }

    public ItemStack getUpgradeItem() {
        return upgradeItem;
    }

    public double getOutputIncreaseChance() {
        return outputIncreaseChance;
    }

    public int getOutputIncreaseAmount() {
        return outputIncreaseAmount;
    }

    public double getRFPerTick() {
        return rfPerTickMultiplier;
    }

    public double getMeshUseChance() {
        return meshUseChance;
    }

    public double getInputItemConsumeChance() {
        return inputItemConsumeChance;
    }

    public double getDurationMultiplier() {
        return this.durationMultiplier;
    }

    public int getDurationSetAmount() {
        return this.durationSetAmount;
    }
    public int getRfPerTickAmount() {
        return this.rfPerTickAmount;
    }
    public int getMeshExtraDamage() {
        return this.meshExtraDamage;
    }
    public int getInputItemExtraAmount() {
        return this.inputItemExtraAmount;
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
            double outputIncreaseChance = GsonHelper.getAsDouble(json, "output_increase_chance", 0.0);
            int outputIncreaseAmount = GsonHelper.getAsInt(json, "output_increase_amount", 0);
            double durationMultiplier = GsonHelper.getAsDouble(json, "duration_multiplier", 0.0);
            int durationSetAmount = GsonHelper.getAsInt(json, "duration_set_amount", 0);
            double rfPerTickMultiplier = GsonHelper.getAsDouble(json, "rf_per_tick_multiplier", 0.0);
            int rfPerTickAmount = GsonHelper.getAsInt(json, "rf_per_tick_amount", 0);
            double meshUseChance = GsonHelper.getAsDouble(json, "mesh_use_chance", 0.0);
            int meshExtraDamage = GsonHelper.getAsInt(json, "mesh_extra_damage", 0);
            double inputItemConsumeChance = GsonHelper.getAsDouble(json, "input_item_consume_chance", 0.0);
            int inputItemExtraAmount = GsonHelper.getAsInt(json, "input_item_extra_amount", 0);

            return new UpgradeRecipeUtil(id, upgradeItem, outputIncreaseChance, outputIncreaseAmount, durationMultiplier, durationSetAmount, rfPerTickMultiplier,
                    rfPerTickAmount, meshUseChance, meshExtraDamage, inputItemConsumeChance, inputItemExtraAmount);
        }

        @Override
        public UpgradeRecipeUtil fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ItemStack upgradeItem = buf.readItem();
            double outputIncreaseChance = buf.readDouble();
            int outputIncreaseAmount = buf.readInt();
            double durationMultiplier = buf.readDouble();
            int durationSetAmount = buf.readInt();
            double rfPerTickMultiplier = buf.readDouble();
            int rfPerTickAmount = buf.readInt();
            double meshUseChance = buf.readDouble();
            int meshExtraDamage = buf.readInt();
            double inputItemConsumeChance = buf.readDouble();
            int inputItemExtraAmount = buf.readInt();

            return new UpgradeRecipeUtil(id, upgradeItem, outputIncreaseChance, outputIncreaseAmount, durationMultiplier, durationSetAmount, rfPerTickMultiplier,
                    rfPerTickAmount, meshUseChance, meshExtraDamage, inputItemConsumeChance, inputItemExtraAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, UpgradeRecipeUtil recipe) {
            buf.writeItem(recipe.upgradeItem);
            buf.writeDouble(recipe.outputIncreaseChance);
            buf.writeInt(recipe.outputIncreaseAmount);
            buf.writeDouble(recipe.durationMultiplier);
            buf.writeInt(recipe.durationSetAmount);
            buf.writeDouble(recipe.rfPerTickMultiplier);
            buf.writeInt(recipe.rfPerTickAmount);
            buf.writeDouble(recipe.meshUseChance);
            buf.writeInt(recipe.meshExtraDamage);
            buf.writeDouble(recipe.inputItemConsumeChance);
            buf.writeInt(recipe.inputItemExtraAmount);
        }
    }
}

*/