package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID)

public class HoverInWorldEvents {

    public static void renderEnderScramblerInfo(GuiGraphics graphics, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        assert player != null;
        if (player.getMainHandItem().is(ModItems.ENDER_SCRAMBLER_CONFIGURATOR.get()) || player.getOffhandItem().is(ModItems.ENDER_SCRAMBLER_CONFIGURATOR.get())) {

        if (minecraft.screen == null) {

            HitResult hitResult = minecraft.hitResult;
            if (hitResult instanceof BlockHitResult hit) {
                BlockPos blockPos = hit.getBlockPos();
                BlockState blockState = minecraft.level.getBlockState(blockPos);
                Block block = blockState.getBlock();
                ItemStack picked = block.getCloneItemStack(minecraft.level, blockPos, blockState);

                if (picked.is(ModBlocks.ENDER_SCRAMBLER.get().asItem())) {

                    Window window = minecraft.getWindow();
                    int x = window.getGuiScaledWidth() / 2 + 3;
                    int y = window.getGuiScaledHeight() / 2 + 3;
                    int currentRange = blockState.getValue(EnderScramblerBlock.SCRAMBLER_RANGE);
                    boolean isRunning = blockState.getValue(EnderScramblerBlock.POWERED);

                    graphics.pose().pushPose();
                    graphics.pose().translate(0, 0, 10);
                    graphics.pose().scale(0.5F, 0.5F, 1);
                    graphics.pose().popPose();

                    graphics.drawString(minecraft.font, "Range: " + currentRange, x - 18, y + 3, 0xFFFFFF, false);
                    if (isRunning) {
                        graphics.drawString(minecraft.font, "Teleporting Not Allowed!", x - 18, y + 15, 0xFFFFFF, false);
                    }
                    if (!isRunning) {
                        graphics.drawString(minecraft.font, "Teleporting Allowed!", x - 18, y + 15, 0xFFFFFF, false);
                    }
                    graphics.pose().pushPose();
                    graphics.pose().scale(0.75F, 0.75F, 1F);
                    graphics.pose().popPose();


                }
            }
            }
        }


    }
}


