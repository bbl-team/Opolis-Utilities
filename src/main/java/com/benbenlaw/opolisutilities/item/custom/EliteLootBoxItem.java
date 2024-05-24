package com.benbenlaw.opolisutilities.item.custom;

import net.minecraft.world.item.Item;


public class EliteLootBoxItem extends Item {

    public EliteLootBoxItem(Properties pProperties) {
        super(pProperties);

   
    }

    /*

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            LootParams lootparams = (new LootParams.Builder((ServerLevel) player.level())).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.ORIGIN, player.position()).create(LootContextParamSets.GIFT);
            LootTable table = Objects.requireNonNull(world.getServer()).getLootData().getLootTable((ModLootTables.ELITE_LOOT_BOX));

            List<ItemStack> loot = table.getRandomItems(lootparams);
            for (ItemStack stack : loot) {
                GiveItem(player, stack);
            }
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide());
    }

    public boolean GiveItem(Player player, ItemStack itemStack) {
        if (player.getInventory().getFreeSlot() >= 0) {
            player.addItem(itemStack);
            return true;
        } else {
            player.drop(itemStack, true);
            return false;
        }
    }


     */

}
