package com.github.zgraund.mythicritual.registry;

import com.github.zgraund.mythicritual.util.RegistryUtils;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MRAttachmentTypes {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            RegistryUtils.create(NeoForgeRegistries.ATTACHMENT_TYPES);

    public static final Supplier<AttachmentType<Boolean>> IS_TRYING_RITUAL = ATTACHMENT_TYPES.register(
            "is_trying_ritual", () -> AttachmentType.builder(() -> false).sync(ByteBufCodecs.BOOL).build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
