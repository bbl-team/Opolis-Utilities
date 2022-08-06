package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.ResourceGeneratorRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {




    //@SubscribeEvent
    public static void registerRecipeTypes(final RegistryAccess.RegistryEntry<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, DryingTableRecipe.Type.ID, DryingTableRecipe.Type.INSTANCE);

        Registry.register(Registry.RECIPE_TYPE, ResourceGeneratorRecipe.Type.ID, ResourceGeneratorRecipe.Type.INSTANCE);

    }


}
