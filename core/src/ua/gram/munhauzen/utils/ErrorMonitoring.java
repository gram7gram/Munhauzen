package ua.gram.munhauzen.utils;

import io.sentry.Sentry;
import ua.gram.munhauzen.MunhauzenGame;

public class ErrorMonitoring {

    public ErrorMonitoring(MunhauzenGame game) {

        Sentry.init(game.params.sentryDsn);

        Sentry.getContext().addTag("release", game.params.release);
        Sentry.getContext().addTag("applicationId", game.params.applicationId);
        Sentry.getContext().addTag("versionCode", game.params.versionCode + "");
        Sentry.getContext().addTag("versionName", game.params.versionName + "");
        Sentry.getContext().addTag("dpi", game.params.dpi + "");
        Sentry.getContext().addTag("locale", game.params.locale + "");
    }

    public void capture(Throwable e) {
        Sentry.capture(e);
    }
}
