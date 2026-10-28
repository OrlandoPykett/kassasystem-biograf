package com.mycompany.app.util;

public class NumberOfCallsLogger implements Logger {
    private int numberOfCalls;

    @Override
    public void log(LogLevel level, String message) {
        numberOfCalls++;
    }

    public int getNumberOfCalls() {
        return numberOfCalls;
    }
}
