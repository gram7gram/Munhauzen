package ua.gram.munhauzen.entity;

import java.util.ArrayList;
import java.util.List;

import ua.gram.munhauzen.utils.StringUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Branch {

    public static final String ACTION_GOTO = "goto";
    public static final String ACTION_CLICK = "click";

    public String cid;
    public String option;
    public String action;
    public List<InventoryBranch> options;

    public Branch() {
        options = new ArrayList<>();
    }

    public Branch(String option) {
        this();
        this.option = option;
    }

    public Branch(String option, String cid) {
        this(option);
        this.cid = cid;
    }

    public String getCid() {
        if (cid == null) {
            cid = StringUtils.cid();
        }

        return cid;
    }

    @Override
    public String toString() {
        return getSignature();
    }

    public String getSignature() {
        return cid + " " + option + " " + action;
    }

    public boolean isClick() {
        return ACTION_CLICK.equals(action);
    }

    public boolean isFinal() {
        return Option.DEATH.equals(option) || Option.VICTORY.equals(option);
    }
}
