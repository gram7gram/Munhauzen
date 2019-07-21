package ua.gram.munhauzen.entity;

import java.util.Date;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class StoryMedia<T> {

    public int startsAt;
    public int finishesAt;
    public boolean isActive;
    public boolean isLocked;
    public boolean isCompleted;
    public float progress;
    public T previous;
    public T next;
    public boolean isPrepared;
    public boolean isPreparing;
    public Date prepareStartedAt;
    public Date prepareCompletedAt;
    public String resource;
}
