package ru.javanumba;

public final class SingleThreadBenchmark {
    public static long run(int value) {
        BenchmarkTimer timer = new BenchmarkTimer();
        timer.start();

        for (int i = 0; i < BenchmarkConfig.DECREMENT_TOTAL; i++) value--;
        for (int i = 0; i < BenchmarkConfig.INCREMENT_TOTAL; i++) value++;

        timer.stop();
        System.out.println("Single-thread result: " + value);
        timer.print("Single-thread");
        return timer.elapsedNanos();
    }
}

