package ua.gram.munhauzen.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JSON {

    public static final ObjectMapper om = new ObjectMapper();

    public static <T> T parse(String content, Class<T> valueType) {
        try {
            return om.readValue(content, valueType);
        } catch (Throwable ignore) {
            return null;
        }
    }

    public static <T> T parse(File content, Class<T> valueType) {
        try {
            return om.readValue(content, valueType);
        } catch (Throwable ignore) {
            return null;
        }
    }

    public static String stringify(Object obj) {
        try {

            return om.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Throwable ignore) {
            return null;
        }
    }

    public static void stringify(File stream, Object obj) {
        try {
            om.writeValue(stream, obj);
        } catch (Throwable ignore) {
        }
    }
}
