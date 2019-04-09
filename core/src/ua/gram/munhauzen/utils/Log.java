package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Log {

    public static void e(String tag, Throwable e) {

//        ErrorMonitoring.capture(e);

        Log.e(tag, e.getClass().getSimpleName() + ": " + e.getMessage());
        e.printStackTrace(System.err);
    }

    public static void e(String tag, String message) {
        Gdx.app.error(tag, message);
    }

    public static void i(String tag, String message) {
        Gdx.app.log(tag, message);
    }
}
