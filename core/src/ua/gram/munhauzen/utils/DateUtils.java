package ua.gram.munhauzen.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DateUtils {

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String now() {
        return now(FORMAT);
    }

    public static String now(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(new Date());
    }

    public static String format(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(toDate(date));
    }

    public static Date toDate(String value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT, Locale.ENGLISH);
        try {
            return dateFormat.parse(value);
        } catch (Throwable e) {
            return new Date();
        }
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        if (date1 == null || date2 == null) return 0;

        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}