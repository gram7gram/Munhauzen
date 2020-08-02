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
import ua.gram.munhauzen.translator.EnglishTranslator;

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
        params.storageDirectory = ".Munchausen/en.munchausen.fingertipsandcompany.any";
        params.locale = "en";
        params.appStoreSkuFull = "full_munchausen_audiobook_eng";
        params.appStoreSkuPart1 = "part_1_munchausen_audiobook_eng";
        params.appStoreSkuPart2 = "part_2_munchausen_audiobook_eng";
        params.iap = new PurchaseManageriOSApple();
        params.translator = new EnglishTranslator();
        params.appStore = new AppleStore(params);
        params.release = PlatformParams.Release.PROD;

        params.tutorialLink = "https://youtu.be/xg25QCxlvXM";
        params.fbLink = "https://www.facebook.com/101729401434875/photos/a.101737761434039/147391586868656/?type=3&xts%5B0%5D=68.ARAk1b34nsmLEQ-Qy1jLGgf5M_OS4Eu2bfkwpEyLcDot-rTuQV1p9diUrSyXxTr7FnK5gVC4KP-wxRZK1Ri6Hom0bEoHHn1ECJU8sqPo_tMbqy4LQv1NHNWSvTpnBVQ4DJGkLFyArtPSoRZPc4pp8XDLMNmtr7wN2Q-w4E2m77vbOrD8CyvHVRMs_zTnZbT9qIX3xJbNv4fqabs9CLQIYnK6hMLvkWUe8u1n32gShORJs1cc_sbj9kbDOxFOghMGyBJq9DCTVWxrdyvukwxeVeMCBXdk8f2N5acc-_jUiXeMpT5EBx_GBMEGIl7h_P0mdMUaDECe_LujIqs5uHausB8&tn=-R";
        params.instaLink = "https://www.instagram.com/p/CBdGkEWnj3A/?utm_source=ig_web_copy_link";
        params.twLink = "https://twitter.com/Finger_Tips_C/status/1272495846488264709?s=20";
        params.vkLink = "https://vk.com/wall374290107_10";
        params.statueLink = "https://youtu.be/a_8EMz8gKAE";

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