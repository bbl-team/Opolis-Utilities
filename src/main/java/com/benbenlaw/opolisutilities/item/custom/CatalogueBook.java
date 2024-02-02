package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.screen.CatalogueMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CatalogueBook extends Item {
    public CatalogueBook(Properties p_41383_) {
        super(p_41383_);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide()) {
            player.openMenu(this.getContainer(level, player.blockPosition()));
        }

        return super.use(level, player, hand);
    }

    private MenuProvider getContainer(Level level, BlockPos blockPos) {
        return new SimpleMenuProvider((windowId, playerInventory, playerEntity) -> {
            return new CatalogueMenu(windowId, playerInventory, ContainerLevelAccess.create(level, blockPos)) {
                @Override
                public boolean stillValid(@NotNull Player player) {
                    return true;
                }
            };
        }, Component.literal(" Catalogue"));
    }
}

