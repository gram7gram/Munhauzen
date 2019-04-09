package ua.gram.munhauzen.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

public class MD5 {

    public static String get() {
        return get(new Date().toString());
    }

    public static String get(String content) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(content.getBytes("UTF-8"));

            return String.format("%032x", new BigInteger(1, md5.digest()));
        } catch (Throwable e) {
            return "";
        }
    }
}
