package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameVersionResponse implements JsonEntry {
    @JsonProperty
    public int versionCode;
}
