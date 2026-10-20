package com.mycompany.app.items;

import com.mycompany.app.Money;

import static com.mycompany.app.items.AgeRequirement.*;

public class Beverage extends Item {

    private BeverageSize size;
    private AgeRequirement ageReq;

    public Beverage(String id, String name, Money price, BeverageSize size, boolean isAlcoholic) {
        super(id, name, price);
        this.size = size;

        if (isAlcoholic) {
            this.ageReq = AGE_18;
        }
        else {
            this.ageReq = AGE_0;
        }
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
