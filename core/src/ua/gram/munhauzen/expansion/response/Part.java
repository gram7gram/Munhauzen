package ua.gram.munhauzen.expansion.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ua.gram.munhauzen.entity.JsonEntry;

public class Part implements JsonEntry {

    @JsonProperty
    public String url, checksum;

    @JsonIgnore
    public float downloadedMB, downloadSpeed;

    @JsonProperty
    public int part;

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
