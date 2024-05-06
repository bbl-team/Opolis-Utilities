package com.benbenlaw.opolisutilities.recipe;

import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class NoInventoryRecipe extends RecipeWrapper {
    public static final NoInventoryRecipe INSTANCE = new NoInventoryRecipe();

    private NoInventoryRecipe() {
        super(new ItemStackHandler(0));
    }


}

