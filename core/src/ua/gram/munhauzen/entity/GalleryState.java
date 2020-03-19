package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class GalleryState implements JsonEntry {

    @JsonProperty
    public boolean hasUpdates;
    @JsonProperty
    public HashSet<String> visitedImages;
    @JsonProperty
    public float scrollY;
    @JsonProperty
    public boolean isGalleryBannerViewed;

    public GalleryState() {
        if (visitedImages == null)
            visitedImages = new HashSet<>();
    }
}
