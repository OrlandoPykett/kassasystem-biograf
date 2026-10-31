package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import com.mycompany.app.membership.Membership;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CustomerTest {
    private static class CustomerBuilder {
        private String id, name;
        private Membership membership;
        LocalDateTime customerSince;

        private CustomerBuilder(String id, String name, Membership membership, LocalDateTime customerSince) {
            this.id = id;
            this.name = name;
            this.membership = membership;
            this.customerSince = customerSince;
        }

        public static CustomerBuilder setDefault() {
            final String DEFAULT_CUSTOMER_ID = "CUSTID";
            final String DEFAULT_NAME = "NAME";
            final String DEFAULT_MEMBERSHIP_ID = "MEMID";
            final Membership DEFAULT_MEMBERSHIP = new Membership(DEFAULT_MEMBERSHIP_ID);
            final LocalDateTime DEFAULT_CUSTOMER_SINCE = LocalDateTime.of(2000, 1, 31, 12, 20);
    
            return new CustomerBuilder(DEFAULT_CUSTOMER_ID, DEFAULT_NAME, DEFAULT_MEMBERSHIP, DEFAULT_CUSTOMER_SINCE);
        }

        public CustomerBuilder id(String id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CustomerBuilder membership(Membership membership) {
            this.membership = membership;
            return this;
        }

        public CustomerBuilder customerSince(LocalDateTime customerSince) {
            this.customerSince = customerSince;
            return this;
        }

        public Customer build() {
            if (id == null || name == null || customerSince == null) {
                throw new IllegalStateException();
            }

            return new Customer(id, name, membership, customerSince);
        }
    }

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

    @DisplayName("Test if name is set correctly. (Uses builder pattern to clarify that name is being tested)")
    @Test public void name_is_set() {

        Customer cust = CustomerBuilder.setDefault()
                            .name("NAME")
                            .build();
        
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

        Customer cust = new Customer("ID", "NAME", getDefaultMembership(), registrationDate);

        assertEquals("2000-01-02 09:30", formatter.format(cust.getCustomerSince()));
    }

    @Test
    public void customer_with_membership_is_valid() {
        Customer cust = new Customer("ID", "NAME", getDefaultMembership(), LocalDateTime.now());
        assertAll(
            () -> assertTrue(cust.isMember()),
            () -> assertNotNull(cust.getMembership())
        );
    }

    @Test
    public void customer_with_no_membership_is_valid() {
        Customer cust = new Customer("ID", "NAME", null, LocalDateTime.now());
        assertAll(
            () -> assertFalse(cust.isMember()),
            () -> assertNull(cust.getMembership())
        );
    }

    @Test
    public void customer_eqaulity_is_correct() {
        Customer cust1 = new Customer("ID1", "NAME1", getDefaultMembership(), LocalDateTime.now());
        Customer cust2 = new Customer("ID1", "NAME2", getDefaultMembership(), LocalDateTime.now());
        Customer cust3 = new Customer("ID2", "NAME1", getDefaultMembership(), LocalDateTime.now());
        
        assertAll(
            () -> assertTrue(cust1.equals(cust2)),
            () -> assertFalse(cust1.equals(new Object())),
            () -> assertNotEquals(cust1.hashCode(), cust3.hashCode())
        );
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
