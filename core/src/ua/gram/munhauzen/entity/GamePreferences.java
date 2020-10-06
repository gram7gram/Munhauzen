package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GamePreferences implements JsonEntry {
    @JsonProperty
    public boolean isLegalViewed;
    @JsonProperty
    public boolean isOnlineMode;
}
