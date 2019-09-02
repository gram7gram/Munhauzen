package ua.gram.munhauzen.expansion.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ua.gram.munhauzen.entity.JsonEntry;

public class ExpansionResponse implements JsonEntry {

    @JsonProperty
    public int version;

    @JsonProperty
    public String locale, device, dpi;

    @JsonProperty
    public double size;

    @JsonProperty
    public PartContainer parts;

    @JsonProperty
    public boolean isCompleted;

    @JsonProperty
    public boolean isDownloadStarted;

    @JsonProperty
    public int progress;

    @JsonIgnore
    public boolean isSameExpansion(ExpansionResponse expansion) {
        return version == expansion.version
                && locale.equals(expansion.locale)
                && device.equals(expansion.device)
                && dpi.equals(expansion.dpi)
//                && parts.count == expansion.parts.count
                ;
    }
}
