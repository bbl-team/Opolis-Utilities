package com.benbenlaw.opolisutilities.block.entity.client;

import com.benbenlaw.opolisutilities.block.entity.custom.SummoningBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class SummoningBlockEntityRenderer implements BlockEntityRenderer<SummoningBlockEntity> {

    private final EntityRenderDispatcher entityRenderer;

    public SummoningBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher();
    }

    @Override
    public void render(SummoningBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.75f, 0.5f);
        pPoseStack.pushPose();
        pPoseStack.translate(0.0f, 0.0f, 0.1875f);
        float scaledProgress = pBlockEntity.getScaledProgress();
        pPoseStack.scale(scaledProgress, scaledProgress, scaledProgress);

        // Adjust this value to move the entity back along the Z-axis
        pPoseStack.translate(0.0f, 0.0f, -0.2f);

        // Adjust this value to move the entity up along the Y-axis
        pPoseStack.translate(0.0f, 0.25f, 0.0f);

        Entity entity = pBlockEntity.getEntity();
        if (entity != null) {
            entityRenderer.render(entity, 0.0d, 0.0d, 0.0d,
                    0.0f, 0.0f, pPoseStack, pBufferSource, pPackedLight);
        }
        pPoseStack.popPose();
        pPoseStack.popPose();
    }


    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}