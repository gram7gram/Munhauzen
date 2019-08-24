package ua.gram.munhauzen.entity;

import java.util.HashSet;

public class FailsState {

    public HashSet<String> listenedAudio;
    public boolean isDaughter = false;
    public boolean isMunhauzen = true;

    public FailsState() {
        listenedAudio = new HashSet<>();
    }
}
