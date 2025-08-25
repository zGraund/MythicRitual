package com.github.zgraund.mythicritual.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

public final class EntityUse {
    private final Entity entity;
    private int used;

    public EntityUse(Entity entity, int used) {
        this.entity = entity;
        this.used = used;
    }

    public void increment(int amount) {used += amount;}

    public void consume() {
        switch (entity) {
            case ItemEntity i -> i.getItem().shrink(used);
            case LivingEntity l -> {if (used > 0) l.kill();}
            default -> entity.kill();
        }
    }

    public Entity entity() {return entity;}

    public int used() {return used;}
}
