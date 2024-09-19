package com.benbenlaw.opolisutilities.integration.jei;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.item.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class InformationJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "information");
    }
     @Override
    public void registerRecipes(IRecipeRegistration reg) {

        //Blocks
         reg.addIngredientInfo(new ItemStack(ModBlocks.DRYING_TABLE.get()), VanillaTypes.ITEM_STACK,
                 Component.translatable("jei.opolisutilities.drying_table"));

        reg.addIngredientInfo(new ItemStack(ModItems.FLOATING_BLOCK.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.opolisutilities.floating_block"));

        reg.addIngredientInfo(new ItemStack(ModBlocks.ITEM_REPAIRER.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.opolisutilities.item_repairer"));

        reg.addIngredientInfo(new ItemStack(ModBlocks.RESOURCE_GENERATOR.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.opolisutilities.resource_generator"));

        reg.addIngredientInfo(new ItemStack(ModBlocks.FLUID_GENERATOR.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.opolisutilities.fluid_generator"));

        reg.addIngredientInfo(new ItemStack(ModBlocks.BLOCK_BREAKER.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.opolisutilities.block_breaker"));

        reg.addIngredientInfo(new ItemStack(ModBlocks.BLOCK_PLACER.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.opolisutilities.block_placer"));


        //Items
        reg.addIngredientInfo(new ItemStack(ModItems.PORTABLE_GUI.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.opolisutilities.portable_gui"));


   //     reg.addIngredientInfo(new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.opolisutilities.resource_generator_2.information"));
    //    reg.addIngredientInfo(new ItemStack(ModBlocks.FLUID_GENERATOR.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.opolisutilities.fluid_generator.information"));

    }
}
