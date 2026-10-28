package com.mycompany.app;

import com.mycompany.app.items.AgeRequirement;
import com.mycompany.app.items.Movie;
import com.mycompany.app.items.MovieScreening;
import com.mycompany.app.items.Seat;
import com.mycompany.app.items.Theater;
import com.mycompany.app.items.Ticket;

import java.time.LocalDateTime;
import java.util.Scanner;

public class App {

    // Demonstrerar filmpris, åldersavdrag och medlemsrabatt tillsammans.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Läs kundens ålder och stoppa ogiltig inmatning.
        System.out.print("Enter your age: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Age must be a number.");
            return;
        }

        int age = scanner.nextInt();

        if (age < 0) {
            System.out.println("Age cannot be negative.");
            return;
        }

        // Demonstrationen använder STANDARD för en aktiv medlem.
        System.out.print("Are you a member? (true/false): ");

        if (!scanner.hasNextBoolean()) {
            System.out.println("Please enter true or false.");
            return;
        }

        boolean member = scanner.nextBoolean();

        Membership membership = new Membership("demo-member");
        
        membership.setActiveStatus(member);

        // Filmen kostar 150 kr och är tillräckligt lång för längdtillägget.
        Movie movie = new Movie("Demo Movie",160,AgeRequirement.AGE_0,Money.ofPenny(15000));

        // Beräkna biljettpriset före medlemsrabatt.
        TicketPriceCalculator ticketCalculator = new TicketPriceCalculator();

        Money basePrice = ticketCalculator.calculateBasePrice(age, movie);

        // Skapa en exempelbiljett med det beräknade priset.
        Theater theater = new Theater("Harald", 8, 38);

        MovieScreening screening = new MovieScreening(movie,LocalDateTime.of(2026, 12, 18, 18, 0),theater);
        Ticket ticket = new Ticket("demo-ticket",basePrice,new Seat('A', 1),screening);

        // Beräkna medlemsrabatten från biljettens oförändrade originalpris.
        MembershipDiscountCalculator discountCalculator = new MembershipDiscountCalculator();

        Money finalPrice = discountCalculator.calculatePrice(ticket, membership);

        // Visa kronor och ören utan att tappa öresdelen.
        System.out.println("Biljettpris: " + Receipt.format(finalPrice));
    }
}