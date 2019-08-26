package ua.gram.munhauzen.interaction.hare;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryMedia;
import ua.gram.munhauzen.repository.AudioRepository;

public class HareStoryScenario extends StoryMedia<HareStoryScenario> {

    GameState gameState;
    public int duration;
    public HareScenario scenario;
    public StoryAudio currentAudio;

    public HareStoryScenario(GameState gameState) {
        this.gameState = gameState;
    }

    public void init() {
        currentAudio = null;

        int size = scenario.audio.size();
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

        duration = audioDuration;
    }

    public void update(float progress, int max) {

        if ((int) progress == max) {
            currentAudio = scenario.lastAudio();
        } else {
            currentAudio = scenario.firstAudio();
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
