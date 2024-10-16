package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.block.entity.custom.BlockBreakerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.CatalogueBlockEntity;
import com.benbenlaw.opolisutilities.item.ModDataComponents;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.screen.custom.BlockBreakerMenu;
import com.benbenlaw.opolisutilities.screen.custom.CatalogueMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class CatalogueBook extends Item {
    public CatalogueBook(Properties p_41383_) {
        super(p_41383_);
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        assert player != null;
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide() && player.isCrouching()) {

            CatalogueBlockEntity catalogueBlockEntity = (CatalogueBlockEntity) level.getBlockEntity(blockPos);
            if (catalogueBlockEntity instanceof CatalogueBlockEntity && itemstack.getItem() instanceof CatalogueBook) {
                itemstack.set(ModDataComponents.INT_X, blockPos.getX());
                itemstack.set(ModDataComponents.INT_Y, blockPos.getY());
                itemstack.set(ModDataComponents.INT_Z, blockPos.getZ());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);


        if (!level.isClientSide()) {


            //MENU OPEN//
            if (itemstack.getItem() instanceof CatalogueBook) {

                if (itemstack.get(ModDataComponents.INT_X) != null && itemstack.get(ModDataComponents.INT_Y) != null && itemstack.get(ModDataComponents.INT_Z) != null) {
                    BlockPos blockPos = new BlockPos(itemstack.get(ModDataComponents.INT_X), itemstack.get(ModDataComponents.INT_Y), itemstack.get(ModDataComponents.INT_Z));

                    if (level.getBlockEntity(blockPos) instanceof CatalogueBlockEntity catalogueBlockEntity) {

                        level.getBlockEntity(blockPos);
                        ContainerData data = catalogueBlockEntity.data;
                        player.openMenu(new SimpleMenuProvider(
                                (windowId, playerInventory, playerEntity) -> new CatalogueMenu(windowId, playerInventory, blockPos, data),
                                Component.translatable("block.opolisutilities.catalogue")), (buf -> buf.writeBlockPos(blockPos)));

                        return InteractionResultHolder.success(itemstack);
                    } else {
                        player.sendSystemMessage(Component.translatable("tooltips.catalogue_book.no_catalogue").withStyle(ChatFormatting.RED));

                    }

                } else {
                    player.sendSystemMessage(Component.translatable("tooltips.catalogue_book.no_catalogue").withStyle(ChatFormatting.RED));
                }
            }

        }
        return InteractionResultHolder.fail(itemstack);
    }


    //Tooltip

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, java.util.List<Component> components, TooltipFlag flag) {

        if (Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.portable_gui.shift.held")
                    .withStyle(ChatFormatting.GREEN));
            components.add(Component.literal("X: " + stack.get(ModDataComponents.INT_X))
                    .withStyle(ChatFormatting.GREEN));
            components.add(Component.literal("Y: " + stack.get(ModDataComponents.INT_Y))
                    .withStyle(ChatFormatting.GREEN));
            components.add(Component.literal("Z: " + stack.get(ModDataComponents.INT_Z))
                    .withStyle(ChatFormatting.GREEN));
        } else {
            components.add(Component.translatable("tooltips.portable_gui.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(stack, tooltipContext, components, flag);
    }

}

