package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mycompany.app.util.DummyIdDatabase;
import com.mycompany.app.util.DummyLogger;
import com.mycompany.app.util.ExceptionThrowingIdDatabase;
import com.mycompany.app.util.IdDatabase;
import com.mycompany.app.util.IdDatabaseStub;
import com.mycompany.app.util.Logger;
import com.mycompany.app.util.NumberOfCallsLogger;
import com.mycompany.app.util.RandomStringGenerator;

public class CustomerServiceTest {
    @Test
    public void customer_creation_is_logged() {
        String[] names = {"NAME1", "NAME2", "NAME3", "NAME4", "NAME5"};

        Logger callsLogger = new NumberOfCallsLogger();

        CustomerService custService = new CustomerService(new DummyIdDatabase(), callsLogger);

        for (int i = 0; i < names.length; i++) {
            custService.createCustomer(names[i]);
        }

        assertEquals(names.length, ((NumberOfCallsLogger) callsLogger).getNumberOfCalls());
    }

    @Test
    public void database_exception_handled_correctly() {
        CustomerService custService = new CustomerService(new ExceptionThrowingIdDatabase(), new DummyLogger());
        assertThrows(IllegalStateException.class, () -> {
            custService.createCustomer("NAME");
        });
    }

    @Test
    public void duplicate_id_handled_correctly() {
        IdDatabase idDatabaseStub = new IdDatabaseStub();

        CustomerService custService = new CustomerService(idDatabaseStub, new DummyLogger()) {
            private int randomIdCallCount;

            @Override
            protected String getRandomId() {
                randomIdCallCount++;
                if (randomIdCallCount % 2 == 0) {
                    return "id"; // simulate generating previously generated id
                }
                return new RandomStringGenerator(10).generate();
            }
        };

        for (int i = 0; i < 20; i ++) {
            Customer cust1 = custService.createCustomer("NAME");
            Customer cust2 = custService.createCustomer("NAME");
            assertNotEquals(cust1.getId(), cust2.getId());
        }
    }
}
