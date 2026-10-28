package com.mycompany.app;

import java.net.SocketException;
import java.time.LocalDateTime;

import com.mycompany.app.util.IdDatabase;
import com.mycompany.app.util.Logger;
import com.mycompany.app.util.RandomStringGenerator;
import com.mycompany.app.util.Logger.LogLevel;

public class MembershipService {
    private final IdDatabase idDatabase;
    private final Logger logger;

    public MembershipService(IdDatabase idDatabase, Logger logger) {
        this.idDatabase = idDatabase;
        this.logger = logger;
    }
    
    public Membership getMembership() {
        return getMembership(MembershipTier.getDefault());
    }

    public Membership getMembership(MembershipTier tier) {
        String id = getRandomId();

        try {
            while (idDatabase.hasId(id)) {
                id = getRandomId();
            }

            idDatabase.storeId(id);
        }
        catch (SocketException e) {
            throw new IllegalStateException("MembershipService cannot connect to database");
        }

        LocalDateTime dateTime = LocalDateTime.now();

        logger.log(LogLevel.INFO, "Membership: id: %s, tier: %s, creation: %s".formatted(id, tier, dateTime));

        return new Membership(id, dateTime, tier);
    }

    protected String getRandomId() {
        return new RandomStringGenerator(10).generate();
    }
}
