package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.MythicRitual;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;

public record RitualConditionKey<T extends RitualCondition>(ResourceLocation name, Class<T> type) {
    public static final RitualConditionKey<Catalyst> CATALYST = create("catalyst", Catalyst.class);
    public static final RitualConditionKey<Altar> ALTAR = create("altar", Altar.class);
    public static final RitualConditionKey<Location> LOCATION = create("location", Location.class);
    public static final RitualConditionKey<Time> TIME = create("time", Time.class);
    public static final RitualConditionKey<Weather> WEATHER = create("weather", Weather.class);

    @Nonnull
    @Contract("_, _ -> new")
    public static <T extends RitualCondition> RitualConditionKey<T> create(String id, Class<T> type) {
        return new RitualConditionKey<>(MythicRitual.id(id), type);
    }
}
