package ua.gram.munhauzen;

import ua.gram.munhauzen.utils.MemoryUsage;

public class PlatformParams {

    public MemoryUsage memoryUsage;
    public String gameHost = "http://munhauzen-api.fingertips.cf";
    public String locale;
    public String versionName;
    public int versionCode;
    public boolean isPro;

    public String getExpansionUrl() {
        return gameHost + "/expansions/" + versionCode + "-expansion.obb";
    }

    public String getGameConfigUrl() {
        return gameHost + "/downloads/game.zip?versionCode=" + versionCode;
    }
}
