package com.benbenlaw.opolisutilities.util;

import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PortableGUIMenuProvider implements MenuProvider {

    private final MenuProvider menuProvider;

    public PortableGUIMenuProvider(MenuProvider menuProvider) {
        this.menuProvider = menuProvider;
    }

    @Override
    public Component getDisplayName() {
        return menuProvider.getDisplayName(); // Use the original display name
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        AbstractContainerMenu menu = menuProvider.createMenu(id, inventory, player);

        return new AbstractContainerMenu(menu.getType(), id) {
            @Override
            public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
                return menu.quickMoveStack(p_38941_, p_38942_);
            }

            @Override
            public boolean stillValid(Player player) {
                return true;
            }
        };
    }
}
