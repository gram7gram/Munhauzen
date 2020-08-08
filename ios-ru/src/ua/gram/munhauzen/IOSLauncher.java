package ua.gram.munhauzen;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.pay.ios.apple.PurchaseManageriOSApple;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIUserInterfaceIdiom;

import java.util.Set;

import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.translator.RussianTranslator;

public class IOSLauncher extends IOSApplication.Delegate {

    IOSApplicationConfiguration config;

    @Override
    protected IOSApplication createApplication() {
        config = new IOSApplicationConfiguration();

        config.orientationPortrait = true;
        config.orientationLandscape = false;
        config.useAccelerometer = false;
        config.useCompass = false;

        PlatformParams params = new PlatformParams();
        params.device.type = isIpad() ? Device.Type.ipad : Device.Type.ios;
        params.isTablet = params.device.type == Device.Type.ipad;
        params.scaleFactor = getDeviceScaleFactor();
        params.storageDirectory = ".Munchausen/ru.munchausen.fingertipsandcompany.any";
        params.locale = "ru";
        params.appStoreSkuFull = "full_munchausen_audiobook_ru";
        params.appStoreSkuPart1 = "part_1_munchausen_audiobook_ru";
        params.appStoreSkuPart2 = "part_2_munchausen_audiobook_ru";
        params.iap = new PurchaseManageriOSApple();
        params.translator = new RussianTranslator();
        params.appStore = new AppleStore(params);
        params.release = PlatformParams.Release.PROD;

        params.tutorialLink = "https://youtu.be/6K__lu7QuLk";
        params.fbLink = "https://www.facebook.com/thebaronmunchausen/photos/a.120269342878170/159930395578731/?type=3&xts%5B0%5D=68.ARDwKU7TTxCgmsz5T6W9Eu2xtm-Hv7chauuJWy_S5CIe9J7J3Rsl8CVsg1jHTERjW2pp3rM-6a5Iup5zR2SmytSr8v5lpnpl3RkXyxE1C6w-x79XqWzx4TRvwZtE_zhEJeIj7lIsceHQe-kBM88Dzz1Z8eLC-r7Z7NbVx7fZdizOFiEobj-hUDluar480Bim6q9hZi3gKs3ul4QUx8vVHZ2IKCoGc_5Vs6qucx07PsvoxnL67qDzxvMjdD7WyCKQaTHwxmrY6delninvJXNdG1lYpE9dP_YrVWO8ES7ZKMo5itape1BZGdURY8ha5jULztsps6zPpMF9725fXyq20AU&tn=-R";
        params.instaLink = "https://www.instagram.com/p/CBdHK7BnYHK/?utm_source=ig_web_copy_link";
        params.twLink = "https://twitter.com/Finger_Tips_C/status/1272495967951097856?s=20";
        params.vkLink = "https://vk.com/wall374290107_9";
        params.statueLink = "https://youtu.be/nxOFXw5Efzo";

        final NSDictionary<NSString, ?> infoDictionary = NSBundle.getMainBundle().getInfoDictionary();
        final Set<NSString> keys = infoDictionary.keySet();

        for (final NSString key : keys) {

            if (key.toString().equals("CFBundleVersion")) {
                final NSObject value = infoDictionary.get(key);
                params.versionCode = Integer.parseInt(value.toString());
            }
            if (key.toString().equals("CFBundleShortVersionString")) {
                final NSObject value = infoDictionary.get(key);
                params.versionName = value.toString();
            }
            if (key.toString().equals("CFBundleIdentifier")) {
                final NSObject value = infoDictionary.get(key);
                params.applicationId = value.toString();
            }
        }

        return new IOSApplication(new MunhauzenGame(params), config);
    }

    private float getDeviceScaleFactor() {
        float displayScaleFactor;

        float scale = (float) (getIosVersion() >= 8 ? UIScreen.getMainScreen().getNativeScale() : UIScreen.getMainScreen().getScale());
        if (scale >= 2.0F) {
            if (UIDevice.getCurrentDevice().getUserInterfaceIdiom() == UIUserInterfaceIdiom.Pad) {
                displayScaleFactor = config.displayScaleLargeScreenIfRetina * scale;
            } else {
                displayScaleFactor = config.displayScaleSmallScreenIfRetina * scale;
            }
        } else if (UIDevice.getCurrentDevice().getUserInterfaceIdiom() == UIUserInterfaceIdiom.Pad) {
            displayScaleFactor = config.displayScaleLargeScreenIfNonRetina;
        } else {
            displayScaleFactor = config.displayScaleSmallScreenIfNonRetina;
        }

        if (displayScaleFactor > 2) return 1.5f;
        return 1;
    }

    boolean isIpad() {
        return UIDevice.getCurrentDevice().getModel().toLowerCase().contains("ipad");
    }

    int getIosVersion() {
        String systemVersion = UIDevice.getCurrentDevice().getSystemVersion();
        return Integer.parseInt(systemVersion.split("\\.")[0]);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}