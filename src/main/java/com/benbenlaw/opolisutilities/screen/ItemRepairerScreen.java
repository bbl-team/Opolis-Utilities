package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.custom.ItemRepairerBlock;
import com.benbenlaw.opolisutilities.networking.ModMessages;
import com.benbenlaw.opolisutilities.networking.packets.PacketItemRepairerOnOffButton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ItemRepairerScreen extends AbstractContainerScreen{
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/drying_table_gui.png");

    private static final ResourceLocation ON_BUTTON =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/crafter_on_button.png");

    private static final ResourceLocation OFF_BUTTON =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/crafter_off_button.png");


    public ItemRepairerScreen(ItemRepairerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        /*

        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 102, y + 41, 176, 0, 8, menu.getScaledProgress());
        }

         */


    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
    }
     /*   renderBackground(guiGraphics);

        //Power Button
        if (this.menu.blockEntity.getBlockState().getValue(ItemRepairerBlock.POWERED)) {
            this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, ON_BUTTON, (p_289630_) -> {

                ModMessages.sendToServer(new PacketItemRepairerOnOffButton(this.menu.blockEntity.getBlockPos()));


                p_289630_.setPosition(this.leftPos + 5, this.height / 2 - 49);
            }));
        }

        else {
            this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, OFF_BUTTON, (p_289630_) -> {

                ModMessages.sendToServer(new PacketItemRepairerOnOffButton(this.menu.blockEntity.getBlockPos()));
                p_289630_.setPosition(this.leftPos + 5, this.height / 2 - 49);
            }));
        }




        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }


      */



}
