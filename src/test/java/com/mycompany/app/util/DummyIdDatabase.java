package com.mycompany.app.util;

import java.net.SocketException;

public class DummyIdDatabase implements IdDatabase {
    @Override
    public boolean hasId(String id) throws SocketException {
        // Do nothing
        return false;
    }

    @Override
    public void storeId(String id) throws SocketException {
        // Do nothing    
    }
}
