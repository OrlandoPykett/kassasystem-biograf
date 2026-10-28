package com.mycompany.app;

import java.net.SocketException;
import java.time.LocalDateTime;

import com.mycompany.app.membership.Membership;
import com.mycompany.app.util.IdDatabase;
import com.mycompany.app.util.Logger;
import com.mycompany.app.util.RandomStringGenerator;
import com.mycompany.app.util.Logger.LogLevel;

/**
 * Class that handles creating customer objects
 */

public class CustomerService {
    private IdDatabase idDatabase;
    private Logger logger;

    public CustomerService(IdDatabase idDatabase, Logger logger) {
        this.idDatabase = idDatabase;
        this.logger = logger;
    }

    public Customer createCustomer(String name) {
        return createCustomer(name, null);
    }

    public Customer createCustomer(String name, Membership membership) {
        String id = getRandomId();

        try {
            while (idDatabase.hasId(id)) {
                id = getRandomId();
            }

            idDatabase.storeId(id);
        }
        catch (SocketException e) {
            throw new IllegalStateException("CustomerService cannot connect to database");
        }
        
        LocalDateTime dateTime = LocalDateTime.now();

        logger.log(LogLevel.INFO, "Customer - id: %s, name: %s, creation: %s".formatted(id, name, dateTime));

        return new Customer(id, name, membership, dateTime);
    }

    protected String getRandomId() {
        return new RandomStringGenerator(10).generate();
    }
}
