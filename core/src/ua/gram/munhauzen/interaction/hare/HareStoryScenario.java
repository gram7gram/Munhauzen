package ua.gram.munhauzen.interaction.hare;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryMedia;

public class HareStoryScenario extends StoryMedia<HareStoryScenario> {

    public int duration;
    public HareScenario scenario;
    public StoryAudio currentAudio;

    public void init(final int offset) {
        currentAudio = null;

        int audioOffset = offset;
        int size = scenario.audio.size;
        int audioDuration = 0;

        for (int i = 0; i < size; i++) {
            StoryAudio current = scenario.audio.get(i);

            if (MunhauzenGame.DEBUG_OVERWRITE_DURATION) {
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

        duration = audioDuration;
    }

    public void update(float progress, int max) {

        if ((int) progress == max) {
            currentAudio = scenario.audio.get(scenario.audio.size - 1);
        } else {
            currentAudio = scenario.audio.get(0);
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
        currentAudio = null;

        isLocked = false;
        isCompleted = false;

        for (StoryAudio audio : scenario.audio) {
            audio.player = null;
            audio.isActive = false;
            audio.isLocked = false;
            audio.isCompleted = false;
        }
    }
}
