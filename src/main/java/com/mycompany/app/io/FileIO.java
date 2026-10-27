package com.mycompany.app.io;

import java.io.File;
import java.io.IOException;

/**
 * Real classes and test doubles of these real ones 
 * can implement this common interface.
 * Thus, a test double can be used wherever a real class 
 * is expected
 */

public interface FileIO {
    String readFile(File file) throws IOException;
    void writeToFile(File file) throws IOException;
}
