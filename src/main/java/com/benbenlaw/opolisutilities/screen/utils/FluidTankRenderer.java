package com.benbenlaw.opolisutilities.screen.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.List;

public class FluidTankRenderer {
    public static final FluidTankRenderer INSTANCE = new FluidTankRenderer(FluidType.BUCKET_VOLUME, 16, 16, 16);

    private static final int TEX_WIDTH = 16;
    private static final int TEX_HEIGHT = 16;

    private final int capacityMb;
    private final int width;
    private final int height;
    private final int minHeight;

    public FluidTankRenderer(int capacityMb, int width, int height, int minHeight) {
        this.capacityMb = capacityMb;
        this.width = width;
        this.height = height;
        this.minHeight = minHeight;
    }

    private static TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack) {
        Minecraft minecraft = Minecraft.getInstance();
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions attributes = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation fluidStill = attributes.getStillTexture(fluidStack);
        return minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();

        // Calculate the adjusted uMax and vMax based on the mask values
        uMax = uMin + ((16 - maskRight) / 16.0f) * (uMax - uMin);
        vMax = vMin + ((16 - maskTop) / 16.0f) * (vMax - vMin);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        bufferBuilder.addVertex(matrix, xCoord, yCoord + 16 - maskTop, zLevel).setUv(uMin, vMax);
        bufferBuilder.addVertex(matrix, xCoord + 16 - maskRight, yCoord + 16 - maskTop, zLevel).setUv(uMax, vMax);
        bufferBuilder.addVertex(matrix, xCoord + 16 - maskRight, yCoord, zLevel).setUv(uMax, vMin);
        bufferBuilder.addVertex(matrix, xCoord, yCoord, zLevel).setUv(uMin, vMin);

    }

    public void render(GuiGraphics graphics, final int xPosition, final int yPosition, @Nullable FluidStack fluidStack) {
        RenderSystem.enableBlend();
        drawFluid(graphics.pose(), xPosition, yPosition, fluidStack);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    private void drawFluid(PoseStack poseStack, final int xPosition, final int yPosition, @Nullable FluidStack fluidStack) {
        if (fluidStack == null) {
            return;
        }
        Fluid fluid = fluidStack.getFluid();
        if (fluid == null) {
            return;
        }

        TextureAtlasSprite fluidStillSprite = getStillFluidSprite(fluidStack);

        IClientFluidTypeExtensions attributes = IClientFluidTypeExtensions.of(fluid);
        int fluidColor = attributes.getTintColor(fluidStack);

        int amount = fluidStack.getAmount();
        int scaledAmount = (amount * height) / capacityMb;
        if (amount > 0 && scaledAmount < minHeight) {
            scaledAmount = minHeight;
        }
        if (scaledAmount > height) {
            scaledAmount = height;
        }

        drawTiledSprite(poseStack, xPosition, yPosition, width, height, fluidColor, scaledAmount, fluidStillSprite);
    }

    private void drawTiledSprite(PoseStack poseStack, final int xPosition, final int yPosition, final int tiledWidth, final int tiledHeight, int color, int scaledAmount, TextureAtlasSprite sprite) {
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Matrix4f matrix = poseStack.last().pose();
        setGLColorFromInt(color);

        final int xTileCount = tiledWidth / TEX_WIDTH;
        final int xRemainder = tiledWidth - (xTileCount * TEX_WIDTH);
        final int yTileCount = scaledAmount / TEX_HEIGHT;
        final int yRemainder = scaledAmount - (yTileCount * TEX_HEIGHT);

        final int yStart = yPosition + tiledHeight;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = (xTile == xTileCount) ? xRemainder : TEX_WIDTH;
                int height = (yTile == yTileCount) ? yRemainder : TEX_HEIGHT;
                int x = xPosition + (xTile * TEX_WIDTH);
                int y = yStart - ((yTile + 1) * TEX_HEIGHT);
                if (width > 0 && height > 0) {
                    int maskTop = TEX_HEIGHT - height;
                    int maskRight = TEX_WIDTH - width;

                    drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
                }
            }
        }
    }

    public List<Component> getTooltips(FluidStack fluidStack) {
        return List.of(Component.literal(fluidStack.getAmount() + " / " + capacityMb + " mB"));
    }
}
