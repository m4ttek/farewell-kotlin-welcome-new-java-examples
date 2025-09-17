package pl.sages.java.dopcalculator.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public interface CustomGatherers {

    /// Odpowiednik `map` ale z pominięciem błędów.
    ///
    /// Przerywa procesowanie strumienia bez rzucenia wyjątku, jeśli liczba błędów osiąga `maxErrors`.
    static <T, R> Gatherer<T, AtomicInteger, R> mapWithMaxErrors(TryFunction<T, R> throwingFunction, int maxErrors) {
        return Gatherer.ofSequential( // sequential oznacza narzucenie sekwencyjności nawet mimo paralelizmu
                AtomicInteger::new,   // można pominąć, jeśli Gatherer nie potrzebuje (wtedy zakładany jest Void)
                (state, element, downstream) -> {
                    try {
                        downstream.push(throwingFunction.map(element));
                    } catch (Throwable e) {
                        if (state.incrementAndGet() >= maxErrors) {
                            return false; // przerywamy dalsze procesowanie strumienia
                        }
                    }
                    return true; // kontynuacja procesowania
                });
    }

    static void main() {
        Stream.of("1", "2137", "lol")
                .gather(mapWithMaxErrors(Long::valueOf, 1))
                .forEach(System.out::println); // 1 2137
    }
}
