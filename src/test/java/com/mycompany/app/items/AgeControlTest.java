package com.mycompany.app.items;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgeControlTest {

    @Test
    void rejectsCustomerBelowMinimumAge() {
        AgeControl control = new AgeControl();

        assertFalse(control.isAccepted(14, AgeRequirement.AGE_15));
    }

    @Test
    void allowsCustomerAboveMinimumAge() {
        AgeControl ageControl = new AgeControl();

        assertTrue(ageControl.isAccepted(18, AgeRequirement.AGE_15));
    }

    @Test
    void rejectsCustomerBelowAlcoholAgeLimit() {
        AgeControl ageControl = new AgeControl();

        assertFalse(ageControl.isAccepted(17, AgeRequirement.AGE_18));
    }

    @Test
    void allowsCustomerAtAlcoholAgeLimit() {
        AgeControl ageControl = new AgeControl();

        assertTrue(ageControl.isAccepted(18, AgeRequirement.AGE_18));
    }
}
