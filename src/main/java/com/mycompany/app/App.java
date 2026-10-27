package com.mycompany.app;

import java.util.Scanner;
import com.mycompany.app.items.AgeRequirement;
import com.mycompany.app.items.Movie;
import com.mycompany.app.items.MovieScreening;
import com.mycompany.app.items.Seat;
import com.mycompany.app.items.Theater;

import com.mycompany.app.items.Ticket;
import java.time.LocalDateTime;

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

       // Medlemsstatus från terminalen används i demonstrationen.
        Membership membership = new Membership("demo-member");
        membership.setActiveStatus(member);

// Beräknar grundpris utan medlemsrabatt.
        TicketPriceCalculator ticketCalculator =
            new TicketPriceCalculator();

        Money basePrice = ticketCalculator.calculateBasePrice(age);

// Skapar en exempelvisning och en biljett.
        Movie movie = new Movie("Demo Movie", 120, AgeRequirement.AGE_0);
        Theater theater = new Theater("Harald", 8, 38);

        MovieScreening screening = new MovieScreening(
        movie, LocalDateTime.of(2026, 12, 18, 18, 0), theater);

        Ticket ticket = new Ticket(
            "demo-ticket",
            basePrice,
            new Seat('A', 1),
            screening
);

// Beräknar medlemspriset en gång från biljettens grundpris.
        MembershipDiscountCalculator discountCalculator = new MembershipDiscountCalculator();
        Money price = discountCalculator.calculatePrice(ticket, membership);

        System.out.println("Biljettpris: " + price.getCrown() + " kr");
    }
}