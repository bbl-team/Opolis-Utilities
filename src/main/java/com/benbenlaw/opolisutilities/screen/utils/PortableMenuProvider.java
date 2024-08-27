package com.benbenlaw.opolisutilities.screen.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class PortableMenuProvider implements MenuProvider {
    private final MenuProvider originalProvider;
    private final Player player;

    public PortableMenuProvider(MenuProvider originalProvider, Player player) {
        this.originalProvider = originalProvider;
        this.player = player;
    }

    @Override
    public Component getDisplayName() {
        return originalProvider.getDisplayName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        AbstractContainerMenu originalMenu = originalProvider.createMenu(containerId, playerInventory, player);

        // Wrap the original container with your custom PortableContainer
        return new PortableContainer(originalMenu);
    }
}
