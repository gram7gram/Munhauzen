package ua.gram.munhauzen;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;

import ru.munchausen.fingertipsandcompany.full.BuildConfig;
import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.translator.RussianTranslator;

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
        params.device.type = Device.Type.android;
        params.isTablet = isTablet(this);
        params.applicationId = BuildConfig.APPLICATION_ID;
        params.tutorialLink = "https://youtu.be/6K__lu7QuLk";
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
