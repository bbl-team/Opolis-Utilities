package com.benbenlaw.opolisutilities.integration.kubejs;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import net.minecraft.resources.ResourceLocation;

public class KubeJSOpolisUtilitiesPlugin extends KubeJSPlugin {

    public static EventGroup GROUP = EventGroup.of("OpolisUtilitiesEvents");

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {

        event.register(new ResourceLocation(OpolisUtilities.MOD_ID, "drying_table"), DryingTableRecipeJS.SCHEMA);
        event.register(new ResourceLocation(OpolisUtilities.MOD_ID, "soaking_table"), SoakingTableRecipeJS.SCHEMA);
        event.register(new ResourceLocation(OpolisUtilities.MOD_ID, "rg2_blocks"), RG2RecipeJS.SCHEMA);
        event.register(new ResourceLocation(OpolisUtilities.MOD_ID, "resource_generator"), ResourceGeneratorRecipeJS.SCHEMA);
        event.register(new ResourceLocation(OpolisUtilities.MOD_ID, "fluid_generator"), FluidGeneratorRecipeJS.SCHEMA);
        event.register(new ResourceLocation(OpolisUtilities.MOD_ID, "rg2_speed_blocks"), RG2SpeedBlocksRecipeJS.SCHEMA);
        event.register(new ResourceLocation(OpolisUtilities.MOD_ID, "catalogue"), CatalogueRecipeJS.SCHEMA);
    }

    @Override
    public void registerEvents() {
        GROUP.register();
    }

}
