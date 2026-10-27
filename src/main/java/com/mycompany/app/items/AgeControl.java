package com.mycompany.app.items;

public class AgeControl {

    public boolean isAccepted(int age, AgeRequirement ageReq) {
        return ageReq.isOfAge(age);
    }
}
