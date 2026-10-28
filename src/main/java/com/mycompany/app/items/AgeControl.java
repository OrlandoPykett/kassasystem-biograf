package com.mycompany.app.items;

public class AgeControl {

    public static boolean isAccepted(int age, AgeRequirement ageReq) {
        return ageReq.isOfAge(age);
    }

    public static boolean isAccepted(int age, Item item) {
        AgeRequirement ageReq = getAgeRequirementOf(item);
        return ageReq.isOfAge(age);
    }

    private static AgeRequirement getAgeRequirementOf(Item item) {
        if (item instanceof Beverage beverage) return beverage.getAgeReq();
        if (item instanceof Ticket ticket) return ticket.getMovie().getAgeReq();

        return AgeRequirement.AGE_0;
    }
}
