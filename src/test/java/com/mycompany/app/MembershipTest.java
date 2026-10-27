package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

public class MembershipTest {
    private Membership getDefaultMembership() {
        return new Membership("ID");
    }

    @Test
    public void id_is_set_correctly() {
        Membership m = getDefaultMembership();
        assertTrue(!(m.getId().isBlank()));
    }

    @Test
    public void membership_since_is_creation_time() {
        LocalDateTime creationTime = LocalDateTime.of(2000, 1, 31, 12, 50);

        DateTimeFormatter formatter = DateTimeFormatter
                                        .ofLocalizedDateTime(FormatStyle.SHORT)
                                        .withLocale(Locale.of("se", "SW"));

        Membership m1 = new Membership("ID") {
            @Override
            public LocalDateTime getMembershipTierSince() {
                return creationTime;
            }
        };

        Membership m2 = new Membership("ID", creationTime);
        
        assertEquals("2000-01-31 12:50", formatter.format(m1.getMembershipTierSince()));
        assertEquals("2000-01-31 12:50", formatter.format(m2.getMembershipTierSince()));
    }
    
    @Test
    public void membershipTier_is_set_correctly() {
        Membership m1 = getDefaultMembership();
        assertTrue(Arrays.asList(MembershipTier.values()).contains(m1.getMembershipTier()));
        
        Membership m2 = new Membership("ID", MembershipTier.STANDARD);
        assertTrue(Arrays.asList(MembershipTier.values()).contains(m2.getMembershipTier()));
    }

    @ParameterizedTest
    @EnumSource(MembershipTier.class)
    public void membershipTier_is_updated_correctly(MembershipTier tier) {
        Membership m = getDefaultMembership();
        m.updateMembershipTier(tier);
        assertEquals(tier, m.getMembershipTier());
    }

    @Test
    public void membership_is_initially_active() {
        Membership m = getDefaultMembership();
        assertTrue(m.isActive());
    }

    @ParameterizedTest
    @ValueSource(booleans={true, false})
    public void active_status_can_be_modified(boolean status) {
        Membership m = getDefaultMembership();
        m.setActiveStatus(status);
        assertEquals(status, m.isActive());
    }

    @Test
    public void point_is_initially_zero() {
        Membership m = getDefaultMembership();
        assertEquals(0, m.getPoint().points());
    }
}
