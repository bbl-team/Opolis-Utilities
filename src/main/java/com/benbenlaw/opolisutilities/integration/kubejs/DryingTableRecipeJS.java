package com.benbenlaw.opolisutilities.integration.kubejs;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

import java.util.logging.Logger;

public interface DryingTableRecipeJS {

    RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("output");
    RecipeKey<InputItem[]> INPUT = ItemComponents.INPUT_ARRAY.key("ingredient");
    RecipeKey<Integer> INPUT_COUNT = NumberComponent.INT.key("count");
    RecipeKey<Integer> DURATION = NumberComponent.INT.key("duration");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT, INPUT_COUNT, DURATION);

}

