package com.mycompany.app;

public class Customer {
    private final String id;
    private final Membership membership;
    private String name;

    public Customer(String id, Membership membership, String name) {
        this.id = id;
        this.membership = membership;
        this.name = name;
    }
}