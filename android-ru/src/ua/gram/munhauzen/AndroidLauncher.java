package ua.gram.munhauzen;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		config.useAccelerometer = false;
		config.useCompass = false;
		config.hideStatusBar = true;
		config.useWakelock = true;

		PlatformParams params = new PlatformParams();
		params.locale = "ru";

		initialize(new MunhauzenGame(params), config);
	}
}
