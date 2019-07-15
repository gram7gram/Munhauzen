package ua.gram.munhauzen.interaction.hare;

import com.badlogic.gdx.utils.Array;

public class HareStory {

    public String id;
    public int totalDuration;
    public boolean isCompleted;
    public float progress;
    public final Array<HareStoryScenario> scenarios;
    public HareStoryScenario currentScenario;

    public HareStory() {
        scenarios = new Array<>();
    }

    public boolean isValid() {

        if (id == null || scenarios.size == 0) return false;

        //StoryScenario last = scenarios.get(scenarios.size - 1);

        return progress >= 0 && totalDuration >= 0
                && progress <= totalDuration
                && currentScenario != null;
    }

    public void init() {

        reset();

        int offset = 0;
        int size = scenarios.size;
        progress = 0;
        totalDuration = 0;

        for (int i = 0; i < size; i++) {
            HareStoryScenario current = scenarios.get(i);

            HareStoryScenario next = null;
            HareStoryScenario prev = null;
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

            current.init(offset);

            current.previous = prev;
            current.next = next;

            current.startsAt = offset;
            current.finishesAt = offset += current.duration;

            totalDuration += current.duration;
        }

        update();
    }

    public HareStoryScenario first() {
        if (scenarios.size == 0) return null;

        return scenarios.get(0);
    }

    public HareStoryScenario last() {
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

        for (HareStoryScenario item : scenarios) {

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