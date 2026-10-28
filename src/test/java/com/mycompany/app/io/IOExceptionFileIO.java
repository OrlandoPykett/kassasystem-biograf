package com.mycompany.app.io;

import java.io.File;
import java.io.IOException;

public class IOExceptionFileIO implements FileIO {
    @Override
    public String readFile(File file) throws IOException {
        throw new IOException();
    }

    @Override
    public void writeToFile(File file) throws IOException {
        throw new IOException();
    }
}
