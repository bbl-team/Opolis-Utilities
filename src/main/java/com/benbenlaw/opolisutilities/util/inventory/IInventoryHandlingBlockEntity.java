package com.benbenlaw.opolisutilities.util.inventory;

import net.neoforged.neoforge.items.ItemStackHandler;

public interface IInventoryHandlingBlockEntity {
    void setHandler(ItemStackHandler handler);
    ItemStackHandler getItemStackHandler();
}