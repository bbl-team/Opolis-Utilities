package com.benbenlaw.opolisutilities.integration.jei;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class OpolisIRecipeSlotTooltipCallback implements IRecipeSlotTooltipCallback {
    @Override
    @Deprecated
    public void onTooltip(IRecipeSlotView recipeSlotView, List<Component> tooltip) {
        // No-op: Default empty implementation
    }

    // Abstract method for the new onRichTooltip method
    @Override
    public abstract void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltipBuilder);
}