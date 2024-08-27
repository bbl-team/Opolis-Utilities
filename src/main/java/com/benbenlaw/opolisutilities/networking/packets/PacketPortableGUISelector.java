package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.item.ModDataComponents;
import com.benbenlaw.opolisutilities.networking.payload.PortableGUISelectorPayload;
import com.benbenlaw.opolisutilities.networking.payload.SaveRecipePayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record PacketPortableGUISelector() {

    public static final PacketPortableGUISelector INSTANCE = new PacketPortableGUISelector();

    public static PacketPortableGUISelector get() {
        return INSTANCE;
    }

    public void handle(final PortableGUISelectorPayload payload, IPayloadContext context) {

        Player player = context.player();
        Level level = player.level();
        ItemStack itemStack = player.getInventory().getItem(payload.itemSlot());

        itemStack.set(ModDataComponents.LOCATION_VALUE, payload.location());

    //    player.sendSystemMessage(Component.literal("Location value: " + payload.location()));
    }
}