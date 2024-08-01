package com.benbenlaw.opolisutilities.screen.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.custom.EnderScramblerBlockEntity;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.screen.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EnderScramblerMenu extends AbstractContainerMenu {
    protected EnderScramblerBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public EnderScramblerMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(0));
    }

    public EnderScramblerMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(ModMenuTypes.ENDER_SCRAMBLER_MENU.get(), containerID);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.blockEntity = (EnderScramblerBlockEntity) this.level.getBlockEntity(blockPos);
        this.data = data;

        checkContainerSize(inventory, 0);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        addDataSlots(data);
    }


    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return pPlayer.getInventory().getItem(pIndex);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {

        if (player.getItemInHand(player.getUsedItemHand()).is(ModItems.PORTABLE_GUI))
            return true;

        return stillValid(ContainerLevelAccess.create(player.level(), blockPos),
                player, ModBlocks.ENDER_SCRAMBLER.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
