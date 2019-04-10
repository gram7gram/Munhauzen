package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

public class Scenario {

    public String cid;
    public int totalDuration;
    public boolean isCompleted;
    public float progress;
    public final Array<ScenarioOption> options;
    public ScenarioOption currentOption;
    private boolean isInit;

    public Scenario() {
        options = new Array<>();
    }

    public boolean isValid() {

        if (options.size == 0) return false;

        ScenarioOption last = options.get(options.size - 1);

        return isInit
                && (last.option.action.equals("CLICK")) // || last.option.interaction ...
                && progress >= 0 && totalDuration >= 0
                && currentOption != null;
    }

    public void init() {
        int offset = 0;
        int size = options.size;
        totalDuration = 0;

        for (int i = 0; i < size; i++) {
            ScenarioOption current = options.get(i);

            ScenarioOption next = null;
            ScenarioOption prev = null;
            if (size > 1) {
                if (i == 0) {
                    next = options.get(i + 1);
                } else if (i == size - 1) {
                    prev = options.get(i - 1);
                } else {
                    next = options.get(i + 1);
                    prev = options.get(i - 1);
                }
            }

            current.init(offset);

            current.previous = prev;
            current.next = next;

            current.startsAt = offset;
            current.finishesAt = offset += current.duration;

            totalDuration += current.duration;
        }

        isInit = true;

        update();
    }

    public ScenarioOption first() {
        if (options.size == 0) return null;

        return options.get(0);
    }

    public ScenarioOption last() {
        if (options.size == 0) return null;

        return options.get(options.size - 1);
    }

    public void update(float progress, int duration) {

        if (progress > duration) {
            progress = duration;
        }

        this.progress = progress;
        this.totalDuration = duration;

        isCompleted = progress >= duration;

        update();
    }

    public void unlock() {
        for (ScenarioOption trackBranch : options) {
            trackBranch.isLocked = false;
        }
    }

    public void update() {

        int currentProgress = (int) progress;

        currentOption = null;

        if (currentProgress == 0) {
            currentOption = first();
        } else if (currentProgress == totalDuration) {
            currentOption = last();
        }

        for (ScenarioOption scenarioOption : options) {

            scenarioOption.isCompleted = false;
            scenarioOption.isLocked = false;
            scenarioOption.progress = progress;

            if (scenarioOption.startsAt <= progress && progress < scenarioOption.finishesAt) {
                scenarioOption.isLocked = true;

                currentOption = scenarioOption;

            } else if (scenarioOption.finishesAt <= progress) {
                scenarioOption.isCompleted = true;
            }
        }

    }
}