package pl.sages.java.dopcalculator.utils;

import java.util.function.Function;
import java.util.stream.Stream;

import static pl.sages.java.dopcalculator.utils.Either.left;
import static pl.sages.java.dopcalculator.utils.Either.right;
import static pl.sages.java.dopcalculator.utils.TryFunction.catching;

@FunctionalInterface
public interface TryFunction<T, R> {

    R map(T t) throws Throwable;

    static <T, R> Function<T, Either<? super Throwable, R>> catching(TryFunction<T, R> exceptionalFunction) {
        return t -> {
            try {
                return right(exceptionalFunction.map(t));
            } catch (Throwable e) {
                return left(e);
            }
        };
    }
}

interface TryFunctionTest {

    static void main() {
        Stream.of("1", "2137", "lol")
                .map(catching(Long::valueOf))
                .forEach(IO::println);
    }
}
