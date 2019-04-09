package ua.gram.munhauzen.utils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String tag = ExceptionHandler.class.getSimpleName();
    private static ExceptionHandler handler;
    private final Thread.UncaughtExceptionHandler original;

    private ExceptionHandler() {
        this.original = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static void dispose() {
        Log.i(tag, "dispose");
        handler = null;
    }

    public static ExceptionHandler create() {
        Log.i(tag, "init");
        if (handler == null) {
            handler = new ExceptionHandler();
        }

        return handler;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        Log.e(tag, ex);

        original.uncaughtException(thread, ex);
    }
}
