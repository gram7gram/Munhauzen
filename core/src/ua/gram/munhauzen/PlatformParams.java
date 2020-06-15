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
    public final int expansionVersion = 10;
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
    public String vkLink;
    @JsonIgnore
    public String instaLink;
    @JsonIgnore
    public String twLink;
    @JsonIgnore
    public String fbLink;

    @JsonIgnore
    public boolean isProduction() {
        return release == Release.PROD;
    }

    @JsonIgnore
    public boolean isDev() {
        return release == Release.DEV;
    }

}
