package com.mycompany.app;

import com.mycompany.app.membership.Membership;

import java.time.LocalDateTime;

public class Customer implements Comparable<Customer> {
    private final String id;
    private String name;
    private final LocalDateTime customerSince;
    private Membership membership;

    public Customer(String id, String name, Membership membership, LocalDateTime customerSince) {
        
        if (id.isBlank() || id.contains(" ") || name.isBlank()) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.name = name;
        this.customerSince = customerSince;
        this.membership = membership;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCustomerSince() {
        return customerSince;
    }

    public boolean isMember() {
        return membership == null ? false : true;
    }

    public Membership getMembership() {
        return membership;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Customer cust) {
            return cust.id.equals(id);
        }

        return false;
    }

    @Override
    public int compareTo(Customer o) {
        return id.compareTo(o.id);
    }
}
