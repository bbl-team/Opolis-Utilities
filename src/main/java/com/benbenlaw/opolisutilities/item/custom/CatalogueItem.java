package com.benbenlaw.opolisutilities.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CatalogueItem extends Item {
    public CatalogueItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide()) {
  //          openCatalogueMenu(player, hand);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    /*

    public static void openCatalogueMenu(Player player, InteractionHand hand) {
        NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return player.getItemInHand(hand).getHoverName();
            }

            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player playerInventory) {
         //       return new CatelogueMenu(windowId, inventory, hand);
         //   }
        }, buf -> buf.writeBoolean(hand == InteractionHand.MAIN_HAND));
    }

     */


}
