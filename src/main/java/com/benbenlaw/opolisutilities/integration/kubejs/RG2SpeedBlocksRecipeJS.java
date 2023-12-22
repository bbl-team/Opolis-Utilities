package com.benbenlaw.opolisutilities.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface RG2SpeedBlocksRecipeJS {

    RecipeKey<String> BLOCK = StringComponent.ANY.key("block");
    RecipeKey<Integer> TICK_RATE = NumberComponent.INT.key("tickRate");
    RecipeSchema SCHEMA = new RecipeSchema(BLOCK, TICK_RATE);
}

