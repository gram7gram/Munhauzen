package ua.gram.munhauzen.entity;

import java.util.ArrayList;

public class Scenario {

    public final ArrayList<TrackBranch> branches;
    public String cid;
    public int duration;
    public boolean isCompleted;
    public int progress;
    public TrackBranch currentBranch;
    private boolean isInit;

    public Scenario() {
        branches = new ArrayList<>();
    }

    public boolean isValid() {
        return isInit && branches.size() > 0
                && progress >= 0 && duration >= 0
                && currentBranch != null;
    }

    public void init() {
        int offset = 0;
        int size = branches.size();
        duration = 0;

        for (int i = 0; i < size; i++) {
            TrackBranch current = branches.get(i);

            TrackBranch next = null;
            TrackBranch prev = null;
            if (size > 1) {
                if (i == 0) {
                    next = branches.get(i + 1);
                } else if (i == size - 1) {
                    prev = branches.get(i - 1);
                } else {
                    next = branches.get(i + 1);
                    prev = branches.get(i - 1);
                }
            }

            current.previous = prev;
            current.next = next;

            current.startsAt = offset;
            current.finishesAt = offset += current.duration;

            duration += current.duration;
        }

        isInit = true;

        update();
    }

    public TrackBranch first() {
        if (branches.size() == 0) return null;

        return branches.get(0);
    }

    public TrackBranch last() {
        if (branches.size() == 0) return null;

        return branches.get(branches.size() - 1);
    }

    public boolean update(int progress, int duration) {

        if (progress > duration) {
            progress = duration;
        }

        this.progress = progress;
        this.duration = duration;

        isCompleted = progress >= duration;

        return update();
    }

    public void unlock() {
        for (TrackBranch trackBranch : branches) {
            trackBranch.isLocked = false;
        }
    }

    public boolean update() {
        String prevBranch = currentBranch != null ? currentBranch.id : null;

        currentBranch = null;

        if (progress == 0) {
            currentBranch = first();
        } else if (progress == duration) {
            currentBranch = last();
        }

        for (TrackBranch trackBranch : branches) {

            trackBranch.isCompleted = false;
            trackBranch.isLocked = false;
            trackBranch.progress = progress;

            if (trackBranch.startsAt <= progress && progress < trackBranch.finishesAt) {
                trackBranch.isLocked = true;

                currentBranch = trackBranch;

            } else if (trackBranch.finishesAt <= progress) {
                trackBranch.isCompleted = true;
            }
        }

        return currentBranch != null && !currentBranch.id.equals(prevBranch);
    }
}