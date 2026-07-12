package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// FIXME: can't really use an ItemPredicate here since i need a single count and not a range
public record Catalyst(ItemPredicate items, Optional<String> description) implements RitualCondition {
    public static final ItemPredicate EMPTY_HAND = ItemPredicate.Builder.item().of(Items.AIR).build();
    private static final String EMPTY_HAND_KEY = "empty_hand";
    public static final ItemPredicate ANY = ItemPredicate.Builder.item().build();
    public static final MapCodec<Catalyst> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            NeoForgeExtraCodecs.withAlternative(
                    Codec.STRING.flatXmap(
                            string -> string.equals(EMPTY_HAND_KEY)
                                    ? DataResult.success(EMPTY_HAND)
                                    : DataResult.error(() -> "Key must be " + EMPTY_HAND_KEY),
                            item -> item == EMPTY_HAND
                                    ? DataResult.success(EMPTY_HAND_KEY)
                                    : DataResult.error(() -> "Invalid ItemPredicate in Catalyst condition")),
                    ItemPredicate.CODEC
            ).optionalFieldOf("value", ANY).forGetter(Catalyst::items),
            DESCRIPTION_CODEC.forGetter(Catalyst::description)
    ).apply(inst, Catalyst::new));

    @Override
    public boolean test(@Nonnull RitualRecipeContext context) {
        return items.test(context.catalyst());
    }

    @Nonnull
    @Override
    public List<Component> getDescription() {
        List<Component> lines = new ArrayList<>();
        String key = "info.hover.ritual.condition.catalyst";
        if (items.items().isEmpty() || items.items().get().size() == 0) {
            lines.add(Component.translatable(key, Component.translatable(key + ".any").withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
        } else if (items == EMPTY_HAND) {
            lines.add(Component.translatable(key, Component.translatable(key + ".empty").withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
        }
        description.ifPresent(string -> lines.add(Component.literal(string).withStyle(ChatFormatting.GRAY)));
        return lines;
    }

//    public List<ItemStack> getItems() {
//        return items.items().map(holders -> holders.stream().map(holder -> new ItemStack(holder,items.count().)).toList()).orElse(List.of());
//    }

    @Override
    public int order() {return 5;}

    @Override
    public RitualConditionKey<Catalyst> key() {
        return RitualConditionKey.CATALYST;
    }

    @Override
    public MapCodec<? extends RitualCondition> type() {
        return CODEC;
    }
}
