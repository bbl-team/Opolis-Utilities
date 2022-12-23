package com.benbenlaw.opolisutilities.integration;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.enchantment.ModEnchantments;
import com.benbenlaw.opolisutilities.util.ModTags;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@JeiPlugin
public class InformationJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(OpolisUtilities.MOD_ID, "information");
    }
     @Override
    public void registerRecipes(IRecipeRegistration reg) {

         reg.addIngredientInfo(new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()), VanillaTypes.ITEM_STACK,
                 Component.translatable("jei.opolisutilities.resource_generator"));
    }

    private class Blocks {
    }

    private class List<T> {
    }
}
