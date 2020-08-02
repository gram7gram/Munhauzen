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

import ru.munchausen.fingertipsandcompany.full.BuildConfig;
import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.translator.RussianTranslator;

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            YandexMetrica.activate(getApplicationContext(),
                    YandexMetricaConfig.newConfigBuilder("7b7577d5-a605-465d-8fc7-f4d802d016a9").build());
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
        params.storageDirectory = ".Munchausen/ru.munchausen.fingertipsandcompany.any";
        params.versionCode = BuildConfig.VERSION_CODE;
        params.versionName = BuildConfig.VERSION_NAME;
        params.locale = "ru";
        params.appStoreSkuFull = "full_munchausen_audiobook_ru";
        params.appStoreSkuPart1 = "part_1_munchausen_audiobook_ru";
        params.appStoreSkuPart2 = "part_2_munchausen_audiobook_ru";
        params.iap = new PurchaseManagerGoogleBilling(this);
        params.translator = new RussianTranslator();
        params.memoryUsage = new AndroidMemoryUsage();
        params.appStore = new GooglePlay(params);

        params.tutorialLink = "https://youtu.be/6K__lu7QuLk";
        params.fbLink = "https://www.facebook.com/thebaronmunchausen/photos/a.120269342878170/159930395578731/?type=3&xts%5B0%5D=68.ARDwKU7TTxCgmsz5T6W9Eu2xtm-Hv7chauuJWy_S5CIe9J7J3Rsl8CVsg1jHTERjW2pp3rM-6a5Iup5zR2SmytSr8v5lpnpl3RkXyxE1C6w-x79XqWzx4TRvwZtE_zhEJeIj7lIsceHQe-kBM88Dzz1Z8eLC-r7Z7NbVx7fZdizOFiEobj-hUDluar480Bim6q9hZi3gKs3ul4QUx8vVHZ2IKCoGc_5Vs6qucx07PsvoxnL67qDzxvMjdD7WyCKQaTHwxmrY6delninvJXNdG1lYpE9dP_YrVWO8ES7ZKMo5itape1BZGdURY8ha5jULztsps6zPpMF9725fXyq20AU&tn=-R";
        params.instaLink = "https://www.instagram.com/p/CBdHK7BnYHK/?utm_source=ig_web_copy_link";
        params.twLink = "https://twitter.com/Finger_Tips_C/status/1272495967951097856?s=20";
        params.vkLink = "https://vk.com/wall374290107_9";
        params.statueLink = "https://youtu.be/nxOFXw5Efzo";

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
