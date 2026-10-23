package com.mycompany.app;

public class Contact {
    private String mail, phone;

    public Contact(String mail, String phone) {
        this.mail = mail;
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }
}
