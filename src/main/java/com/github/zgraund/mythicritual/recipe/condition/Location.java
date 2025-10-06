package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.FormatUtils;
import com.github.zgraund.mythicritual.util.predicate.ClientLocationPredicate;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Location(Optional<ClientLocationPredicate> location, Optional<String> description) implements RitualCondition {
    public static final MapCodec<Location> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ClientLocationPredicate.CODEC.codec().optionalFieldOf("value").forGetter(Location::location),
            DESCRIPTION_CODEC.forGetter(Location::description)
    ).apply(inst, Location::new));

    @Override
    public boolean test(@Nonnull RitualRecipeContext context) {
        return location.isEmpty() || location.get().matches(context.level(), context.origin());
    }

    @Nonnull
    @Override
    public List<Component> getDescription() {
        List<Component> lines = new ArrayList<>();
        description.ifPresent(string -> lines.add(Component.literal(string)));
        if (location.isPresent()) {
            location.get().biomes().ifPresent(biomes -> {
                List<Component> components =
                        biomes.stream()
                              .map(Holder::getRegisteredName)
                              .map(ResourceLocation::parse)
                              .map(id -> makeTranslationId("biome", id))
                              .toList();
                Component formatted = ComponentUtils.formatList(components, Component.literal(", "));
                lines.add(Component.translatable("info.hover.ritual.condition.location.biomes", formatted).withStyle(ChatFormatting.GRAY));
            });
            location.get().dimensions().ifPresent(dimensions -> {
                List<Component> components =
                        dimensions.stream()
                                  .map(ResourceKey::location)
                                  .map(id -> makeTranslationId("dimension", id))
                                  .toList();
                Component formatted = ComponentUtils.formatList(components, Component.literal(", "));
                lines.add(Component.translatable("info.hover.ritual.condition.location.dimensions", formatted).withStyle(ChatFormatting.GRAY));
            });
            location.get().canSeeSky().ifPresent(sky -> {
                String key = "info.hover.ritual.condition.sky";
                lines.add(Component.translatable(
                        key,
                        Component.translatable(sky ? key + ".open" : key + ".blocked").withStyle(sky ? ChatFormatting.GREEN : ChatFormatting.RED)
                ).withStyle(ChatFormatting.GRAY));
            });
            location.get().light().ifPresent(light -> lines.add(Component.translatable(
                            "info.hover.ritual.condition.location.light",
                            Component.literal(FormatUtils.minMaxBoundToString(light)).withStyle(ChatFormatting.RED)
                    ).withStyle(ChatFormatting.GRAY)
            ));
            location.get().smokey().ifPresent(smoke -> {
                String key = "info.hover.ritual.condition.location.smoke";
                lines.add(Component.translatable(
                                key,
                                Component.literal(smoke ? key + ".true" : key + ".false").withStyle(smoke ? ChatFormatting.GREEN : ChatFormatting.RED)
                        ).withStyle(ChatFormatting.GRAY)
                );
            });
        }
        return lines;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return RitualConditionKeys.LOCATION;
    }

    @Override
    public MapCodec<? extends RitualCondition> type() {
        return CODEC;
    }

    @Nonnull
    private static Component makeTranslationId(String type, ResourceLocation id) {
        return Component.translatableWithFallback(Util.makeDescriptionId(type, id), id.toString()).withStyle(ChatFormatting.GREEN);
    }
}
