package ua.gram.munhauzen.interaction.timer2;

import com.badlogic.gdx.utils.Array;

public class Timer2Story {

    public String id;
    public int totalDuration;
    public boolean isCompleted;
    public float progress;
    public final Array<Timer2StoryScenario> scenarios;
    public Timer2StoryScenario currentScenario;

    public Timer2Story() {
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
            Timer2StoryScenario current = scenarios.get(i);

            Timer2StoryScenario next = null;
            Timer2StoryScenario prev = null;
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

    public Timer2StoryScenario first() {
        if (scenarios.size == 0) return null;

        return scenarios.get(0);
    }

    public Timer2StoryScenario last() {
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

        for (Timer2StoryScenario item : scenarios) {

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