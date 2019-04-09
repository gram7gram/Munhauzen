package ua.gram.munhauzen.entity;

import ua.gram.munhauzen.utils.StringUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Interaction {

    public String option;
    public String name;
    public boolean isLocked;

    public Interaction(String name) {
        this(name, StringUtils.cid());
    }

    public Interaction(String name, String option) {
        this.option = option;
        this.name = name;
    }

    @Override
    public String toString() {
        return option + "#" + name;
    }
}
