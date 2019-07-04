package ua.gram.munhauzen;

import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;

import org.moe.natj.general.Pointer;

import apple.uikit.c.UIKit;

public class IOSMoeLauncher extends IOSApplication.Delegate {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();

        config.useAccelerometer = false;
        config.useCompass = false;
//        config. = true;

        PlatformParams params = new PlatformParams();
        params.versionCode = 1;
        params.versionName = "0.1.0";
        params.locale = "en";
        params.isPro = true;

        return new IOSApplication(new MunhauzenGame(params), config);
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
    }
}
