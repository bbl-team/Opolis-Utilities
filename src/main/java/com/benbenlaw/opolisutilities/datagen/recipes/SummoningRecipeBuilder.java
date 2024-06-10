package com.benbenlaw.opolisutilities.datagen.recipes;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.recipe.CatalogueRecipe;
import com.benbenlaw.opolisutilities.recipe.SummoningBlockRecipe;
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
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SummoningRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected SizedIngredient input;
    protected Ingredient catalyst;
    String mob;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SummoningRecipeBuilder(SizedIngredient input, Ingredient catalyst, String mob) {
        this.input = input;
        this.catalyst = catalyst;
        this.mob = mob;
    }

    public static SummoningRecipeBuilder CatalogueRecipeBuilder(SizedIngredient input, Ingredient catalyst, String mob) {
        return new SummoningRecipeBuilder(input, catalyst, mob);
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
        this.save(recipeOutput, new ResourceLocation(OpolisUtilities.MOD_ID, "summoning/")
        );
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        SummoningBlockRecipe summoningBlockRecipe = new SummoningBlockRecipe(this.input, this.catalyst, this.mob);
        recipeOutput.accept(id, summoningBlockRecipe, builder.build(id.withPrefix("recipes/summoning/")));

    }
}
