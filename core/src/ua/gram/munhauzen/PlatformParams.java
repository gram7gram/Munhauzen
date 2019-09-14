package ua.gram.munhauzen;

import ua.gram.munhauzen.utils.AppStore;
import ua.gram.munhauzen.utils.MemoryUsage;

public class PlatformParams {

    public AppStore appStore;
    public MemoryUsage memoryUsage;
    public String gameHost = "http://munhauzen-api.fingertips.cf";
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
        return gameHost + "/api/v1/expansions/" + versionCode + "/" + locale + "/" + dpi;
    }

    public String getGameExportUrl() {
        return "http://munhauzen-api.fingertips.cf/api/v1/export";
    }
}
