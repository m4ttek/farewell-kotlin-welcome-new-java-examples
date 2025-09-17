package pl.sages.java.dopcalculator.utils;

import java.util.function.Function;

public sealed interface Either<L, R> {

    default <NL> Either<NL, R> mapLeft(Function<? super L, ? extends NL> leftMapper) {
        return switch (this) {
            case Either.Left(L l) -> left(leftMapper.apply(l));
            case Either.Right r -> r;
        };
    }

    default <NR> Either<L, NR> mapRight(Function<? super R, ? extends NR> rightMapper) {
        return switch (this) {
            case Either.Left l -> l;
            case Either.Right(R r) -> right(rightMapper.apply(r));
        };
    }

    static <L, R> Left<L, R> left(L left) {
        return new Left<>(left);
    }

    static <L, R> Right<L, R> right(R right) {
        return new Right<>(right);
    }

    record Left<L, R>(L left) implements Either<L, R> {
    }

    record Right<L, R>(R right) implements Either<L, R> {
    }
}