package com.mycompany.app;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your age: ");
        
        if (!scanner.hasNextInt()){
            System.out.println("Age must be a number.");
            return;
        }
        int age = scanner.nextInt();

        if(age < 0){
            System.out.println("Age cannot be negative.");
            return;
        }

        System.out.print("Are you a member? (true/false): ");
        if(!scanner.hasNextBoolean()){
            System.out.println("Please Enter true or false.");
            return;
        }
        boolean member = scanner.nextBoolean();

        TicketPriceCalculator calculator = new TicketPriceCalculator();
        Money price = calculator.calculatePrice(age, member);

        System.out.println("Biljettpris: " + price.getCrown() + " kr");
    }
}