package com.mycompany.app.items;

import com.mycompany.app.Money;

import java.util.Objects;

public class Ticket extends Item {

    private final MovieScreening screening;
    private final Seat seat;

    public Ticket(String id, Money price, Seat seat, MovieScreening screening) {

        super(id, price);

        this.screening = screening;
        this.seat = seat;
    }

    public MovieScreening getScreening() {
        return screening;
    }

    public Movie getMovie() {
        return screening.getMovie();
    }

    public Seat getSeat() {
        return seat;
    }

    public boolean customerIsOfAge(int age) {
        return screening.getMovie().isSuitableFor(age);
    }

    @Override
    public String toString() {
        return "Movie: %s, Seat: %s, Price: %s"
                .formatted(screening, seat.toString(), getPrice());
    }

    @Override
    public String getName() {
        return screening.getMovie().getTitle();
    }
}
