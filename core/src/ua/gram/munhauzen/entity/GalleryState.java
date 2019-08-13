package ua.gram.munhauzen.entity;

import java.util.HashSet;

public class GalleryState {

    public HashSet<String> visitedImages;

    public GalleryState() {
        visitedImages = new HashSet<>();
    }
}
