package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Log {

    private static final String prefix = "gamelog.";

    public static void e(String tag, Throwable e) {

        if (ErrorMonitoring.instance() != null)
            ErrorMonitoring.instance().capture(e);

        String trace = "";
        for (StackTraceElement tr : e.getStackTrace()) {
            trace += "\n" + prefix + "trace "+ tr.toString();
        }
        Log.e(tag, e.getClass().getSimpleName() + ": " + e.getMessage() + trace);

        Throwable cause = e.getCause();
        if (cause != null) {
            e(tag, cause);
        }
    }

    public static void e(String tag, String message) {
        Gdx.app.error(prefix + tag, message);
    }

    public static void i(String tag, String message) {
        Gdx.app.log(prefix + tag, message);
    }
}
