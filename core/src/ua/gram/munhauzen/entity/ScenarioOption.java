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
        currentImage = option.images.get(0);
        currentAudio = option.audio.get(0);

        if (progress == max) {
            currentImage = option.images.get(option.images.size - 1);
            currentAudio = option.audio.get(option.audio.size - 1);
        }

        for (OptionImage optionImage : option.images) {

            optionImage.isCompleted = false;
            optionImage.progress = progress;

            if (optionImage.startsAt <= progress && progress < optionImage.finishesAt) {
                currentImage = optionImage;
            }

            if (progress >= optionImage.finishesAt) {
                optionImage.isCompleted = true;
            }
        }

        for (OptionAudio optionAudio : option.audio) {

            optionAudio.isCompleted = false;
            optionAudio.progress = progress;

            if (optionAudio.startsAt <= progress && progress < optionAudio.finishesAt) {
                currentAudio = optionAudio;
            }

            if (progress >= optionAudio.finishesAt) {
                optionAudio.isCompleted = true;
            }
        }
    }

    public void reset() {
        currentImage = null;
        currentAudio = null;

        for (OptionImage image : option.images) {
            image.image = null;
            image.isLocked = false;
            image.isCompleted = false;
        }

        for (OptionAudio audio : option.audio) {
            audio.player = null;
            audio.isLocked = false;
            audio.isCompleted = false;
        }
    }
}
