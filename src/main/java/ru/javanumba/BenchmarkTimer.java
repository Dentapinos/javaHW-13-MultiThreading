package ru.javanumba;

public final class BenchmarkTimer {
    private long startNanos;
    private long elapsedNanos;

    public void start() {
        startNanos = System.nanoTime();
    }

    public void stop() {
        elapsedNanos = System.nanoTime() - startNanos;
    }

    public long elapsedNanos() {
        return elapsedNanos;
    }

    public void print(String label) {
        System.out.printf("%s: %d ns (%d ms)%n",
                label, elapsedNanos, elapsedNanos / 1_000_000);
    }
}

