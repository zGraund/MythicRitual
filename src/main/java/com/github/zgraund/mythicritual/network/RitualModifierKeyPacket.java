package com.github.zgraund.mythicritual.network;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.registry.MRAttachmentTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nonnull;

public record RitualModifierKeyPacket(boolean pressed) implements CustomPacketPayload {
    public static final Type<RitualModifierKeyPacket> TYPE = new Type<>(MythicRitual.asResource("ritual_modifier"));
    public static final StreamCodec<ByteBuf, RitualModifierKeyPacket> STREAM_CODEC =
            ByteBufCodecs.BOOL.map(RitualModifierKeyPacket::new, RitualModifierKeyPacket::pressed);

    public static void handleOnServer(@Nonnull RitualModifierKeyPacket packet, @Nonnull IPayloadContext context) {
        context.player().setData(MRAttachmentTypes.IS_TRYING_RITUAL, packet.pressed());
    }

    @Nonnull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
