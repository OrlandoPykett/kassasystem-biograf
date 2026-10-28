package com.mycompany.app.util;

import java.net.SocketException;
import java.util.HashSet;

public class IdDatabaseStub implements IdDatabase {
    private HashSet<String> ids = new HashSet<>();

    @Override
    public boolean hasId(String id) throws SocketException {
        return ids.contains(id);
    }

    @Override
    public void storeId(String id) throws SocketException {
        ids.add(id);
    }
}
