package com.benbenlaw.opolisutilities.datagen.recipes;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.recipe.SpeedUpgradesRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class SpeedUpgradesRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected Ingredient input;
    protected int tickRate;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SpeedUpgradesRecipeBuilder(Ingredient input, int tickRate) {
        this.input = input;
        this.tickRate = tickRate;
    }

    public static SpeedUpgradesRecipeBuilder resourceGeneratorRecipeBuilder(Ingredient input, int tickRate) {
        return new SpeedUpgradesRecipeBuilder(input, tickRate);
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return ItemStack.EMPTY.getItem();
    }

    public void save(@NotNull RecipeOutput recipeOutput) {
        this.save(recipeOutput, new ResourceLocation(OpolisUtilities.MOD_ID, "speed_upgrades/" +
                BuiltInRegistries.ITEM.getKey(Objects.requireNonNull(this.input.getItems()[0].getItem())).getPath()));
    }


    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        SpeedUpgradesRecipe speedUpgradesRecipe = new SpeedUpgradesRecipe(this.input, this.tickRate);
        recipeOutput.accept(id, speedUpgradesRecipe, builder.build(id.withPrefix("recipes/speed_upgrades/")));

    }
}
