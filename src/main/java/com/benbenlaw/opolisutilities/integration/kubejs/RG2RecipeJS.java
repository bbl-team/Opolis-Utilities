package com.benbenlaw.opolisutilities.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface RG2RecipeJS {

    RecipeKey<String> BLOCK = StringComponent.ANY.key("block");
    RecipeSchema SCHEMA = new RecipeSchema(BLOCK);
}

