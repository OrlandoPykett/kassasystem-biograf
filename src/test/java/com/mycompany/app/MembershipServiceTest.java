package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.*;

import com.mycompany.app.membership.Membership;
import com.mycompany.app.membership.MembershipService;
import org.junit.jupiter.api.Test;

import com.mycompany.app.util.DummyIdDatabase;
import com.mycompany.app.util.DummyLogger;
import com.mycompany.app.util.ExceptionThrowingIdDatabase;
import com.mycompany.app.util.IdDatabase;
import com.mycompany.app.util.IdDatabaseStub;
import com.mycompany.app.util.Logger;
import com.mycompany.app.util.NumberOfCallsLogger;
import com.mycompany.app.util.RandomStringGenerator;

public class MembershipServiceTest {
    @Test
    public void membership_creation_is_logged() {
        int creationCount = 10;
        Logger callsLogger = new NumberOfCallsLogger();

        MembershipService memService = new MembershipService(new DummyIdDatabase(), callsLogger);

        for (int i = 0; i < creationCount; i++) {
            memService.getMembership();
        }

        assertEquals(creationCount, ((NumberOfCallsLogger) callsLogger).getNumberOfCalls());
    }

    @Test
    public void database_exception_handled_correctly() {
        MembershipService memService = new MembershipService(new ExceptionThrowingIdDatabase(), new DummyLogger());
        assertThrows(IllegalStateException.class, () -> {
            memService.getMembership();
        });
    }

    @Test
    public void duplicate_id_handled_correctly() {
        IdDatabase idDatabaseStub = new IdDatabaseStub();

        MembershipService memService = new MembershipService(idDatabaseStub, new DummyLogger()) {
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
            Membership mem1 = memService.getMembership();
            Membership mem2 = memService.getMembership();
            assertNotEquals(mem1.getId(), mem2.getId());
        }
    }
}
