package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.loot.ModLootTables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;


public class AdvancedLootBoxItem extends Item {

    public AdvancedLootBoxItem(Properties pProperties) {
        super(pProperties);

   
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            LootTable table = world.getServer().getLootTables().get(ModLootTables.ADVANCED_LOOT_BOX);
            LootContext generatedItems = (new LootContext.Builder((ServerLevel) world))
                    .withLuck(player.getLuck())
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .withParameter(LootContextParams.ORIGIN, player.position())
                    .create(LootContextParamSets.GIFT);
            List<ItemStack> loot = table.getRandomItems(generatedItems);
            for (int i = 0; i < loot.size(); i++) {
                GiveItem(player, loot.get(i));
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


}
