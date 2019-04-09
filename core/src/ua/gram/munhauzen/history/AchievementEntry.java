package ua.gram.munhauzen.history;

import ua.gram.munhauzen.utils.StringUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AchievementEntry extends Entry {

    public String type;
    public String value;
    public int reward;

    @SuppressWarnings("unused")
    public AchievementEntry() {
        super();
    }

    public AchievementEntry(String type, int reward) {
        this(type, reward, null);
    }

    public AchievementEntry(String type, int reward, String value) {
        super(StringUtils.cid());

        this.type = type;
        this.value = value;
        this.reward = reward;
    }

    @Override
    public String toString() {
        return super.toString() + " " + type + " " + reward + " " + value;
    }
}
