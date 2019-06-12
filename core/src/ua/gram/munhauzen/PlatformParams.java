package ua.gram.munhauzen;

import ua.gram.munhauzen.utils.MemoryUsage;

public class PlatformParams {

    public MemoryUsage memoryUsage;
    public String gameConfigPath = "http://munhauzen-api.fingertips.cf/api/v1/export";
    public String gameExpansionPath = "http://munhauzen-api.fingertips.cf/expansions";
    public String locale;
    public String versionName;
    public int versionCode;
    public boolean isPro;

    public String getExpansionUrl() {
        return gameExpansionPath + "/" + versionCode + "-expansion.obb";
    }
}
