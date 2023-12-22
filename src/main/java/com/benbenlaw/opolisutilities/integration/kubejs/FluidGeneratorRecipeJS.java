package com.benbenlaw.opolisutilities.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface FluidGeneratorRecipeJS {

    RecipeKey<String> FLUID = StringComponent.ANY.key("fluid");
    RecipeKey<Integer> AMOUNT = NumberComponent.INT.key("fluidAmount");
    RecipeSchema SCHEMA = new RecipeSchema(FLUID, AMOUNT);
}

