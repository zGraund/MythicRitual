package com.github.zgraund.mythicritual.recipe.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RitualConditionMap {
    public static final MapCodec<RitualConditionMap> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            RitualCondition.CODEC.listOf().fieldOf("conditions").forGetter(RitualConditionMap::asList)
    ).apply(inst, RitualConditionMap::new));

    private final HashMap<ResourceLocation, RitualCondition> conditions = new LinkedHashMap<>();

    public RitualConditionMap(@Nonnull List<RitualCondition> conditions) {
        conditions.forEach(condition -> this.conditions.put(condition.getResourceLocation(), condition));
    }

    public List<RitualCondition> asList() {
        return conditions.values().stream().toList();
    }

    @Override
    public String toString() {
        return "RitualConditionMap{" +
               "conditions=" + conditions +
               '}';
    }
}
