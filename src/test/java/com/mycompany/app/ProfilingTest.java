package com.mycompany.app;

import com.mycompany.app.util.DummyLogger;
import com.mycompany.app.util.IdDatabaseStub;

public class ProfilingTest {
    public static void main(String[] args) {
        CustomerService custService = new CustomerService(new IdDatabaseStub(), new DummyLogger());
        for (int i = 0; i < 10_000_000; i++) {
            custService.createCustomer("NAME");
        }
    }
}
