package com.benbenlaw.opolisutilities.screen.utils;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.item.custom.PortableGUIItem;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class PortableContainer extends AbstractContainerMenu {
    private final AbstractContainerMenu originalMenu;


    public PortableContainer(AbstractContainerMenu originalMenu) {
        super(originalMenu.getType(), originalMenu.containerId);
        this.originalMenu = originalMenu;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        // Always return true, ignoring the usual distance checks
        if (player.getItemInHand(player.getUsedItemHand()).getItem() instanceof PortableGUIItem) {
            return true;
        }
        return originalMenu.stillValid(player);
    }

    // Delegate other methods to originalMenu as needed
    @Override
    public void broadcastChanges() {
        originalMenu.broadcastChanges();
    }

    @Override
    public void slotsChanged(@NotNull Container inventory) {
        originalMenu.slotsChanged(inventory);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return originalMenu.quickMoveStack(player, index);
    }

    @Override
    public void removed(@NotNull Player player) {
        originalMenu.removed(player);
    }

}
