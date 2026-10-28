package com.mycompany.app;

public enum MembershipTier {
    VIP,
    PREMIUM,
    STANDARD;

    public static MembershipTier getDefault() {
        return STANDARD;
    }
}
