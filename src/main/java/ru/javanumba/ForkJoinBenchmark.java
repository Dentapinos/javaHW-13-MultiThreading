package ru.javanumba;

import java.util.Arrays;
import java.util.concurrent.*;
import static ru.javanumba.SplitUtil.splitEvenly;

public final class ForkJoinBenchmark {

    public static long run(int initialValue) {
        BenchmarkTimer timer = new BenchmarkTimer();
        timer.start();

        ExecutorService pool = Executors.newFixedThreadPool(BenchmarkConfig.THREAD_COUNT);

        // Decrement
        int[] startParts   = splitEvenly(initialValue, BenchmarkConfig.THREAD_COUNT);
        int[] decParts     = splitEvenly(BenchmarkConfig.DECREMENT_TOTAL, BenchmarkConfig.THREAD_COUNT);

        int[] afterDec = invokeAndSum(pool, decParts, (idx) -> startParts[idx] - decParts[idx]);

        // Increment
        int[] incStartParts = splitEvenly(Arrays.stream(afterDec).sum(), BenchmarkConfig.THREAD_COUNT);
        int[] incParts      = splitEvenly(BenchmarkConfig.INCREMENT_TOTAL, BenchmarkConfig.THREAD_COUNT);

        int result = Arrays.stream(invokeAndSum(pool, incParts,
                        idx -> incStartParts[idx] + incParts[idx]))
                .sum();

        pool.shutdown();
        timer.stop();

        System.out.println("Fork/Join result: " + result);
        timer.print("Fork/Join");
        return timer.elapsedNanos();
    }

    private static int[] invokeAndSum(ExecutorService pool,
                                      int[] parts,
                                      IntUnaryOperator op) {
        Future<Integer>[] futures = new Future[parts.length];
        for (int i = 0; i < parts.length; i++) {
            final int idx = i;
            futures[i] = pool.submit(() -> op.applyAsInt(idx));
        }
        int[] res = new int[parts.length];
        try {
            for (int i = 0; i < res.length; i++) res[i] = futures[i].get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Task failed", e);
        }
        return res;
    }

    @FunctionalInterface
    private interface IntUnaryOperator {
        int applyAsInt(int operand);
    }
}