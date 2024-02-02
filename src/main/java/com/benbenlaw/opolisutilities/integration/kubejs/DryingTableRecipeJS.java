package com.benbenlaw.opolisutilities.integration.kubejs;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface DryingTableRecipeJS {

    RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("output");
    RecipeKey<InputItem[]> INPUT = ItemComponents.INPUT_ARRAY.key("ingredient");
    RecipeKey<Integer> INPUT_COUNT = NumberComponent.INT.key("count");
    RecipeKey<Integer> DURATION = NumberComponent.INT.key("duration");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT, INPUT_COUNT, DURATION);

}

