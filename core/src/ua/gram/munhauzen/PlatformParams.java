package ua.gram.munhauzen;

import ua.gram.munhauzen.utils.AppStore;
import ua.gram.munhauzen.utils.MemoryUsage;

public class PlatformParams {

    public AppStore appStore;
    public MemoryUsage memoryUsage;
    public String gameHost = "http://munhauzen-api.fingertips.cf";
    //public String gameHost = "http://78.27.147.177:20000";
    //public String gameHost = "http://192.168.100.247:20000";
    public String device = "phone";
    public String dpi = "hdpi";
    public String locale;
    public String versionName;
    public int versionCode;
    public boolean isPro;
    public String applicationId;
    public String applicationProId;
    public String applicationDemoId;

    public String getExpansionUrl() {
        return gameHost + "/api/v1/expansions/" + versionCode + "/" + locale + "/" + device + "/" + dpi;
    }

    public String getGameExportUrl() {
        return "http://munhauzen-api.fingertips.cf/api/v1/export";
    }
}
