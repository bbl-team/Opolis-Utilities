package com.benbenlaw.opolisutilities.datagen.recipes;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.recipe.ClocheRecipe;
import com.benbenlaw.opolisutilities.recipe.SummoningBlockRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
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

public class ClocheRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected Ingredient seed;
    protected Ingredient catalyst;
    protected Ingredient soil;
    protected ItemStack mainOutput;
    protected ItemStack chanceOutput1;
    protected double chanceOutputChance1;
    protected ItemStack chanceOutput2;
    protected double chanceOutputChance2;
    protected ItemStack chanceOutput3;
    protected double chanceOutputChance3;
    protected double durationModifier;
    String mob;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public ClocheRecipeBuilder(Ingredient seed, Ingredient catalyst, Ingredient soil, ItemStack mainOutput, ItemStack chanceOutput1, double chanceOutputChance1, ItemStack chanceOutput2, double chanceOutputChance2, ItemStack chanceOutput3, double chanceOutputChance3, double durationModifier) {
        this.seed = seed;
        this.catalyst = catalyst != null ? catalyst : Ingredient.EMPTY;
        this.soil = soil;
        this.mainOutput = mainOutput;
        this.chanceOutput1 = chanceOutput1 != null ? chanceOutput1 : ItemStack.EMPTY;
        this.chanceOutputChance1 = chanceOutputChance1 != 0 ? chanceOutputChance1 : 0.0;
        this.chanceOutput2 = chanceOutput2 != null ? chanceOutput2 : ItemStack.EMPTY;
        this.chanceOutputChance2 = chanceOutputChance2 != 0 ? chanceOutputChance2 : 0.0;
        this.chanceOutput3 = chanceOutput3 != null ? chanceOutput3 : ItemStack.EMPTY;
        this.chanceOutputChance3 = chanceOutputChance3 != 0 ? chanceOutputChance3 : 0.0;
        this.durationModifier = durationModifier;
    }

    public static ClocheRecipeBuilder ClocheBuilder(Ingredient seed, Ingredient catalyst, Ingredient soil, ItemStack mainOutput, ItemStack chanceOutput1, double chanceOutputChance1, ItemStack chanceOutput2, double chanceOutputChance2, ItemStack chanceOutput3, double chanceOutputChance3, double durationModifier) {
        return new ClocheRecipeBuilder(seed, catalyst, soil, mainOutput, chanceOutput1, chanceOutputChance1, chanceOutput2, chanceOutputChance2, chanceOutput3, chanceOutputChance3, durationModifier);
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
        this.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "cloche/")
        );
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        ClocheRecipe clocheRecipe = new ClocheRecipe(this.seed, this.catalyst, this.soil, this.mainOutput, this.chanceOutput1, this.chanceOutputChance1, this.chanceOutput2, this.chanceOutputChance2, this.chanceOutput3, this.chanceOutputChance3, this.durationModifier);
        recipeOutput.accept(id, clocheRecipe, builder.build(id.withPrefix("recipes/cloche/")));

    }
}
