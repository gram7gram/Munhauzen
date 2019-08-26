package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageTranslation implements JsonEntry {
    @JsonProperty
    public String locale;
    @JsonProperty
    public String description;
}