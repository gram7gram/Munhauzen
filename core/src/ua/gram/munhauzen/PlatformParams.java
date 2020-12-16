package ua.gram.munhauzen;

import com.badlogic.gdx.pay.PurchaseManager;

import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.entity.JsonEntry;
import ua.gram.munhauzen.translator.Translator;
import ua.gram.munhauzen.utils.AppStore;
import ua.gram.munhauzen.utils.MemoryUsage;

public class PlatformParams implements JsonEntry {

    enum Release {
        DEV, PROD, TEST
    }

    public Translator translator;
    public AppStore appStore;
    // Not required
    public MemoryUsage memoryUsage = null;
    // Not required
    public PurchaseManager iap = null;

    public final boolean isStandaloneProVersion;
    public Release release = Release.PROD;
    public final int expansionVersion = 16;
    public int width, height;
    public final boolean isAdultGateEnabled = true;
    public boolean isTablet;
    public final Device device = new Device();
    public String dpi = "mdpi";
    public String locale;
    public String versionName;
    public String appStoreSkuFull,
            appStoreSkuPart1, appStoreSkuPart2,
            appStoreSku1Chapter, appStoreSku3Chapter, appStoreSku5Chapter, appStoreSku10Chapter, appStoreSkuThanks, appStoreSkuFullThanks;
    public int versionCode;
    public String applicationId;
    public String storageDirectory;
    public float scaleFactor = 1;
    public String yandexId;
    public String facebookId;
    public String appStoreId;

    public String tutorialLink;
    public String trailerLink;
    public final String sentryDsn = "https://aaab9a00313c443498afb6184a21c867@sentry.io/1802514";
    public final String googleApiKey = "AIzaSyD7dkdttQpRuVLP-UhYU9DEWI6duFCO0lY";
    public String vkLink;
    public String instaLink;
    public String twLink;
    public String fbLink;
    public String statueLink;

    public final String promocode1 = "KPI";
    public final String promocode2 = "Moustache";

    public int achievementPoints = 1;

    public PlatformParams() {
        this(false);
    }

    public PlatformParams(boolean isStandaloneProVersion) {
        this.isStandaloneProVersion = isStandaloneProVersion;
    }

    public boolean isProduction() {
        return release == Release.PROD;
    }

    public boolean isDev() {
        return release == Release.DEV;
    }

}
