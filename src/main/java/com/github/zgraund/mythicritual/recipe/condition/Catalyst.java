package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Catalyst(Optional<ItemPredicate> items, Optional<String> description) implements RitualCondition {
    public static final ItemPredicate EMPTY_HAND = ItemPredicate.Builder.item().of(Items.AIR).build();
    public static final MapCodec<Catalyst> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            NeoForgeExtraCodecs.withAlternative(
                    Codec.STRING.flatXmap(
                            string -> string.equals("empty_hand")
                                    ? DataResult.success(EMPTY_HAND)
                                    : DataResult.error(() -> "String must be \"empty_hand\""),
                            item -> item == EMPTY_HAND
                                    ? DataResult.success("empty_hand")
                                    : DataResult.error(() -> "Predicate not EMPTY_HAND")),
                    ItemPredicate.CODEC
            ).optionalFieldOf("value").forGetter(Catalyst::items),
            DESCRIPTION_CODEC.forGetter(Catalyst::description)
    ).apply(inst, Catalyst::new));

    @Override
    public boolean test(RitualRecipeContext context) {
        return items.isEmpty() || items.get().test(context.catalyst());
    }

    @Nonnull
    @Override
    public List<Component> getDescription() {
        List<Component> lines = new ArrayList<>();
        String key = "info.hover.ritual.condition.catalyst";
        if (items.isEmpty()) {
            lines.add(Component.translatable(key, Component.translatable(key + ".any").withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
        } else if (items.get() == EMPTY_HAND) {
            lines.add(Component.translatable(key, Component.translatable(key + ".empty").withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
        }
        description.ifPresent(string -> lines.add(Component.literal(string).withStyle(ChatFormatting.GRAY)));
        return lines;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return RitualConditionKeys.CATALYST;
    }

    @Override
    public MapCodec<? extends RitualCondition> type() {
        return CODEC;
    }
}
