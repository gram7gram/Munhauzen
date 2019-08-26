package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class FailsState implements JsonEntry {

    @JsonProperty
    public HashSet<String> listenedAudio;
    @JsonProperty
    public boolean isMunhauzen;

    public FailsState() {
        listenedAudio = new HashSet<>();
        isMunhauzen = false;
    }
}
