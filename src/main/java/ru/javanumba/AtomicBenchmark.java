package ru.javanumba;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import static ru.javanumba.SplitUtil.splitEvenly;

public final class AtomicBenchmark {
    public static long run(int initialValue) {
        AtomicInteger value = new AtomicInteger(initialValue);
        BenchmarkTimer timer = new BenchmarkTimer();
        timer.start();

        ExecutorService pool = Executors.newFixedThreadPool(BenchmarkConfig.THREAD_COUNT);

        int[] decParts = splitEvenly(BenchmarkConfig.DECREMENT_TOTAL, BenchmarkConfig.THREAD_COUNT);
        int[] incParts = splitEvenly(BenchmarkConfig.INCREMENT_TOTAL, BenchmarkConfig.THREAD_COUNT);

        for (int part : decParts) pool.execute(() -> repeat(part, value::decrementAndGet));
        pool.shutdown();
        await(pool);

        pool = Executors.newFixedThreadPool(BenchmarkConfig.THREAD_COUNT);
        for (int part : incParts) pool.execute(() -> repeat(part, value::incrementAndGet));
        pool.shutdown();
        await(pool);

        timer.stop();
        System.out.println("Atomic result: " + value.get());
        timer.print("Atomic");
        return timer.elapsedNanos();
    }

    private static void repeat(int times, Runnable action) {
        for (int i = 0; i < times; i++) action.run();
    }

    private static void await(ExecutorService pool) {
        try {
            if (!pool.awaitTermination(1, TimeUnit.MINUTES)) pool.shutdownNow();
        } catch (InterruptedException e) {
            pool.shutdownNow();
            throw new RuntimeException("Interrupted", e);
        }
    }
}