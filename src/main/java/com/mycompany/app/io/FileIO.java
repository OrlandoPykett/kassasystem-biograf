package com.mycompany.app.io;

import java.io.File;

/**
 * Real classes and test doubles of these real ones 
 * can implement this common interface.
 * Thus, a test double can be used wherever a real class 
 * is expected
 */

public interface FileIO {
    String readFile(File file);
    void writeToFile(File file);
}
