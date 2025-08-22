package ru.javanumba;

public final class BenchmarkConfig {
    public static final int DECREMENT_TOTAL = 2_500_000;
    public static final int INCREMENT_TOTAL = 3_200_000;
    public static final int THREAD_COUNT    = 4;     // можно взять из аргументов

    private BenchmarkConfig() {}
}

