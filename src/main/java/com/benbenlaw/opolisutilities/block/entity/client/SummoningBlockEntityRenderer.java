package com.benbenlaw.opolisutilities.block.entity.client;

import com.benbenlaw.opolisutilities.block.custom.SummoningBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.SummoningBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.joml.Vector3f;

public class SummoningBlockEntityRenderer implements BlockEntityRenderer<SummoningBlockEntity> {

    private final EntityRenderDispatcher entityRenderer;

    public SummoningBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher();
    }

    @Override
    public void render(SummoningBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 1.0f, 0.5f); // Start from the top center of the block

        float scaledProgress = pBlockEntity.getScaledProgress();

        // Scale the entity
        pPoseStack.scale(scaledProgress, scaledProgress, scaledProgress);

        // Adjust translation to keep the entity grounded during scaling
        pPoseStack.translate(0.0f, -0.5f * (1.0f - scaledProgress), 0.0f);

        // Adjust the direction the entity is facing
        Direction direction = pBlockEntity.getBlockState().getValue(SummoningBlock.FACING);
        float yaw = 0;
        switch (direction) {
            case NORTH:
                yaw = 180;
                break;
            case SOUTH:
                yaw = 0;
                break;
            case EAST:
                yaw = 90;
                break;
            case WEST:
                yaw = -90;
                break;
            default:
                break;
        }
        pPoseStack.mulPose(Axis.YP.rotationDegrees(yaw));

        Entity entity = pBlockEntity.getEntity();
        if (entity != null) {
            entityRenderer.render(entity, 0.0d, 0.0d, 0.0d,
                    0.0f, 0.0f, pPoseStack, pBufferSource, pPackedLight);
        }

        pPoseStack.popPose();
    }




    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}