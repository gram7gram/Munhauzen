package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

public class Story {

    public String id;
    public int totalDuration;
    public boolean isCompleted;
    public float progress;
    public final Array<StoryScenario> scenarios;
    public StoryScenario currentScenario;
    public StoryInteraction currentInteraction;
    private boolean isInit;

    public Story() {
        scenarios = new Array<>();
    }

    public boolean isInteractionLocked() {
        if (currentInteraction == null) return false;

        return currentInteraction.isLocked;
    }

    public boolean isValid() {

        if (id == null || scenarios.size == 0) return false;

        return isInit
                && progress >= 0 && totalDuration >= 0
                && progress <= totalDuration
                && currentScenario != null;
    }

    public void init() {

        reset();

        int size = scenarios.size;
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

    public StoryScenario first() {
        if (scenarios.size == 0) return null;

        return scenarios.get(0);
    }

    public StoryScenario last() {
        if (scenarios.size == 0) return null;

        return scenarios.get(scenarios.size - 1);
    }

    public void update(float progress, int duration) {

        if (progress < 0) {
            progress = 0;
        }

        if (progress > duration) {
            progress = duration;
        }

        this.progress = progress;
        this.totalDuration = duration;

        isCompleted = progress >= duration;

        update();
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