package ua.gram.munhauzen;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import en.fingertips.munchausen.pro.BuildConfig;
import ua.gram.munhauzen.translator.EnglishTranslator;

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        config.useAccelerometer = false;
        config.useCompass = false;
        config.hideStatusBar = true;
        config.useWakelock = true;

        PlatformParams params = new PlatformParams();
        params.applicationId = BuildConfig.APPLICATION_ID;
        params.applicationProId = "en.fingertips.munchausen.pro";
        params.applicationDemoId = "en.fingertips.munchausen.demo";
        params.versionCode = BuildConfig.VERSION_CODE;
        params.versionName = BuildConfig.VERSION_NAME;
        params.locale = "en";
        params.isPro = true;
        params.translator = new EnglishTranslator();
        params.memoryUsage = new AndroidMemoryUsage();
        params.appStore = new AndroidAppStore(params, getApplicationContext());

        PermissionManager.grant(this, PermissionManager.PERMISSIONS);

        initialize(new MunhauzenGame(params), config);
    }
}
