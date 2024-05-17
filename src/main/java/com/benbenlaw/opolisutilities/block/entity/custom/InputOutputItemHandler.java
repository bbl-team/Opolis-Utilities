package com.benbenlaw.opolisutilities.block.entity.custom;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class InputOutputItemHandler  implements IItemHandlerModifiable {
    private final IItemHandlerModifiable handler;
    private final BiPredicate<Integer, ItemStack> canInput;
    private final Predicate<Integer> canOutput;

    public InputOutputItemHandler(IItemHandlerModifiable handler, BiPredicate<Integer, ItemStack> canInput, Predicate<Integer> canOutput) {
        this.handler = handler;
        this.canInput = canInput;
        this.canOutput = canOutput;
    }

    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.handler.setStackInSlot(slot, stack);
    }

    public @NotNull ItemStack getStackInSlot(int slot) {
        return this.handler.getStackInSlot(slot);
    }

    public int getSlots() {
        return this.handler.getSlots();
    }

    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return this.canInput.test(slot, stack) ? this.handler.insertItem(slot, stack, simulate) : stack;
    }

    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.canOutput.test(slot) ? this.handler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    public int getSlotLimit(int slot) {
        return this.handler.getSlotLimit(slot);
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.canInput.test(slot, stack) && this.handler.isItemValid(slot, stack);
    }
}
