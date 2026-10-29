
package com.mycompany.app;

import com.mycompany.app.initialisation.CinemaInitialiser;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RegisterPerformanceTest {

    private static final String VALID_ITEM_ID = "F1";
    private static final String AGE_RESTRICTED_ITEM_ID =
            "B3";

    private record Scenario(
            String name,
            String commands,
            int iterations
    ) {}

    public static void main(String[] args) {
        List<Scenario> scenarios = List.of(
                normalSales(10),
                repeatedCancellations(100),
                invalidItemAttempts(100),
                emptyCheckoutAttempts(100),
                invalidCommands(1000),
                ageValidationAttempt()
        );

        for (Scenario scenario : scenarios) {
            runScenario(scenario);
        }
    }

    private static Register createRegister(InputReader input) {
        CinemaInitialiser initialiser = new CinemaInitialiser();

        StockManager stockManager = new StockManager();
        initialiser.populateInventory(stockManager);

        ScreeningManager screeningManager = new ScreeningManager();
        initialiser.populateMovies(screeningManager);

        TicketManager ticketManager = new TicketManager();

        CheckoutManager checkoutManager =
                new CheckoutManager(stockManager, ticketManager);

        return new Register(
                input,
                stockManager,
                ticketManager,
                checkoutManager,
                screeningManager
        );
    }

    private static Scenario normalSales(int customers) {
        StringBuilder commands = new StringBuilder();

        for (int i = 0; i < customers; i++) {
            commands.append("new customer\n");
            commands.append("add\n");
            commands.append(VALID_ITEM_ID).append('\n');
            commands.append("pay card\n");
        }

        commands.append("exit\n");

        return new Scenario(
                "Normal sales",
                commands.toString(),
                customers
        );
    }

    private static Scenario repeatedCancellations(int customers) {
        StringBuilder commands = new StringBuilder();

        for (int i = 0; i < customers; i++) {
            commands.append("new customer\n");
            commands.append("add\n");
            commands.append(VALID_ITEM_ID).append('\n');
            commands.append("cancel\n");
        }

        commands.append("exit\n");

        return new Scenario(
                "Repeated cancellations",
                commands.toString(),
                customers
        );
    }

    private static Scenario invalidItemAttempts(int attempts) {
        StringBuilder commands = new StringBuilder();

        for (int i = 0; i < attempts; i++) {
            commands.append("new customer\n");
            commands.append("add\n");
            commands.append("INVALID_ITEM_ID\n");
            commands.append("cancel\n");
        }

        commands.append("exit\n");

        return new Scenario(
                "Invalid item IDs",
                commands.toString(),
                attempts
        );
    }

    private static Scenario emptyCheckoutAttempts(int attempts) {
        StringBuilder commands = new StringBuilder();

        for (int i = 0; i < attempts; i++) {
            commands.append("new customer\n");
            commands.append("pay card\n");
            commands.append("cancel\n");
        }

        commands.append("exit\n");

        return new Scenario(
                "Empty checkout attempts",
                commands.toString(),
                attempts
        );
    }

    private static Scenario invalidCommands(int attempts) {
        StringBuilder commands = new StringBuilder();

        for (int i = 0; i < attempts; i++) {
            commands.append("invalid-command\n");
        }

        commands.append("exit\n");

        return new Scenario(
                "Invalid commands",
                commands.toString(),
                attempts
        );
    }

    private static Scenario ageValidationAttempt() {
        String commands = """
                new customer
                add
                %s
                cancel
                exit
                """.formatted(AGE_RESTRICTED_ITEM_ID);

        return new Scenario(
                "Age-restricted item with unknown age",
                commands,
                1
        );
    }

    private static void runScenario(Scenario scenario) {
        InputReader input = new InputReader(
                new Scanner(scenario.commands())
        );

        Register register = createRegister(input);

        PrintStream originalOut = System.out;
        long start = 0;
        long elapsed = 0;

        try (PrintStream sink =
                     new PrintStream(OutputStream.nullOutputStream())) {

            // Suppress console output so printing does not dominate timings.
            System.setOut(sink);

            start = System.nanoTime();
            register.start();
            elapsed = System.nanoTime() - start;

        } catch (Exception e) {
            System.setOut(originalOut);

            System.out.println(
                    scenario.name() + " failed: " + e.getMessage()
            );
            return;

        } finally {
            System.setOut(originalOut);
        }

        double milliseconds = elapsed / 1_000_000.0;
        double operationsPerSecond =
                elapsed == 0 ? 0 :
                        scenario.iterations() * 1_000_000_000.0 / elapsed;

        System.out.printf(
                "%-40s %10.3f ms | %10.1f iterations/sec%n",
                scenario.name(),
                milliseconds,
                operationsPerSecond
        );
    }
}
