package com.mycompany.app.items;

import com.mycompany.app.Money;

public class Ticket extends Item {

    private final MovieScreening screening;
    private final int seat;

    public Ticket(String id, String name, Money price, int seat, MovieScreening screening) {

        super(id, name, price);

        this.screening = screening;
        this.seat = seat;
    }

    public MovieScreening getScreening() {
        return screening;
    }

    public Movie getMovie() {
        return screening.getMovie();
    }

    public int getSeat() {
        return seat;
    }

    public boolean customerIsOfAge(int age) {
        return screening.getMovie().isSuitableFor(age);
    }

    @Override
    public String toString() {
        return "Movie: %s, Seat: %d, Price: %s"
                .formatted(screening, seat, getPrice());
    }
}
