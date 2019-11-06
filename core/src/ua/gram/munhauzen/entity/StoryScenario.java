package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoryScenario extends StoryMedia<StoryScenario> {

    @JsonProperty
    public int duration;
    @JsonProperty
    public Scenario scenario;
    @JsonIgnore
    public StoryImage currentImage;
    @JsonIgnore
    public StoryAudio currentAudio;

    public void init() {
        reset();

        int size = scenario.images.size();
        int imageDuration = 0;

        for (int i = 0; i < size; i++) {
            StoryImage current = scenario.images.get(i);

            StoryImage next = null;
            StoryImage prev = null;
            if (size > 1) {
                if (i == 0) {
                    next = scenario.images.get(i + 1);
                } else if (i == size - 1) {
                    prev = scenario.images.get(i - 1);
                } else {
                    next = scenario.images.get(i + 1);
                    prev = scenario.images.get(i - 1);
                }
            }

            current.previous = prev;
            current.next = next;

            current.startsAt = prev != null ? prev.finishesAt : startsAt;
            current.finishesAt = current.startsAt + current.duration;

            imageDuration += current.duration;
        }

        size = scenario.audio.size();
        int audioDuration = 0;

        for (int i = 0; i < size; i++) {
            StoryAudio current = scenario.audio.get(i);

            StoryAudio next = null;
            StoryAudio prev = null;
            if (size > 1) {
                if (i == 0) {
                    next = scenario.audio.get(i + 1);
                } else if (i == size - 1) {
                    prev = scenario.audio.get(i - 1);
                } else {
                    next = scenario.audio.get(i + 1);
                    prev = scenario.audio.get(i - 1);
                }
            }

            current.previous = prev;
            current.next = next;

            current.startsAt = prev != null ? prev.finishesAt : startsAt;
            current.finishesAt = current.startsAt + current.duration;

            audioDuration += current.duration;
        }

        duration = Math.max(imageDuration, audioDuration);
        if (duration == 0) {
            duration = 500;
        }
    }

    public void update(float progress, int max) {
        currentImage = null;
        currentAudio = null;

        if (scenario.images != null) {
            if (scenario.images.size() > 0) {
                if ((int) progress == max) {
                    currentImage = scenario.lastImage();
                } else {
                    currentImage = scenario.images.get(0);
                }
            }

            for (StoryImage item : scenario.images) {

                item.isCompleted = false;
                item.isLocked = false;
                item.progress = progress;

                if (item.startsAt < progress && (scenario.lastImage() == item || progress <= item.finishesAt)) {
                    item.isLocked = true;
                    currentImage = item;
                }

                if (progress >= item.finishesAt) {
                    item.isCompleted = true;
                }
            }
        }

        if (scenario.audio != null) {
            if (scenario.audio.size() > 0) {
                if ((int) progress == max) {
                    currentAudio = scenario.lastAudio();
                } else {
                    currentAudio = scenario.audio.get(0);
                }
            }

            for (StoryAudio item : scenario.audio) {

                item.isCompleted = false;
                item.isLocked = false;
                item.progress = progress;

                if (item.startsAt < progress && (scenario.lastAudio() == item || progress <= item.finishesAt)) {
                    item.isLocked = true;
                    currentAudio = item;
                }

                if (progress >= item.finishesAt) {
                    item.isCompleted = true;
                }
            }
        }
    }

    public void reset() {
        currentImage = null;
        currentAudio = null;

        isLocked = false;
        isCompleted = false;

        for (StoryImage image : scenario.images) {
            image.drawable = null;
            image.isActive = false;
            image.isLocked = false;
            image.isCompleted = false;
            image.isPrepared = false;
            image.isPreparing = false;
        }

        for (StoryAudio audio : scenario.audio) {
            audio.player = null;
            audio.isActive = false;
            audio.isLocked = false;
            audio.isCompleted = false;
            audio.isPrepared = false;
            audio.isPreparing = false;
        }
    }
}
