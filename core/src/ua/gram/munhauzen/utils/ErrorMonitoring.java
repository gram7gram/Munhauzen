package ua.gram.munhauzen.utils;

import com.badlogic.gdx.pay.FetchItemInformationException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashSet;

import io.sentry.Sentry;
import ua.gram.munhauzen.MunhauzenGame;

public class ErrorMonitoring {

    private final boolean canCapture;
    private static ErrorMonitoring instance;
    private static HashSet<Throwable> captured;

    public static ErrorMonitoring instance() {
        return instance;
    }

    public static void createInstance(MunhauzenGame game) {
        instance = new ErrorMonitoring(game);
    }

    private ErrorMonitoring(MunhauzenGame game) {

        Sentry.init(game.params.sentryDsn);

        captured = new HashSet<>();
        canCapture = !game.params.isDev();

        Sentry.getContext().addTag("release", game.params.release + "");
        Sentry.getContext().addTag("applicationId", game.params.applicationId);
        Sentry.getContext().addTag("expansionVersion", game.params.expansionVersion + "");
        Sentry.getContext().addTag("versionCode", game.params.versionCode + "");
        Sentry.getContext().addTag("versionName", game.params.versionName + "");
        Sentry.getContext().addTag("dpi", game.params.dpi + "");
        Sentry.getContext().addTag("locale", game.params.locale + "");
    }

    public static void destroy() {
        captured.clear();
        captured = null;
        instance = null;
    }

    public void capture(Throwable e) {

        if (!canCapture) return;

        if (e instanceof SocketException) return;
        if (e instanceof SocketTimeoutException) return;
        if (e instanceof MismatchedInputException) return;
        if (e instanceof FetchItemInformationException) return;
        if (e instanceof IOException) return;

        if (captured.contains(e)) return;

        captured.add(e);

        Sentry.capture(e);
    }
}
