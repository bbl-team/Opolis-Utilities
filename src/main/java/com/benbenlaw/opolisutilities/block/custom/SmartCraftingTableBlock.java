package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.casting.screen.SolidifierMenu;
import com.benbenlaw.opolisutilities.screen.custom.SmartCraftingMenu;
import com.benbenlaw.opolisutilities.screen.custom.SmartCraftingScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
@Deprecated(since = "4.12.2", forRemoval = true)
public class SmartCraftingTableBlock extends Block {
    public SmartCraftingTableBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {

            ContainerData data = new SimpleContainerData(2);

            player.openMenu(new SimpleMenuProvider(
                    (windowId, playerInventory, playerEntity) -> new SmartCraftingMenu(windowId, playerInventory, pos, data),
                    Component.literal("Smart Crafting Table")), (buf -> buf.writeBlockPos(pos)));

        }
        return InteractionResult.SUCCESS;
    }
}