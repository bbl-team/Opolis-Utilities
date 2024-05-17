package com.benbenlaw.opolisutilities.util;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, OpolisUtilities.MOD_ID);

    private static final Supplier<AttachmentType<ItemStackHandler>> HANDLER = ATTACHMENT_TYPES.register(
            "handler", () -> AttachmentType.serializable(() -> new ItemStackHandler(1)).build());



    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }



}
