package com.mycompany.app.util;

import java.net.SocketException;

public interface IdDatabase {
    boolean hasId(String id) throws SocketException;
    void storeId(String id) throws SocketException;
}
