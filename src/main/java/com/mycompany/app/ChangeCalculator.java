package com.mycompany.app;

public class ChangeCalculator {

    public Money calculateChange(Money totalPrice, Money amountPaid) {
        return amountPaid.subtract(totalPrice);
    }

    public long calculateChange(long totalPrice, long amountPaid) {
        Money price = Money.ofPenny(totalPrice);
        Money paid = Money.ofPenny(amountPaid);

        return calculateChange(price, paid).getPenny();
    }
}
