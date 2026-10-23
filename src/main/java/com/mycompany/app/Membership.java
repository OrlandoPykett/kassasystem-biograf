package com.mycompany.app;

import java.time.*;

public class Membership {
    private final String id;
    private LocalDateTime memberSince;
    private MembershipTier membershipTier;
    private Point point = Point.getZeroPoint();

    public Membership(String id) {
        this(id, LocalDateTime.now(), MembershipTier.REGULAR);
    }

    public Membership(String id, LocalDateTime memberSince) {
        this(id, memberSince, MembershipTier.REGULAR);
    }

    public Membership(String id, MembershipTier membershipTier) {
        this(id, LocalDateTime.now(),membershipTier);
    }

    private Membership(String id, LocalDateTime memberSince, MembershipTier membershipTier) {
        this.id = id;
        this.memberSince = memberSince;
        this.membershipTier = membershipTier;
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

    public void updateMembershipTier(MembershipTier membershipTier) {
        this.membershipTier = membershipTier;
    }
}
