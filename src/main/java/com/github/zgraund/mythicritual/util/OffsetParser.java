package com.github.zgraund.mythicritual.util;

import com.github.zgraund.mythicritual.position.RelativeDirection;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class OffsetParser<D extends Enum<D> & StringRepresentable> {
    protected final Codec<Vec3i> codec;
    protected final Codec<List<Vec3i>> listCodec;
    protected final D xPositive, xNegative, yPositive, yNegative, zPositive, zNegative;
    protected final Function<D, D> oppositeFunc;

    public OffsetParser(
            Codec<D> codec,
            D xPositive, D xNegative,
            D yPositive, D yNegative,
            D zPositive, D zNegative,
            Function<D, D> opposite
    ) {
        this.xPositive = xPositive;
        this.xNegative = xNegative;
        this.yPositive = yPositive;
        this.yNegative = yNegative;
        this.zPositive = zPositive;
        this.zNegative = zNegative;

        this.oppositeFunc = opposite;

        this.codec =
                CodecUtils.key2IntMapCodec(codec, xPositive.getDeclaringClass().getEnumConstants(), this::validate, this::toVec, this::toMap);
        this.listCodec =
                CodecUtils.listOf(NeoForgeExtraCodecs.withAlternative(this.codec, Vec3i.CODEC));
    }

    public static Cardinal cardinal() {
        return Cardinal.INSTANCE;
    }

    public static Relative relative() {
        return Relative.INSTANCE;
    }

    public Vec3i toVec(Map<D, Integer> map) {
        return new Vec3i(
                get(map, xPositive) - get(map, xNegative),
                get(map, yPositive) - get(map, yNegative),
                get(map, zPositive) - get(map, zNegative)
        );
    }

    @Unmodifiable
    public Map<D, Integer> toMap(Vec3i vec) {
        Map<D, Integer> map = new HashMap<>(3);
        put(map, xPositive, xNegative, vec.getX());
        put(map, yPositive, yNegative, vec.getY());
        put(map, zPositive, zNegative, vec.getZ());
        return map;
    }

    public DataResult<Map<D, Integer>> validate(Map<D, Integer> map) {
        return map.keySet().stream().anyMatch(direction -> map.containsKey(oppositeFunc.apply(direction)))
                ? DataResult.error(() -> "Cannot set offset for opposite directions.", map)
                : DataResult.success(map);
    }

    protected void put(Map<D, Integer> map, D positive, D negative, int val) {
        if (val != 0)
            map.put(val > 0 ? positive : negative, Math.abs(val));
    }

    protected int get(Map<D, Integer> map, D key) {
        return map.getOrDefault(key, 0);
    }

    public Codec<Vec3i> codec() {
        return codec;
    }

    public Codec<List<Vec3i>> listCodec() {
        return listCodec;
    }

    public static class Cardinal extends OffsetParser<Direction> {
        public static final Cardinal INSTANCE = new Cardinal();

        private Cardinal() {
            super(
                    Direction.CODEC,
                    Direction.EAST, Direction.WEST,
                    Direction.UP, Direction.DOWN,
                    Direction.SOUTH, Direction.NORTH,
                    Direction::getOpposite
            );
        }
    }

    public static class Relative extends OffsetParser<RelativeDirection> {
        public static final Relative INSTANCE = new Relative();

        private Relative() {
            super(
                    RelativeDirection.CODEC,
                    RelativeDirection.RIGHT, RelativeDirection.LEFT,
                    RelativeDirection.UP, RelativeDirection.DOWN,
                    RelativeDirection.FORWARD, RelativeDirection.BACK,
                    RelativeDirection::getOpposite
            );
        }
    }
}
