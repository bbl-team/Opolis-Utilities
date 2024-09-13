package com.benbenlaw.opolisutilities.screen.custom;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.networking.payload.ClearTankPayload;
import com.benbenlaw.opolisutilities.screen.utils.FluidStackWidget;
import com.benbenlaw.opolisutilities.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class FluidGeneratorScreen extends AbstractContainerScreen<FluidGeneratorMenu> {

    Level level;

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/fluid_generator_gui.png");

    public FluidGeneratorScreen(FluidGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.level = pMenu.level;
    }

    @Override
    protected void init() {
        super.init();
        addFluidWidgets();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;


        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 84, y + 35, 176, 0, 8, menu.getScaledProgress());
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderTickRate(guiGraphics, mouseX, mouseY, x, y);
        renderWarning(guiGraphics, mouseX, mouseY);
    }

    private void renderTickRate(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 116, 16, 16, 16)) {
            guiGraphics.drawString(this.font, this.menu.blockEntity.maxProgress + " ticks", this.leftPos + 120,
                    this.topPos + 68, 0x3F3F3F, false);
        }
    }

    private void addFluidWidgets() {
        addRenderableOnly(new FluidStackWidget(this, getMenu().blockEntity.FLUID_TANK, leftPos + 14, topPos + 15, 14, 56));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean handled = super.mouseClicked(mouseX, mouseY, mouseButton);

        int tankX = leftPos + 14;
        int tankY = topPos + 15;
        int tankWidth = 14;
        int tankHeight = 56;

        if (MouseUtil.isMouseOver(mouseX, mouseY, tankX, tankY, tankWidth, tankHeight)) {

            int tank = this.menu.blockEntity.FLUID_TANK.getTanks();

            boolean hasShiftDown = FluidGeneratorScreen.hasShiftDown();
            PacketDistributor.sendToServer(new ClearTankPayload(menu.blockEntity.getBlockPos(), hasShiftDown, tank));

        }
        return handled;
    }
    private void renderWarning(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        int tankX = leftPos + 14;
        int tankY = topPos + 15;
        int tankWidth = 14;
        int tankHeight = 56;

        if (MouseUtil.isMouseOver(mouseX, mouseY, tankX, tankY, tankWidth, tankHeight) && FluidGeneratorScreen.hasShiftDown()) {

            guiGraphics.renderTooltip(this.font, Component.translatable("screen.opolisutilities.warning").withStyle(ChatFormatting.RED), mouseX, mouseY - 14);
        }
    }

}

