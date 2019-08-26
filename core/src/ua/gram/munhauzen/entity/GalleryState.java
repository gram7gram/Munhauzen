package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class GalleryState implements JsonEntry {

    @JsonProperty
    public boolean hasUpdates;
    @JsonProperty
    public HashSet<String> visitedImages;

    public GalleryState() {
        visitedImages = new HashSet<>();
    }
}
