package com.benbenlaw.opolisutilities.integration;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.enchantment.ModEnchantments;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

    //    reg.addIngredientInfo(new ItemStack(Items.ENCHANTED_BOOK.readShareTag(ModEnchantments.WARDEN_SMITE.get());), VanillaTypes.ITEM_STACK, Component.translatable("jei.essence.spawner_shard"));



    }
}
