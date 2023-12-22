package com.benbenlaw.opolisutilities.integration.kubejs;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface CatalogueRecipeJS {

    RecipeKey<String> OUTPUT = StringComponent.ANY.key("result");
    RecipeKey<InputItem> INPUT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<Integer> INPUT_COUNT = NumberComponent.INT.key("itemInCount");
    RecipeKey<Integer> OUTPUT_COUNT = NumberComponent.INT.key("itemOutCount");
    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT, INPUT_COUNT, OUTPUT_COUNT);
}
