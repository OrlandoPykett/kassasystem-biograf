package com.mycompany.app;

import java.time.*;

public class Membership {
    private final String id;
    private LocalDateTime memberSince;
    private MembershipTier membershipTier;
    private boolean active;
    private Point point;

    public Membership(String id) {
        this(id, LocalDateTime.now(), MembershipTier.STANDARD);
    }

    public Membership(String id, LocalDateTime memberSince) {
        this(id, memberSince, MembershipTier.STANDARD);
    }

    public Membership(String id, MembershipTier membershipTier) {
        this(id, LocalDateTime.now(),membershipTier);
    }

    private Membership(String id, LocalDateTime memberSince, MembershipTier membershipTier) {
        this.id = id;
        this.memberSince = memberSince;
        this.membershipTier = membershipTier;
        active = true;
        point = Point.zeroPoint();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getMemberSince() {
        return memberSince;
    }

    public MembershipTier getMembershipTier() {
        return membershipTier;
    }

    public Point getPoint() {
        return point;
    }

    public boolean isActive() {
        return active;
    }

    public void setActiveStatus(boolean status) {
        active = status;
    }

    public void updateMembershipTier(MembershipTier membershipTier) {
        this.membershipTier = membershipTier;
    }
}
