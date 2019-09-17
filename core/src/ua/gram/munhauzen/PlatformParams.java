package ua.gram.munhauzen;

import ua.gram.munhauzen.utils.AppStore;
import ua.gram.munhauzen.utils.MemoryUsage;

public class PlatformParams {

    public AppStore appStore;
    public MemoryUsage memoryUsage;
    public String gameHost = "http://munchauzen-api.fingertips.cf";
    //public String gameHost = "http://78.27.147.177:20000";
    //public String gameHost = "http://192.168.100.115:20000";
    public String dpi = "mdpi";
    public String locale = "en";
    public String versionName;
    public int versionCode;
    public boolean isPro;
    public String applicationId;
    public String applicationProId;
    public String applicationDemoId;
    public float scaleFactor = 1;

    public String getExpansionUrl() {
        return gameHost + "/api/v1/" + locale + "/expansions/" + versionCode + "/" + dpi;
    }

    public String getGameExportUrl() {
        return gameHost + "/downloads/game-" + locale + "-" + versionCode + ".zip";
    }
}
