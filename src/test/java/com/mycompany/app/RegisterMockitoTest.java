package com.mycompany.app;

import org.junit.jupiter.api.Test;

import com.mycompany.app.items.Food;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RegisterMockitoTest {

    @Test
    void emptyPurchaseDoesNotReachCheckout() {
        InputReader input = mock(InputReader.class);
        StockManager stock = mock(StockManager.class);
        TicketManager tickets = mock(TicketManager.class);
        CheckoutManager checkout = mock(CheckoutManager.class);
        ScreeningManager screenings = mock(ScreeningManager.class);

        Register register = new Register(input,stock,tickets,checkout,screenings);

        register.executeCommand("new customer");
        register.executeCommand("pay card");

        verifyNoInteractions(checkout);
    }

    @Test
    void rejectedCheckoutAllowsAnotherPaymentAttempt() {
        InputReader input = mock(InputReader.class);
        StockManager stock = mock(StockManager.class);
        TicketManager tickets = mock(TicketManager.class);
        CheckoutManager checkout = mock(CheckoutManager.class);
        ScreeningManager screenings = mock(ScreeningManager.class);

        Food popcorn = new Food("F1", Money.ofPenny(6000), "popcorn");

        when(input.readString("Customer age")).thenReturn("25");
        when(input.readString("Item id")).thenReturn("F1");
        when(stock.getItem("F1")).thenReturn(popcorn);
        when(stock.getStock(popcorn)).thenReturn(10);

        when(checkout.checkout(any(Receipt.class), eq(25)))
                .thenReturn(false);

        Register register = new Register(input, stock, tickets, checkout, screenings);

        register.executeCommand("new customer");
        register.executeCommand("add");

        register.executeCommand("pay card");
        register.executeCommand("pay card");

        verify(checkout, times(2))
                .checkout(any(Receipt.class), eq(25));
    }

}