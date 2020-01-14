package ua.gram.munhauzen.expansion.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import ua.gram.munhauzen.entity.JsonEntry;

public class Part implements JsonEntry {

    @JsonProperty
    public String url, checksum;

    @JsonProperty
    public double size;

    @JsonProperty
    public int part;

    @JsonProperty
    public String partKey;

    @JsonProperty
    public boolean isCompleted;

    @JsonProperty
    public boolean isDownloading;

    @JsonProperty
    public boolean isDownloaded;

    @JsonProperty
    public boolean isDownloadFailure;

    @JsonProperty
    public boolean isExtracting;

    @JsonProperty
    public boolean isExtracted;

    @JsonProperty
    public boolean isExtractFailure;
}
