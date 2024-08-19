package com.benbenlaw.opolisutilities.block.entity.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;

@Deprecated
public interface OpolisBlockEntity {

    Component getDisplayName();

    AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player);


}
