package ua.gram.munhauzen;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();

        config.useAccelerometer = false;
        config.useCompass = false;

        PlatformParams params = new PlatformParams();
        params.versionCode = 1;
        params.versionName = "0.1.0";
        params.locale = "en";
        params.isPro = true;
        params.memoryUsage = new IOSMemoryUsage();

        return new IOSApplication(new MunhauzenGame(params), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}