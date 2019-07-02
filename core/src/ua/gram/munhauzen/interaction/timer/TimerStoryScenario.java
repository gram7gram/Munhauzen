package ua.gram.munhauzen.interaction.timer;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryMedia;
import ua.gram.munhauzen.repository.AudioRepository;

public class TimerStoryScenario extends StoryMedia<TimerStoryScenario> {

    final GameState gameState;
    public int duration;
    public TimerScenario scenario;
    public StoryImage currentImage;
    public StoryAudio currentAudio;

    public TimerStoryScenario(GameState gameState) {
        this.gameState = gameState;
    }

    public void init(final int offset) {
        currentImage = null;
        currentAudio = null;

        int imageOffset = offset;
        int size = scenario.images.size;
        int imageDuration = 0;

        for (int i = 0; i < size; i++) {
            StoryImage current = scenario.images.get(i);

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

            imageDuration += current.duration;

            if (i == size - 1) {
                current.finishesAt = Math.max(current.finishesAt, duration);
            }
        }

        int audioOffset = offset;
        size = scenario.audio.size;
        int audioDuration = 0;

        for (int i = 0; i < size; i++) {
            StoryAudio current = scenario.audio.get(i);

            Audio audio = AudioRepository.find(gameState, current.audio);
            current.duration = audio.duration;

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

            audioDuration += current.duration;

            if (i == size - 1) {
                current.finishesAt = Math.max(current.finishesAt, duration);
            }
        }

        duration = Math.max(imageDuration, audioDuration);
    }

    public void update(float progress, int max) {
        currentImage = null;
        currentAudio = null;

        if (scenario.images != null) {
            if (scenario.images.size > 0) {
                if ((int) progress == max) {
                    currentImage = scenario.images.get(scenario.images.size - 1);
                } else {
                    currentImage = scenario.images.get(0);
                }
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
        }

        if (scenario.audio != null) {
            if (scenario.audio.size > 0) {
                if ((int) progress == max) {
                    currentAudio = scenario.audio.get(scenario.audio.size - 1);
                } else {
                    currentAudio = scenario.audio.get(0);
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
