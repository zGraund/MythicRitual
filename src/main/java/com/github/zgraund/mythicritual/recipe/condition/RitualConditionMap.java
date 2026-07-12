package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;

public class RitualConditionMap {
    public static final MapCodec<RitualConditionMap> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            RitualCondition.CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(RitualConditionMap::asList)
    ).apply(inst, RitualConditionMap::new));

    private final HashMap<RitualConditionKey<?>, RitualCondition> conditions = new LinkedHashMap<>();

    private RitualConditionMap(@Nonnull List<RitualCondition> conditions) {
        conditions.stream()
                  .sorted(Comparator.comparing(RitualCondition::order))
                  .forEach(condition -> this.conditions.put(condition.key(), condition));
    }

    public boolean test(RitualRecipeContext context) {
        return this.conditions.values().stream().allMatch(condition -> condition.test(context));
    }

    public <T extends RitualCondition> Optional<T> get(@Nonnull RitualConditionKey<T> key) {
        return Optional.ofNullable(key.type().cast(this.conditions.get(key)));
    }

    public Optional<Component> getDescriptions() {
        List<Component> lines =
                this.conditions.values()
                               .stream()
                               .map(RitualCondition::getDescription)
                               .filter(Predicate.not(List::isEmpty))
                               .map(components -> ComponentUtils.formatList(components, CommonComponents.NEW_LINE))
                               .toList();
        if (lines.isEmpty()) return Optional.empty();
        return Optional.of(ComponentUtils.formatList(lines, Component.literal("\n\n")));
    }

    public List<RitualCondition> asList() {
        return conditions.values().stream().toList();
    }

    @Nonnull
    @Contract(" -> new")
    public static Builder builder() {return new Builder();}

    @Override
    public String toString() {
        return "RitualConditionMap{" +
               "conditions=" + conditions +
               '}';
    }

    public static class Builder {
        private final List<RitualCondition> conditions = new ArrayList<>();

        public void addCondition(RitualCondition condition) {
            conditions.add(condition);
        }

        public RitualConditionMap build() {return new RitualConditionMap(conditions);}
    }
}
