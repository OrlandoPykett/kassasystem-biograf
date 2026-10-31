package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycompany.app.util.DummyIdDatabase;
import com.mycompany.app.util.DummyLogger;
import com.mycompany.app.util.ExceptionThrowingIdDatabase;
import com.mycompany.app.util.IdDatabase;
import com.mycompany.app.util.IdDatabaseStub;
import com.mycompany.app.util.Logger;
import com.mycompany.app.util.NumberOfCallsLogger;
import com.mycompany.app.util.RandomStringGenerator;

@ExtendWith(MockitoExtension.class)
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

    @Mock IdDatabase idDatabase;
    
    @Mock Logger logger;

    @InjectMocks CustomerService custService;

    @DisplayName("Check if logging occurs with each customer creation. (With the help us mock injection)")
    @Test
    public void customer_creation_is_logged_UsingInjectMocksAnnotation() {
        String[] names = {"NAME1", "NAME2", "NAME3", "NAME4", "NAME5"};

        for (int i = 0; i < names.length; i++) {
            custService.createCustomer(names[i]);
        }

        verify(logger, times(names.length)).log(any(), any());
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
