package ua.gram.munhauzen;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import en.munchausen.fingertipsandcompany.full.BuildConfig;
import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.translator.EnglishTranslator;

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            YandexMetrica.activate(getApplicationContext(),
                    YandexMetricaConfig.newConfigBuilder("94888cd9-5fad-46a5-ac7f-eb03504fba4d").build());
            YandexMetrica.enableActivityAutoTracking(getApplication());
        } catch (Throwable ignore) {

        }

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        config.useAccelerometer = false;
        config.useCompass = false;
        config.hideStatusBar = true;
        config.useWakelock = true;

        PlatformParams params = new PlatformParams();
        params.device.type = Device.Type.android;
        params.isTablet = isTablet(this);
        params.applicationId = BuildConfig.APPLICATION_ID;
        params.storageDirectory = ".Munchausen/en.munchausen.fingertipsandcompany.any";
        params.versionCode = BuildConfig.VERSION_CODE;
        params.versionName = BuildConfig.VERSION_NAME;
        params.locale = "en";
        params.appStoreSkuFull = "full_munchausen_audiobook_eng";
        params.appStoreSkuPart1 = "part_1_munchausen_audiobook_eng";
        params.appStoreSkuPart2 = "part_2_munchausen_audiobook_eng";
        params.iap = new PurchaseManagerGoogleBilling(this);
        params.translator = new EnglishTranslator();
        params.memoryUsage = new AndroidMemoryUsage();
        params.appStore = new GooglePlay(params);

        params.tutorialLink = "https://youtu.be/xg25QCxlvXM";
        params.fbLink = "https://www.facebook.com/101729401434875/photos/a.101737761434039/147391586868656/?type=3&xts%5B0%5D=68.ARAk1b34nsmLEQ-Qy1jLGgf5M_OS4Eu2bfkwpEyLcDot-rTuQV1p9diUrSyXxTr7FnK5gVC4KP-wxRZK1Ri6Hom0bEoHHn1ECJU8sqPo_tMbqy4LQv1NHNWSvTpnBVQ4DJGkLFyArtPSoRZPc4pp8XDLMNmtr7wN2Q-w4E2m77vbOrD8CyvHVRMs_zTnZbT9qIX3xJbNv4fqabs9CLQIYnK6hMLvkWUe8u1n32gShORJs1cc_sbj9kbDOxFOghMGyBJq9DCTVWxrdyvukwxeVeMCBXdk8f2N5acc-_jUiXeMpT5EBx_GBMEGIl7h_P0mdMUaDECe_LujIqs5uHausB8&tn=-R";
        params.instaLink = "https://www.instagram.com/p/CBdGkEWnj3A/?utm_source=ig_web_copy_link";
        params.twLink = "https://twitter.com/Finger_Tips_C/status/1272495846488264709?s=20";
        params.vkLink = "https://vk.com/wall374290107_10";
        params.statueLink = "https://youtu.be/a_8EMz8gKAE";

        if (BuildConfig.BUILD_TYPE.equals("staging")) {
            params.release = PlatformParams.Release.TEST;
        } else if (BuildConfig.BUILD_TYPE.equals("release")) {
            params.release = PlatformParams.Release.PROD;
        } else {
            params.release = PlatformParams.Release.DEV;
        }

        PermissionManager.grant(this, PermissionManager.PERMISSIONS);

        initialize(new MunhauzenGame(params), config);
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        super.startActivity(intent, options);
    }
}
