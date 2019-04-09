package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

public class Scenario {

    public String cid;
    public int totalDuration;
    public boolean isCompleted;
    public int progress;
    public final Array<ScenarioOption> options;
    public ScenarioOption currentOption;
    private boolean isInit;

    public Scenario() {
        options = new Array<>();
    }

    public boolean isValid() {
        return isInit && options.size > 0
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

    public boolean update(int progress, int duration) {

        if (progress > duration) {
            progress = duration;
        }

        this.progress = progress;
        this.totalDuration = duration;

        isCompleted = progress >= duration;

        return update();
    }

    public void unlock() {
        for (ScenarioOption trackBranch : options) {
            trackBranch.isLocked = false;
        }
    }

    public boolean update() {
        String prevBranch = currentOption != null ? currentOption.id : null;

        currentOption = null;

        if (progress == 0) {
            currentOption = first();
        } else if (progress == totalDuration) {
            currentOption = last();
        }

        for (ScenarioOption trackBranch : options) {

            trackBranch.isCompleted = false;
            trackBranch.isLocked = false;
            trackBranch.progress = progress;

            if (trackBranch.startsAt <= progress && progress < trackBranch.finishesAt) {
                trackBranch.isLocked = true;

                currentOption = trackBranch;

            } else if (trackBranch.finishesAt <= progress) {
                trackBranch.isCompleted = true;
            }
        }

        return currentOption != null && !currentOption.id.equals(prevBranch);
    }
}