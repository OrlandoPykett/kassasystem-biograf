package com.mycompany.app.items;

import com.mycompany.app.Money;

import static com.mycompany.app.items.AgeRequirement.*;

public class Beverage extends Item {

    private final String name;
    private final BeverageSize size;
    private final AgeRequirement ageReq;

    public Beverage(String id, Money price, String name, BeverageSize size, boolean isAlcoholic) {
        super(id, price);
        this.size = size;
        this.name = name;

        if (isAlcoholic) {
            this.ageReq = AGE_18;
        }
        else {
            this.ageReq = AGE_0;
        }
    }

    public String getName() {
        return name;
    }

    public BeverageSize getSize() {
        return size;
    }

    public AgeRequirement getAgeReq() {
        return ageReq;
    }

    public boolean customerIsOfAge(int age) {
        return ageReq.isOfAge(age);
    }
}
