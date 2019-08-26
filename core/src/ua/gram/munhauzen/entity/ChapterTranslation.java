package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChapterTranslation implements JsonEntry {
    @JsonProperty
    public String locale;
    @JsonProperty
    public String description;
}