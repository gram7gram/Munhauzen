package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product implements JsonEntry {

    @JsonProperty
    public boolean isAvailable;
    @JsonProperty
    public boolean isDownloaded;
    @JsonProperty
    public String id;
    @JsonProperty
    public String localName;
    @JsonProperty
    public String localDescription;
    @JsonProperty
    public String localPricing;
}
