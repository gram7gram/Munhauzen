package ua.gram.munhauzen.utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class MathUtils {

    public static String round(float value, int digits) {
        return Float.toString(BigDecimal.valueOf(value)
                .setScale(digits, BigDecimal.ROUND_HALF_UP).floatValue());
    }

    public static <T> T random(T[] items) {
        Random random = new Random();
        int randomIndex = random.between(0, items.length - 1);
        return items[randomIndex];
    }

    public static <T> T random(List<T> items) {
        Random random = new Random();
        int randomIndex = random.between(0, items.size() - 1);
        return items.get(randomIndex);
    }
}
