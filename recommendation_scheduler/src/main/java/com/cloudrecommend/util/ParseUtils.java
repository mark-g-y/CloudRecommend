package com.cloudrecommend.util;

public class ParseUtils {

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

}
