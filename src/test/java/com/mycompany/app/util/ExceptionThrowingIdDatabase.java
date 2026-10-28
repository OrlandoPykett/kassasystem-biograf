package com.mycompany.app.util;

import java.net.SocketException;

public class ExceptionThrowingIdDatabase implements IdDatabase {
    @Override
    public boolean hasId(String id) throws SocketException {
        throw new SocketException();
    }
    
    @Override
    public void storeId(String id) throws SocketException {
        throw new SocketException();
    }
}
