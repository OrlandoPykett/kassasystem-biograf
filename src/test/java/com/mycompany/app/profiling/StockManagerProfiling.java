package com.mycompany.app.profiling;

import com.mycompany.app.Money;
import com.mycompany.app.StockManager;
import com.mycompany.app.items.Food;
import com.mycompany.app.items.Item;

public class StockManagerProfiling {

    private static final int WARMUP_ITERATIONS = 1_000_000;
    private static final int MEASURED_ITERATIONS = 1_000_000_000;

    public static void main(String[] args) {

        StockManager manager = new StockManager();

        // Test item
        Item item = new Food("Food1", Money.ofPenny(5000), "Popcorn");
        manager.addStock(item, 1_000);

        System.out.println("Attach VisualVM, then press Enter.");
        try {
            if (System.in.read() == -1) {
                return;
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to read input", e);
        }

        // Warm up the JVM before measuring
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            manager.getStock(item);
        }

        System.out.println("Warmup complete. Starting workload.");

        long start = System.nanoTime();

        for (int i = 0; i < MEASURED_ITERATIONS; i++) {
            manager.getStock(item);
            manager.hasStock(item, 1);
            manager.addStock(item, 1);
            manager.removeStock(item, 1);
            manager.getItems();
            manager.getItem(item.getId());
        }

        long elapsedTime = System.nanoTime() - start;

        System.out.println("Workload complete.");
        System.out.println("Execution time: "
                + elapsedTime / 1_000_000.0 + " ms");
    }
}
