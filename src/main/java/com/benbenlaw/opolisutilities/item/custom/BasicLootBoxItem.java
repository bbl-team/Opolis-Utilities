package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.loot.ModLootTables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;
import java.util.Objects;


public class BasicLootBoxItem extends Item {

    public BasicLootBoxItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {

            LootParams lootparams = (new LootParams.Builder((ServerLevel) player.level())).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.ORIGIN, player.position()).create(LootContextParamSets.GIFT);
            LootTable table = Objects.requireNonNull(level.getServer()).reloadableRegistries().getLootTable(ModLootTables.BASIC_LOOT_BOX);

            List<ItemStack> loot = table.getRandomItems(lootparams);
            for (ItemStack stack : loot) {
                GiveItem(player, stack);
            }
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
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
