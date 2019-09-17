package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Translation implements JsonEntry {
    @JsonProperty
    public String locale;
    @JsonProperty
    public String text;
}