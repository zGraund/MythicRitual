package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.util.*;

public class RitualConditionMap {
    public static final MapCodec<RitualConditionMap> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            RitualCondition.CODEC.listOf().fieldOf("conditions").forGetter(RitualConditionMap::asList)
    ).apply(inst, RitualConditionMap::new));

    private final HashMap<ResourceLocation, RitualCondition> conditions = new LinkedHashMap<>();

    public RitualConditionMap(@Nonnull List<RitualCondition> conditions) {
        // TODO: sort to have a consistent recipe info in jei
        conditions.forEach(condition -> this.conditions.put(condition.getResourceLocation(), condition));
    }

    public boolean test(RitualRecipeContext contest) {
        return this.conditions.values().stream().allMatch(condition -> condition.test(contest));
    }

    public Catalyst getCatalyst() {
        RitualCondition uncasted = this.conditions.get(RitualConditionKey.CATALYST);
        if (uncasted instanceof Catalyst catalyst) {
            return catalyst;
        } else {
            throw new ClassCastException("Wrong type in RitualConditionMap, catalyst key is " + uncasted);
        }
    }

    public List<Component> getDescriptions() {
        // TODO: better formatting
        return this.conditions.values().stream().map(RitualCondition::getDescription).flatMap(Collection::stream).toList();
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
        private final HashSet<RitualCondition> conditions = new LinkedHashSet<>();

        public void addCondition(RitualCondition condition) {
            conditions.add(condition);
        }

        public RitualConditionMap build() {return new RitualConditionMap(conditions.stream().toList());}
    }
}
