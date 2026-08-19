package com.github.zgraund.mythicritual.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CodecUtils {
    public static <T> Codec<List<T>> listOf(@Nonnull Codec<T> codec) {
        return Codec.either(codec, codec.listOf()).xmap(CodecUtils::eitherToList, CodecUtils::listToEither);
    }

    public static <T> List<T> eitherToList(@Nonnull Either<T, List<T>> either) {
        return either.map(List::of, t -> t);
    }

    public static <T> Either<T, List<T>> listToEither(@Nonnull List<T> list) {
        return list.size() == 1 ? Either.left(list.getFirst()) : Either.right(list);
    }

    @Nonnull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> Codec<T> dispatch(Registry<MapCodec<? extends T>> registry, Function<? super T, MapCodec<? extends T>> codec) {
        return Codec.lazyInitialized(() -> registry.byNameCodec().dispatch(codec, Function.identity()));
    }

    public static <K, V> Codec<V> key2IntMapCodec(
            Codec<K> keyCodec,
            StringRepresentable[] keys,
            Function<Map<K, Integer>, DataResult<Map<K, Integer>>> validator,
            Function<Map<K, Integer>, V> to,
            Function<V, Map<K, Integer>> from
    ) {
        return Codec.simpleMap(keyCodec, ExtraCodecs.NON_NEGATIVE_INT, StringRepresentable.keys(keys))
                    .validate(validator)
                    .xmap(to, from)
                    .codec();
    }
}
