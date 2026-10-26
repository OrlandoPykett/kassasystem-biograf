package com.mycompany.app.util;

/**
 * Real classes and test doubles of these real ones 
 * can implement this common interface.
 * Thus, a test double can be used wherever a real class 
 * is expected
 */

public interface Logger {
    void log(LogLevel level, String message);
}

enum LogLevel {
    INFO,
    WARNING,
    CRITICAL
}
