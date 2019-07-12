package ua.gram.munhauzen.utils;

import com.badlogic.gdx.files.FileHandle;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

public class MD5 {

    static final String tag = "MD5";

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

    public static String get(FileHandle file) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            InputStream is = file.read();

            byte[] byteArray = new byte[1024];
            int bytesCount;

            while ((bytesCount = is.read(byteArray)) != -1) {
                md5.update(byteArray, 0, bytesCount);
            }

            is.close();

            return String.format("%032x", new BigInteger(1, md5.digest()));
        } catch (Throwable e) {
            Log.e(tag, e);

            return "";
        }
    }
}
