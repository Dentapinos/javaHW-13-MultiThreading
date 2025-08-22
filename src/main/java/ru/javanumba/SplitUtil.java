package ru.javanumba;

public final class SplitUtil {
    public static int[] splitEvenly(int total, int parts) {
        int[] res = new int[parts];
        for (int i = 0; i < parts; i++) {
            res[i] = ((i + 1) * total) / parts - (i * total) / parts;
        }
        return res;
    }

    private SplitUtil() {}
}

