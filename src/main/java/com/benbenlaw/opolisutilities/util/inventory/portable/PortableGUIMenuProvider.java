package com.benbenlaw.opolisutilities.util.inventory.portable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PortableGUIMenuProvider implements MenuProvider {
    private final MenuProvider originalProvider;
    private final BlockPos blockPos;

    public PortableGUIMenuProvider(MenuProvider originalProvider, BlockPos blockPos) {
        this.originalProvider = originalProvider;
        this.blockPos = blockPos;
    }

    @Override
    public Component getDisplayName() {
        return originalProvider.getDisplayName();  // Delegate to the original provider
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        AbstractContainerMenu originalMenu = originalProvider.createMenu(containerId, playerInventory, player);

        // Return a custom container menu that overrides 'stillValid'
        return new PortableGUIContainerMenu(originalMenu, blockPos, player);
    }
}