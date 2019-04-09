package ua.gram.munhauzen.entity;

public class TrackImage extends TrackMedia {

    public int duration;
    public String interaction;
    public String transition;
    public String altId;
    public Interaction interactionObject;

    @Override
    public int getDuration() {
        return duration;
    }
}
