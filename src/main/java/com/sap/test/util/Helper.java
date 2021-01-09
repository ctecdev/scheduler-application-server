package com.sap.test.util;

import java.util.UUID;

public class Helper {

    public static boolean isUUID(String string) {
        try {
            UUID.fromString(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
