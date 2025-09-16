package com.github.zgraund.mythicritual.attachment;

import com.github.zgraund.mythicritual.MythicRitual;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MythicRitual.MOD_ID);

    public static final Supplier<AttachmentType<Boolean>> IS_SACRIFICED =
            TYPES.register("is_sacrificed", () -> AttachmentType.builder(() -> false).build());

    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
    }
}
