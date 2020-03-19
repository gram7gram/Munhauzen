package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class FailsState implements JsonEntry {

    @JsonProperty
    public HashSet<String> listenedAudio;
    @JsonProperty
    public boolean isMunhauzen;
    @JsonProperty
    public boolean hasUpdates;
    @JsonProperty
    public boolean isGoofsBannerViewed;

    public FailsState() {
        if (listenedAudio == null)
            listenedAudio = new HashSet<>();
        isMunhauzen = true;
    }
}
