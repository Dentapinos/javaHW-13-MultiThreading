package ru.javanumba;

import java.util.Scanner;

public final class CompareExecution {
    public static void main(String[] args) {
        final String blue  = "\u001b[34m", yellow = "\u001b[33m", reset = "\u001b[0m";
        System.out.println(blue
                + "Приложение для сравнения скорости уменьшения/увеличения числа"
                + " в одно- и многопоточном режиме" + reset);

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print(yellow + "Введите целое число: " + reset);
            int input = sc.nextInt();

            long single   = SingleThreadBenchmark.run(input);
            long forkJoin = ForkJoinBenchmark.run(input);
            long atomic   = AtomicBenchmark.run(input);
            long sync     = SyncBenchmark.execute(input);

            System.out.println("\n📊 Результаты:");
            System.out.printf("1. Single-thread: %d ns%n", single);
            System.out.printf("2. Fork/Join:     %d ns%n", forkJoin);
            System.out.printf("3. Atomic:        %d ns%n", atomic);
            System.out.printf("4. Synchronized:  %d ns%n", sync);

            long best = Math.min(Math.min(single, forkJoin), Math.min(atomic, sync));
            System.out.print("✅ Самый быстрый: ");
            if (best == single)   System.out.println("single-thread");
            else if (best == forkJoin) System.out.println("fork/join");
            else if (best == atomic)   System.out.println("atomic");
            else System.out.println("synchronized");
        }
    }
}
