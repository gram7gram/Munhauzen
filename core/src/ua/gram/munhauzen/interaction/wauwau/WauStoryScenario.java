package ua.gram.munhauzen.interaction.wauwau;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryMedia;
import ua.gram.munhauzen.repository.AudioRepository;

public class WauStoryScenario extends StoryMedia<WauStoryScenario> {

    GameState gameState;
    public int duration;
    public WauScenario scenario;
    public WauStoryImage currentImage;
    public StoryAudio currentAudio;

    public WauStoryScenario(GameState gameState) {
        this.gameState = gameState;
    }

    public void init() {
        currentImage = null;
        currentAudio = null;

        int size = scenario.images.size();
        int imageDuration = 0;

        for (int i = 0; i < size; i++) {
            WauStoryImage current = scenario.images.get(i);

            current.isLocked = false;
            current.isCompleted = false;

            WauStoryImage next = null;
            WauStoryImage prev = null;
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

            current.startsAt = prev != null ? prev.finishesAt : startsAt;
            current.finishesAt = current.startsAt + current.duration;

            audioDuration += current.duration;

        }

        duration = Math.max(imageDuration, audioDuration);
    }

    public void update(float progress, int max) {

        if ((int) progress == max) {
            currentImage = scenario.lastImage();
            currentAudio = scenario.lastAudio();
        } else {
            currentImage = scenario.firstImage();
            currentAudio = scenario.firstAudio();
        }

        for (WauStoryImage item : scenario.images) {

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

    public void reset() {
        currentImage = null;
        currentAudio = null;

        isLocked = false;
        isCompleted = false;

        for (WauStoryImage image : scenario.images) {
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
