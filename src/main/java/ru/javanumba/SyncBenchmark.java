package ru.javanumba;

import static ru.javanumba.SplitUtil.splitEvenly;

public final class SyncBenchmark {
    private int value;

    public long run(int initialValue) {
        value = initialValue;
        BenchmarkTimer timer = new BenchmarkTimer();
        timer.start();

        int[] decParts = splitEvenly(BenchmarkConfig.DECREMENT_TOTAL, BenchmarkConfig.THREAD_COUNT);
        int[] incParts = splitEvenly(BenchmarkConfig.INCREMENT_TOTAL, BenchmarkConfig.THREAD_COUNT);

        runTasks(decParts, this::decrement);
        runTasks(incParts, this::increment);

        timer.stop();
        System.out.println("Synchronized result: " + value);
        timer.print("Synchronized");
        return timer.elapsedNanos();
    }

    private void runTasks(int[] parts, Runnable action) {
        Thread[] threads = new Thread[parts.length];
        for (int i = 0; i < parts.length; i++) {
            int n = parts[i];
            threads[i] = new Thread(() -> repeat(n, action));
            threads[i].start();
        }
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }
        }
    }

    private void repeat(int times, Runnable action) {
        for (int i = 0; i < times; i++) action.run();
    }

    private synchronized void increment() { value++; }
    private synchronized void decrement() { value--; }

    public static long execute(int initialValue) {
        return new SyncBenchmark().run(initialValue);
    }
}
