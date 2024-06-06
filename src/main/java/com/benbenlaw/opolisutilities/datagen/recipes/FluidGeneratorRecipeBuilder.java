package com.benbenlaw.opolisutilities.datagen.recipes;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.recipe.FluidGeneratorRecipe;
import com.benbenlaw.opolisutilities.recipe.ResourceGeneratorRecipe;
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
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class FluidGeneratorRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected FluidStack input;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public FluidGeneratorRecipeBuilder(FluidStack input) {
        this.input = input;
    }

    public static FluidGeneratorRecipeBuilder resourceGeneratorRecipeBuilder(FluidStack input) {
        return new FluidGeneratorRecipeBuilder(input);
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
        this.save(recipeOutput, new ResourceLocation(OpolisUtilities.MOD_ID, "fluid_generator/" +
                BuiltInRegistries.FLUID.getKey(this.input.getFluid()).getPath()));
    }


    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        FluidGeneratorRecipe fluidGeneratorRecipe = new FluidGeneratorRecipe(this.input);
        recipeOutput.accept(id, fluidGeneratorRecipe, builder.build(id.withPrefix("recipes/fluid_generator/")));

    }
}
