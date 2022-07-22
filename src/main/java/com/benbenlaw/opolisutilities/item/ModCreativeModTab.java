package com.benbenlaw.opolisutilities.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModTab {
    public static final CreativeModeTab OPOLIS_UTILITIES = new CreativeModeTab("opolis_utilities") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.COPPER_NUGGET.get());
        }
    };


}
