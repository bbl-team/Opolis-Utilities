package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.block.custom.ResourceGenerator2Block;
import com.benbenlaw.opolisutilities.block.entity.custom.BlockBreakerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.BlockPlacerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.ItemRepairerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.ResourceGenerator2BlockEntity;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID)

public class HoverInWorldEvents {

    @OnlyIn(Dist.CLIENT)
    public static void renderEnderScramblerInfo(GuiGraphics graphics, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        assert player != null;
        Level level = player.level();

        if (player.getMainHandItem().is(ModItems.OPOLIS_WRENCH.get()) || player.getOffhandItem().is(ModItems.OPOLIS_WRENCH.get())) {

            if (minecraft.screen == null) {

                HitResult hitResult = minecraft.hitResult;
                if (hitResult instanceof BlockHitResult hit) {
                    BlockPos blockPos = hit.getBlockPos();
                    BlockState blockState = minecraft.level.getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    ItemStack lookingAtBlock = block.getCloneItemStack(minecraft.level, blockPos, blockState);
                    Window window = minecraft.getWindow();
                    int x = window.getGuiScaledWidth() / 2 + 3;
                    int y = window.getGuiScaledHeight() / 2 + 3;

                    if (lookingAtBlock.is(ModBlocks.ENDER_SCRAMBLER.get().asItem())) {

                        int currentRange = blockState.getValue(EnderScramblerBlock.SCRAMBLER_RANGE);
                        boolean isRunning = blockState.getValue(EnderScramblerBlock.POWERED);

                        graphics.drawCenteredString(minecraft.font, "Range: " + currentRange, x, y + 3, 0xFFFFFF);
                        if (isRunning) {
                            graphics.drawCenteredString(minecraft.font, "Teleporting Not Allowed!", x, y + 15, 0xFFFFFF);
                        }
                        if (!isRunning) {
                            graphics.drawCenteredString(minecraft.font, "Teleporting Allowed!", x, y + 15, 0xFFFFFF);
                        }

                        graphics.pose().pushPose();
                        graphics.pose().scale(0.75F, 0.75F, 1F);
                        graphics.pose().popPose();

                    }

                    if (lookingAtBlock.is(ModBlocks.BLOCK_BREAKER.get().asItem())) {

                        BlockBreakerBlockEntity entity = (BlockBreakerBlockEntity) level.getBlockEntity(blockPos);

                        if (entity != null) {
                            boolean isWhiteList = !entity.getItemStackHandler().getStackInSlot(1).isEmpty();
                            Item whiteListItem = entity.getItemStackHandler().getStackInSlot(1).getItem();
                            String translatedWhiteListItem = whiteListItem.getName(whiteListItem.getDefaultInstance()).getString();

                            boolean isBlackList = !entity.getItemStackHandler().getStackInSlot(2).isEmpty();
                            Item blackListItem = entity.getItemStackHandler().getStackInSlot(2).getItem();
                            String translatedBlackListItem = blackListItem.getName(blackListItem.getDefaultInstance()).getString();

                            Item currentTool = entity.getItemStackHandler().getStackInSlot(0).getItem();
                            String translatedTool = currentTool.getName(currentTool.getDefaultInstance()).getString();

                            int toolDurabilityTaken = entity.getItemStackHandler().getStackInSlot(0).getDamageValue();
                            int toolDurabilityRemaining = entity.getItemStackHandler().getStackInSlot(0).getMaxDamage() - toolDurabilityTaken;

                            if(!translatedTool.equals("Air")) {
                                graphics.drawCenteredString(minecraft.font, "Current Tool: " + translatedTool + " with " + toolDurabilityRemaining + " durability", x, y + 3, 0xFFFFFF);
                            }

                            if(translatedTool.equals("Air")) {
                                graphics.drawCenteredString(minecraft.font, "Current Tool: No Tool", x, y + 3, 0xFFFFFF);
                            }

                            if (isWhiteList) {
                                graphics.drawCenteredString(minecraft.font, "Will only break " + translatedWhiteListItem, x, y + 15, 0xFFFFFF);
                            }
                            if (isBlackList) {
                                graphics.drawCenteredString(minecraft.font, "Will break any block except " + translatedBlackListItem, x, y + 15, 0xFFFFFF);
                            }

                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();

                        }
                    }

                    if (lookingAtBlock.is(ModBlocks.BLOCK_PLACER.get().asItem())) {

                        BlockPlacerBlockEntity entity = (BlockPlacerBlockEntity) level.getBlockEntity(blockPos);

                        if (entity != null) {
                            Item itemPlaced = entity.getItemStackHandler().getStackInSlot(0).getItem();
                            String translatedPlaced  = itemPlaced.getName(itemPlaced.getDefaultInstance()).getString();

                            if (!translatedPlaced.equals("Air")) {
                                graphics.drawCenteredString(minecraft.font, "Placing: " + translatedPlaced , x, y + 3, 0xFFFFFF);
                            }
                            if (translatedPlaced.equals("Air")) {
                                graphics.drawCenteredString(minecraft.font, "Placing: Nothing" , x, y + 3, 0xFFFFFF);
                            }
                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();

                        }
                    }

                    if (lookingAtBlock.is(ModBlocks.ITEM_REPAIRER.get().asItem())) {

                        ItemRepairerBlockEntity entity = (ItemRepairerBlockEntity) level.getBlockEntity(blockPos);

                        if (entity != null) {
                            Item itemBeingRepaired = entity.getItemStackHandler().getStackInSlot(0).getItem();
                            String translatedWhiteListItem = itemBeingRepaired.getName(itemBeingRepaired.getDefaultInstance()).getString();

                            Item itemRepaired = entity.getItemStackHandler().getStackInSlot(1).getItem();
                            String translatedRepaired  = itemRepaired.getName(itemRepaired.getDefaultInstance()).getString();

                            int toolDurabilityTaken = entity.getItemStackHandler().getStackInSlot(0).getDamageValue();

                            if (!translatedWhiteListItem.equals("Air")) {
                                graphics.drawCenteredString(minecraft.font, "Repairing: " + translatedWhiteListItem + " " + toolDurabilityTaken + " to go ", x, y + 3, 0xFFFFFF);
                            }
                            if (translatedWhiteListItem.equals("Air")) {
                                graphics.drawCenteredString(minecraft.font, "Put items in the top slot to be repaired" , x, y + 3, 0xFFFFFF);
                            }
                            if (!translatedRepaired.equals("Air")) {
                                graphics.drawCenteredString(minecraft.font, "Repaired: " + translatedRepaired, x, y + 15, 0xFFFFFF);
                            }
                            if (translatedRepaired.equals("Air")) {
                                graphics.drawCenteredString(minecraft.font, "No Items repaired " , x, y + 15, 0xFFFFFF);
                            }

                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();

                        }
                    }

                    if (lookingAtBlock.is(ModBlocks.RESOURCE_GENERATOR_2.get().asItem())) {

                        ResourceGenerator2BlockEntity entity = (ResourceGenerator2BlockEntity) level.getBlockEntity(blockPos);

                        if (entity != null) {

                            graphics.drawCenteredString(minecraft.font, "Right Click for more Information", x, y + 3, 0xFFFFFF);

                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();

                        }
                    }
                }
            }
        }
    }
}

