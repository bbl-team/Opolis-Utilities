package com.benbenlaw.opolisutilities.util.inventory.portable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class PortableGUIContainerMenu extends AbstractContainerMenu {
    private final AbstractContainerMenu originalMenu;
    private final BlockPos blockPos;
    private final Player player;

    public PortableGUIContainerMenu(AbstractContainerMenu originalMenu, BlockPos blockPos, Player player) {
        super(originalMenu.getType(), originalMenu.containerId);  // Keep the same type and container ID
        this.originalMenu = originalMenu;
        this.blockPos = blockPos;
        this.player = player;

        // Copy slots from the original menu by adding them to the custom container
        for (int i = 0; i < originalMenu.slots.size(); i++) {
            this.addSlot(originalMenu.slots.get(i));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        // Override to always return true or allow a custom distance check if necessary
        return true;  // Always valid, so it ignores the normal range restrictions
    }



    // Delegate other methods to the original menu as needed
    @Override
    public void slotsChanged(Container container) {
        originalMenu.slotsChanged(container);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return originalMenu.quickMoveStack(player, index);  // Delegate to original menu logic
    }

    @Override
    public void removed(Player player) {
        originalMenu.removed(player);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        // Ensure this only runs server-side
        if (!player.level().isClientSide && player.level().getBlockEntity(blockPos) instanceof BlockEntity blockEntity) {
            blockEntity.setChanged();  // Mark block as changed

            // Send the block entity's data packet to the player manually
            ServerPlayer serverPlayer = (ServerPlayer) player;
            Packet<ClientGamePacketListener> updatePacket = blockEntity.getUpdatePacket();

            if (updatePacket != null) {
                serverPlayer.connection.send(updatePacket);  // Send block entity data to the client
            }
        }
    }

    @Override
    public void sendAllDataToRemote() {
        originalMenu.sendAllDataToRemote();
    }

    // You can override other methods as necessary to delegate functionality to the original menu
}
