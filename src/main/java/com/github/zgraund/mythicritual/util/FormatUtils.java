package com.github.zgraund.mythicritual.util;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FormatUtils {
    public static Component minMaxBoundToComponent(@Nonnull MinMaxBounds<?> bounds) {
//        List<MutableComponent> list = new ArrayList<>();
//        bounds.min().map(Objects::toString).map(text -> Component.literal(text).withStyle(ChatFormatting.GREEN)).ifPresent(list::add);
//        bounds.max().map(Objects::toString).map(text -> Component.literal(text).withStyle(ChatFormatting.GREEN)).ifPresent(list::add);
//        return list.isEmpty() ? Component.literal("any").withStyle(ChatFormatting.GREEN) : ComponentUtils.formatList(list, Component.literal(" - "));
        List<String> list = new ArrayList<>();
        bounds.min().map(Objects::toString).ifPresent(list::add);
        bounds.max().map(Objects::toString).ifPresent(list::add);
        return list.isEmpty()
                ? Component.literal("Any").withStyle(ChatFormatting.GREEN)
                : ComponentUtils.formatList(list, Component.literal(" - "), s -> Component.literal(s).withStyle(ChatFormatting.GREEN));
    }
}
