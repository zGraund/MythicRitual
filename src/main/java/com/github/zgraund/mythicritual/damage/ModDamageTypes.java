package com.github.zgraund.mythicritual.damage;

import com.github.zgraund.mythicritual.MythicRitual;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> RITUAL_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, MythicRitual.ID("ritual_damage"));
}
