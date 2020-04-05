package ua.gram.munhauzen;

import com.badlogic.gdx.pay.PurchaseManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.entity.JsonEntry;
import ua.gram.munhauzen.translator.Translator;
import ua.gram.munhauzen.utils.AppStore;
import ua.gram.munhauzen.utils.MemoryUsage;

public class PlatformParams implements JsonEntry {

    enum Release {
        DEV, PROD, TEST
    }

    @JsonIgnore
    public Translator translator;
    @JsonIgnore
    public AppStore appStore;
    @JsonIgnore
    public MemoryUsage memoryUsage;
    @JsonIgnore
    public PurchaseManager iap;

    @JsonProperty
    public Release release = Release.PROD;
    @JsonProperty
    public final int expansionVersion = 7;
    @JsonProperty
    public final int gameConfigVersion = 1;
    @JsonProperty
    public int width, height;
    @JsonProperty
    public boolean isTablet;
    @JsonProperty
    public final Device device = new Device();
    @JsonProperty
    public String dpi = "mdpi";
    @JsonProperty
    public String locale;
    @JsonProperty
    public String versionName;
    @JsonProperty
    public String appStoreSkuFull, appStoreSkuPart1, appStoreSkuPart2;
    @JsonProperty
    public int versionCode;
    @JsonProperty
    public String applicationId;
    @JsonProperty
    public String storageDirectory;
    @JsonProperty
    public float scaleFactor = 1;

    @JsonIgnore
    public String tutorialLink;

    @JsonIgnore
    public final String sentryDsn = "https://aaab9a00313c443498afb6184a21c867@sentry.io/1802514";
    @JsonIgnore
    public final String vkLink = "https://vk.com/fingertipsandcompany?z=photo491072996_456239025%2Fb76a7cd5942e3325a8";
    @JsonIgnore
    public final String instaLink = "https://www.instagram.com/p/Bj2-Y58gPRR/";
    @JsonIgnore
    public final String twLink = "https://twitter.com/Finger_Tips_C/status/1005920810295611393";
    @JsonIgnore
    public final String fbLink = "https://www.facebook.com/photo.php?fbid=233858484056409&set=gm.253300848748389&type=3&theater&ifg=1";

    @JsonIgnore
    public boolean isProduction() {
        return release == Release.PROD;
    }

    @JsonIgnore
    public boolean isDev() {
        return release == Release.DEV;
    }

    @JsonIgnore
    public String getExpansionUrl(String productId) {
        return "https://api.thebaronmunchausen.com/api/v1/" + locale + "/expansions/" + expansionVersion + "/" + dpi + "?product=" + productId;
    }

    @JsonIgnore
    public String getGameExportUrl() {
        return "https://api.thebaronmunchausen.com/downloads/game-" + locale + "-" + gameConfigVersion + ".zip";
    }
}
