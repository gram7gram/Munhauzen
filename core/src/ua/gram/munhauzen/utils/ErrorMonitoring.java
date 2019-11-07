package ua.gram.munhauzen.utils;

import io.sentry.Sentry;
import ua.gram.munhauzen.MunhauzenGame;

public class ErrorMonitoring {

    private final boolean canCapture;

    public ErrorMonitoring(MunhauzenGame game) {

        Sentry.init(game.params.sentryDsn);

        canCapture = !game.params.isDev();

        Sentry.getContext().addTag("release", game.params.release + "");
        Sentry.getContext().addTag("applicationId", game.params.applicationId);
        Sentry.getContext().addTag("versionCode", game.params.versionCode + "");
        Sentry.getContext().addTag("versionName", game.params.versionName + "");
        Sentry.getContext().addTag("dpi", game.params.dpi + "");
        Sentry.getContext().addTag("locale", game.params.locale + "");
    }

    public void capture(Throwable e) {

        if (!canCapture) return;

        Sentry.capture(e);
    }
}
