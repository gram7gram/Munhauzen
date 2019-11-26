package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GamePreferences implements JsonEntry {
    @JsonProperty
    public String currentScreen;
    @JsonProperty
    public boolean isLegalViewed;
}
