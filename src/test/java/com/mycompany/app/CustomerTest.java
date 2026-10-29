package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.NoSuchElementException;

import com.mycompany.app.membership.Membership;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CustomerTest {
    private Membership getDefaultMembership() {
        return new Membership("ID");
    }

    @Test
    public void id_is_set() {
        Customer cust = new Customer("ID", "NAME", getDefaultMembership(), LocalDateTime.now());
        assertEquals("ID", cust.getId());
    }

    @ParameterizedTest
    @ValueSource(strings={"", "HAS SPACE"})
    public void invalid_id_throws_exception(String invalidId) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer(invalidId, "NAME", getDefaultMembership(), LocalDateTime.now());
        });
    }

    @Test
    public void name_is_set() {
        Customer cust = new Customer("ID", "NAME", getDefaultMembership(), LocalDateTime.now());
        assertEquals("NAME", cust.getName());
    }

    @ParameterizedTest
    @ValueSource(strings={""})
    public void invalid_name_throws_exception(String invalidName) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer("ID", invalidName, getDefaultMembership(), LocalDateTime.now());
        });
    }

    @Test
    public void customer_since_is_registration_date() {
        LocalDateTime registrationDate = LocalDateTime.of(2000, 1, 2, 9, 30);

        DateTimeFormatter formatter = DateTimeFormatter
                                .ofLocalizedDateTime(FormatStyle.SHORT)
                                .withLocale(Locale.of("se", "SW"));

        Customer cust = new Customer("ID", "NAME", getDefaultMembership(), LocalDateTime.now()) {
            @Override
            public LocalDateTime getCustomerSince() {
                return registrationDate;
            }
        };

        assertEquals("2000-01-02 09:30", formatter.format(cust.getCustomerSince()));
    }

    @Test
    public void customer_with_membership_is_valid() {
        Customer cust = new Customer("ID", "NAME", getDefaultMembership(), LocalDateTime.now());
        assertTrue(cust.isMember());
    }

    @Test
    public void customer_with_no_membership_is_valid() {
        Customer cust = new Customer("ID", "NAME", null, LocalDateTime.now());
        assertFalse(cust.isMember());

        assertThrows(NoSuchElementException.class, () -> {
            cust.getMembership().get();
        });
    }

    @Test
    public void customer_eqaulity_is_correct() {
        Customer cust1 = new Customer("ID1", "NAME1", getDefaultMembership(), LocalDateTime.now());
        Customer cust2 = new Customer("ID1", "NAME2", getDefaultMembership(), LocalDateTime.now());
        Customer cust3 = new Customer("ID2", "NAME1", getDefaultMembership(), LocalDateTime.now());
        
        assertTrue(cust1.equals(cust2));
        assertNotEquals(cust1.hashCode(), cust3.hashCode());;
    }
    
    @Test
    public void customers_are_sorted_correctly() {
        Customer cust1 = new Customer("ID1", "NAME3", getDefaultMembership(), LocalDateTime.now());
        Customer cust2 = new Customer("ID2", "NAME1", getDefaultMembership(), LocalDateTime.now());
        Customer cust3 = new Customer("ID3", "NAME2", getDefaultMembership(), LocalDateTime.now());
    
        ArrayList<Customer> expected = new ArrayList<>(Arrays.asList(cust1, cust2, cust3));
        ArrayList<Customer> actual = new ArrayList<>(Arrays.asList(cust1, cust3, cust2));

        Collections.sort(actual);
        assertEquals(expected, actual);
    }
}
