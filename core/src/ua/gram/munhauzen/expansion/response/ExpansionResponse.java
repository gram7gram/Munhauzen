package ua.gram.munhauzen.expansion.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ua.gram.munhauzen.entity.JsonEntry;

public class ExpansionResponse implements JsonEntry {

    @JsonProperty
    public int version;

    @JsonProperty
    public String locale, dpi;

    @JsonProperty
    public long size;

    @JsonProperty
    public float sizeMB;

    @JsonProperty
    public PartContainer parts;

    @JsonProperty
    public boolean isCompleted;

    @JsonProperty
    public boolean isDownloadStarted;

    @JsonProperty
    public float progress;

    @JsonIgnore
    public boolean isSameExpansion(ExpansionResponse expansion) {
        return version == expansion.version
                && locale.equals(expansion.locale)
                && dpi.equals(expansion.dpi)
//                && parts.count == expansion.parts.count
                ;
    }
}
