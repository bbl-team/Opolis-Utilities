package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.block.entity.custom.CatalogueBlockEntity;
import com.benbenlaw.opolisutilities.screen.CatalogueMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class CatalogueBook extends Item {
    public CatalogueBook(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {

        BlockPos blockPos = player.blockPosition();

        if (!level.isClientSide()) {

            CatalogueBlockEntity catalogueBlockEntity = (CatalogueBlockEntity) level.getBlockEntity(blockPos);

            if (catalogueBlockEntity instanceof CatalogueBlockEntity) {
                ContainerData data = catalogueBlockEntity.data;
                player.openMenu(new SimpleMenuProvider(
                        (windowId, playerInventory, playerEntity) -> new CatalogueMenu(windowId, playerInventory, blockPos, data),
                        Component.translatable("block.opolisutilities.catalogue")), (buf -> buf.writeBlockPos(blockPos)));
            }
        }

        return super.use(level, player, hand);
    }



}

