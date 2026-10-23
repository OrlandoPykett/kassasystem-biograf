package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ContactTest {
    @Test
    public void valid_email_will_pass() {
        Contact c = new Contact("user@mail.com", "PHONE");
        assertEquals("user@mail.com", c.getMail());
    }

    @Test
    public void valid_phone_will_pass() {
        Contact c = new Contact("MAIL", "1234567890");
        assertEquals("1234567890", c.getPhone());
    }
}
