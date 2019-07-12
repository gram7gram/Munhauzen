package ua.gram.munhauzen;

import ua.gram.munhauzen.utils.MemoryUsage;

public class PlatformParams {

    public MemoryUsage memoryUsage;
    public String gameHost = "http://78.27.147.177:20000";// http://munhauzen-api.fingertips.cf";
    public String device = "phone";
    public String dpi = "hdpi";
    public String locale;
    public String versionName;
    public int versionCode;
    public boolean isPro;

    public String getExpansionUrl() {
        return gameHost + "/api/v1/expansions/" + versionCode + "/" + locale + "/" + device + "/" + dpi;
    }

    public String getGameExportUrl() {
        return gameHost + "/api/v1/export";
    }
}
