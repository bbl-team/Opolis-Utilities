package com.benbenlaw.opolisutilities.datagen.recipes;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.SoakingTableRecipe;
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
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SoakingTableRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected SizedIngredient input;
    protected ItemStack output;
    protected int duration;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SoakingTableRecipeBuilder(SizedIngredient input, ItemStack output, int duration) {
        this.input = input;
        this.output = output;
        this.duration = duration;
    }

    public static SoakingTableRecipeBuilder soakingTableRecipe(SizedIngredient input, ItemStack output, int duration) {
        return new SoakingTableRecipeBuilder(input, output, duration);
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
        return output.getItem();
    }

    public void save(@NotNull RecipeOutput recipeOutput) {
        this.save(recipeOutput, new ResourceLocation(OpolisUtilities.MOD_ID, "soaking_table/" +
                BuiltInRegistries.ITEM.getKey(this.output.getItem()).getPath()));
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        SoakingTableRecipe soakingTableRecipe = new SoakingTableRecipe(this.input, this.output, this.duration);
        recipeOutput.accept(id, soakingTableRecipe, builder.build(id.withPrefix("recipes/soaking_table/")));

    }
}
