package ua.gram.munhauzen.entity;

import ua.gram.munhauzen.MunhauzenGame;

public class StoryScenario extends StoryMedia<StoryScenario> {

    public int duration;
    public Scenario scenario;
    public StoryImage currentImage;
    public StoryAudio currentAudio;

    public void init(final int offset) {
        currentImage = null;
        currentAudio = null;

        int imageOffset = offset;
        int size = scenario.images.size;
        int imageDuration = 0;

        for (int i = 0; i < size; i++) {
            StoryImage current = scenario.images.get(i);

            if (MunhauzenGame.DEBUG) {
                if (current.duration == 0) {
                    current.duration = 1000;
                }
            }

            current.isLocked = false;
            current.isCompleted = false;

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

            current.startsAt = imageOffset;
            current.finishesAt = imageOffset += current.duration;

            if (i == size - 1) {
                current.finishesAt = Math.max(current.finishesAt, duration);

                imageDuration = current.finishesAt;
            }
        }

        int audioOffset = offset;
        size = scenario.audio.size;
        int audioDuration = 0;

        for (int i = 0; i < size; i++) {
            StoryAudio current = scenario.audio.get(i);

            if (MunhauzenGame.DEBUG) {
                if (current.duration == 0) {
                    current.duration = 2000;
                }
            }

            current.isLocked = false;
            current.isCompleted = false;

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

            current.startsAt = audioOffset;
            current.finishesAt = audioOffset += current.duration;

            if (i == size - 1) {
                current.finishesAt = Math.max(current.finishesAt, duration);

                audioDuration = current.finishesAt;
            }
        }

        duration = Math.max(imageDuration, audioDuration);
    }

    public void update(float progress, int max) {

        if ((int) progress == max) {
            currentImage = scenario.images.get(scenario.images.size - 1);
            currentAudio = scenario.audio.get(scenario.audio.size - 1);
        } else {
            currentImage = scenario.images.get(0);
            currentAudio = scenario.audio.get(0);
        }

        for (StoryImage item : scenario.images) {

            item.isCompleted = false;
            item.isLocked = false;
            item.progress = progress;

            if (item.startsAt <= progress && progress < item.finishesAt) {
                item.isLocked = true;
                currentImage = item;
            }

            if (progress >= item.finishesAt) {
                item.isCompleted = true;
            }
        }

        for (StoryAudio item : scenario.audio) {

            item.isCompleted = false;
            item.isLocked = false;
            item.progress = progress;

            if (item.startsAt <= progress && progress < item.finishesAt) {
                item.isLocked = true;
                currentAudio = item;
            }

            if (progress >= item.finishesAt) {
                item.isCompleted = true;
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
        }

        for (StoryAudio audio : scenario.audio) {
            audio.player = null;
            audio.isActive = false;
            audio.isLocked = false;
            audio.isCompleted = false;
        }
    }
}
