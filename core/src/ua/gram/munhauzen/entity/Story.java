package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Story implements JsonEntry {

    @JsonProperty
    public String id;
    @JsonProperty
    public int totalDuration;
    @JsonProperty
    public boolean isCompleted;
    @JsonProperty
    public float progress;
    @JsonProperty
    public final ArrayList<StoryScenario> scenarios;
    @JsonIgnore
    public StoryScenario currentScenario;
    @JsonProperty
    public StoryInteraction currentInteraction;
    @JsonIgnore
    private boolean isInit;

    public Story() {
        scenarios = new ArrayList<>();
    }

    @JsonIgnore
    public boolean isVictory() {
        for (StoryScenario storyScenario : scenarios) {
            if (storyScenario.scenario.isVictory()) {
                return true;
            }
        }

        return false;
    }

    @JsonIgnore
    public boolean isInteraction() {
        return scenarios.size() == 1 && first().scenario.interaction != null;
    }

    @JsonIgnore
    public boolean isInteractionLocked() {
        if (currentInteraction == null) return false;

        return currentInteraction.interaction != null && currentInteraction.isLocked;
    }

    @JsonIgnore
    public boolean isValid() {

        if (id == null || scenarios.size() == 0) return false;

        return isInit
                && progress >= 0 && totalDuration >= 0
                && progress <= totalDuration
                && currentScenario != null;
    }

    public void init() {

        reset();

        int size = scenarios.size();
        progress = 0;
        totalDuration = 0;

        for (int i = 0; i < size; i++) {
            StoryScenario current = scenarios.get(i);

            StoryScenario next = null;
            StoryScenario prev = null;
            if (size > 1) {
                if (i == 0) {
                    next = scenarios.get(i + 1);
                } else if (i == size - 1) {
                    prev = scenarios.get(i - 1);
                } else {
                    next = scenarios.get(i + 1);
                    prev = scenarios.get(i - 1);
                }
            }

            current.startsAt = prev != null ? prev.finishesAt : 0;

            current.init();

            current.previous = prev;
            current.next = next;

            current.finishesAt = current.startsAt + current.duration;

            totalDuration += current.duration;
        }

        isInit = true;

        update();
    }

    @JsonIgnore
    public StoryScenario first() {
        if (scenarios.size() == 0) return null;

        return scenarios.get(0);
    }

    @JsonIgnore
    public StoryScenario last() {
        if (scenarios.size() == 0) return null;

        return scenarios.get(scenarios.size() - 1);
    }

    public void update(float progress, int duration) {

        if (progress < 0) {
            progress = 0;
        }

        if (progress > duration) {
            progress = duration;
        }

        isCompleted = false;

        if (!isCurrentAudioLoaded()) return;

        this.progress = progress;
        this.totalDuration = duration;

        isCompleted = progress >= duration;

        update();
    }

    public boolean isCurrentAudioLoaded() {
        if (currentScenario != null) {
            if (currentScenario.currentAudio != null) {
                return currentScenario.currentAudio.isPrepared;
            }
        }

        return true;
    }

    public void reset() {
        progress = 0;
        isCompleted = false;
    }

    public void update() {

        int currentProgress = (int) progress;

        currentScenario = null;

        if (currentProgress == 0) {
            currentScenario = first();
        } else if (currentProgress == totalDuration) {
            currentScenario = last();
        }

        for (StoryScenario item : scenarios) {

            item.isCompleted = false;
            item.isLocked = false;
            item.progress = progress;

            if (item.startsAt <= progress && (last() == item || progress < item.finishesAt)) {
                item.isLocked = true;

                currentScenario = item;
            }

            if (progress >= item.finishesAt) {
                item.isCompleted = true;
            }

            item.update(progress, totalDuration);
        }

    }
}