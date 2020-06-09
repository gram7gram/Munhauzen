package ua.gram.munhauzen;

import com.badlogic.gdx.pay.PurchaseManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

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
    public final int expansionVersion = 8;
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
        String part = "Part_demo";

        if (appStoreSkuFull.equals(productId)) {
            part = "Part_2";
        }
        if (appStoreSkuPart2.equals(productId)) {
            part = "Part_2";
        }
        if (appStoreSkuPart1.equals(productId)) {
            part = "Part_1";
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("en-hdpi-Part_1", googleDriveApi("130ZkNwW9kY-FmHbfOKEAEEZWwJQO7QzD"));
        map.put("en-hdpi-Part_2", googleDriveApi("1FNb7m4pOsRCfNdKWBNTy4JIorm7TiOOz"));
        map.put("en-hdpi-Part_demo", googleDriveApi("1hvaekabGsdrGVRMNs140ZrrahJzS1Bma"));
        map.put("en-mdpi-Part_1", googleDriveApi("1fWaAeVZfFYElClw3Cx5C2P9AHYt5Ipmy"));
        map.put("en-mdpi-Part_2", googleDriveApi("1vYyNWBxHeas8PhuScJNzA-jWXuH4TOve"));
        map.put("en-mdpi-Part_demo", googleDriveApi("1ObK4qupUoQV-Kc07zSQ1hErFCVGfAgCd"));
        map.put("ru-hdpi-Part_1", googleDriveApi("1I0y1ThRwSOtl8PyC85O-20mA1jShedkY"));
        map.put("ru-hdpi-Part_2", googleDriveApi("1zBjSKsptSb2HGmRcNRBSJK_ggaFtNqp1"));
        map.put("ru-hdpi-Part_demo", googleDriveApi("1UsBWeqMpPiEUZMDAInMnAbHv1uzzFEO2"));
        map.put("ru-mdpi-Part_1", googleDriveApi("1FI5sQF104UoHSgCNYcNe62muQnst8sXx"));
        map.put("ru-mdpi-Part_2", googleDriveApi("1uBHOpRlEcnkzP-HqaCLQ22VRwjag7nvQ"));
        map.put("ru-mdpi-Part_demo", googleDriveApi("1IVObYPDTM3Wc5KNjxUQiE1m4O9dopc8h"));

        return map.get(locale + "-" + dpi + "-" + part);
    }

    private String googleDriveApi(String id) {
        return "https://www.googleapis.com/drive/v3/files/" + id + "?alt=media&key=AIzaSyD7dkdttQpRuVLP-UhYU9DEWI6duFCO0lY";
    }
}
