package ua.gram.munhauzen.entity;

public class ScenarioOption extends OptionMedia<ScenarioOption> {

    public int duration;
    public Option option;
    public OptionImage currentImage;
    public OptionAudio currentAudio;

    public void init(final int offset) {
        currentImage = null;
        currentAudio = null;

        int imageOffset = offset;
        int size = option.images.size;
        int imageDuration = 0;

        for (int i = 0; i < size; i++) {
            OptionImage current = option.images.get(i);

            current.isLocked = false;
            current.isCompleted = false;

            OptionImage next = null;
            OptionImage prev = null;
            if (size > 1) {
                if (i == 0) {
                    next = option.images.get(i + 1);
                } else if (i == size - 1) {
                    prev = option.images.get(i - 1);
                } else {
                    next = option.images.get(i + 1);
                    prev = option.images.get(i - 1);
                }
            }

            current.previous = prev;
            current.next = next;

            current.startsAt = imageOffset;
            current.finishesAt = imageOffset += current.duration;

            if (i == size - 1) {
                current.finishesAt = Math.max(current.finishesAt, duration);

                imageDuration = current.finishesAt;
            }
        }

        int audioOffset = offset;
        size = option.audio.size;
        int audioDuration = 0;

        for (int i = 0; i < size; i++) {
            OptionAudio current = option.audio.get(i);

            current.isLocked = false;
            current.isCompleted = false;

            OptionAudio next = null;
            OptionAudio prev = null;
            if (size > 1) {
                if (i == 0) {
                    next = option.audio.get(i + 1);
                } else if (i == size - 1) {
                    prev = option.audio.get(i - 1);
                } else {
                    next = option.audio.get(i + 1);
                    prev = option.audio.get(i - 1);
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

        duration = Math.max(imageDuration, audioDuration);
    }

    public void update(float progress, int max) {

        if ((int) progress == max) {
            currentImage = option.images.get(option.images.size - 1);
            currentAudio = option.audio.get(option.audio.size - 1);
        } else {
            currentImage = option.images.get(0);
            currentAudio = option.audio.get(0);
        }

        for (OptionImage item : option.images) {

            item.isCompleted = false;
            item.isLocked = false;
            item.progress = progress;

            if (item.startsAt <= progress && progress < item.finishesAt) {
                item.isLocked = true;
                currentImage = item;
            }

            if (progress >= item.finishesAt) {
                item.isCompleted = true;
            }
        }

        for (OptionAudio item : option.audio) {

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
        currentImage = null;
        currentAudio = null;

        isLocked = false;
        isCompleted = false;

        for (OptionImage image : option.images) {
            image.image = null;
            image.isActive = false;
            image.isLocked = false;
            image.isCompleted = false;
        }

        for (OptionAudio audio : option.audio) {
            audio.player = null;
            audio.isActive = false;
            audio.isLocked = false;
            audio.isCompleted = false;
        }
    }
}
