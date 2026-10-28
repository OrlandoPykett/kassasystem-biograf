package com.mycompany.app.membership;

public enum MembershipTier {
    VIP,
    PREMIUM,
    STANDARD;

    public static MembershipTier getDefault() {
        return STANDARD;
    }
}
