package com.benbenlaw.opolisutilities.util.inventory;

import net.minecraftforge.items.ItemStackHandler;

public interface IInventoryHandlingBlockEntity {
    void setHandler(ItemStackHandler handler);
    ItemStackHandler getItemStackHandler();
}