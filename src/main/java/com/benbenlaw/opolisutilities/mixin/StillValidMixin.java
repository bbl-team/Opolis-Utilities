package com.benbenlaw.opolisutilities.mixin;

import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = {
        ChestMenu.class,
        HopperMenu.class,
        DispenserMenu.class,
        AbstractFurnaceMenu.class,
        EnchantmentMenu.class,
        ItemCombinerMenu.class,
        BrewingStandMenu.class,
        BeaconMenu.class,
        InventoryMenu.class,
        CraftingMenu.class
})

public class StillValidMixin {


    @Inject(method = "stillValid(Lnet/minecraft/world/entity/player/Player;)Z", at = @At("HEAD"), cancellable = true)

    void stillValid(Player p_41431_, CallbackInfoReturnable<Boolean> cir) {

        if (p_41431_.getItemInHand(InteractionHand.MAIN_HAND).getItem().asItem() == ModItems.PORTABLE_GUI.get()) {
            cir.setReturnValue(true);
        }
    }



}
