package com.mycompany.app;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        int age = 18;
        boolean member = false; 

        TicketPriceCalculator calculator = new TicketPriceCalculator();
        Money price = calculator.calculatePrice(age, member);

        System.out.println("Biljettpris: " + price.getCrown() + " kr");
    }
}