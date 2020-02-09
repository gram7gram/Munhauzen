package ua.gram.munhauzen;

import com.badlogic.gdx.pay.PurchaseManager;

import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.translator.Translator;
import ua.gram.munhauzen.utils.AppStore;
import ua.gram.munhauzen.utils.MemoryUsage;

public class PlatformParams {

    enum Release {
        DEV, PROD, TEST
    }

    public Release release = Release.DEV;
    public final int expansionVersion = 2;
    public final int gameConfigVersion = 1;
    public Translator translator;
    public final Device device = new Device();
    public AppStore appStore;
    public MemoryUsage memoryUsage;
    public PurchaseManager iap;
    public String dpi = "mdpi";
    public String locale;
    public String versionName;
    public String appStoreSkuFull, appStoreSkuPart1, appStoreSkuPart2;
    public int versionCode;
    public final String sentryDsn = "https://aaab9a00313c443498afb6184a21c867@sentry.io/1802514";
    public String applicationId;
    public String storageDirectory;
    public float scaleFactor = 1;
    public String vkLink = "https://vk.com/fingertipsandcompany?z=photo491072996_456239025%2Fb76a7cd5942e3325a8";
    public String instaLink = "https://www.instagram.com/p/Bj2-Y58gPRR/";
    public String twLink = "https://twitter.com/Finger_Tips_C/status/1005920810295611393";
    public String fbLink = "https://www.facebook.com/photo.php?fbid=233858484056409&set=gm.253300848748389&type=3&theater&ifg=1";

    public boolean isProduction() {
        return release == Release.PROD;
    }

    public boolean isDev() {
        return release == Release.DEV;
    }

    public String getExpansionUrl(String productId) {
        return "https://api.thebaronmunchausen.com/api/v1/" + locale + "/expansions/" + expansionVersion + "/" + dpi + "?product=" + productId;
    }

    public String getGameExportUrl() {
        return "https://api.thebaronmunchausen.com/downloads/game-" + locale + "-" + gameConfigVersion + ".zip";
    }
}
