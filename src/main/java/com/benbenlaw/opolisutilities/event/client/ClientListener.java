package com.benbenlaw.opolisutilities.event.client;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.client.DryingTableBlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = OpolisUtilities.MOD_ID, bus = EventBusSubscriber.Bus.MOD ,value = Dist.CLIENT)
public class ClientListener {

    //Drying Table Item Renderer
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.DRYING_TABLE_BLOCK_ENTITY.get(),
                DryingTableBlockEntityRenderer::new);

    }
}
