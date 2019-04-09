package ua.gram.munhauzen.entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Image extends Entity {

    public HashMap<String, ArrayList<Sharing>> sharing;
    public String file;
    public String description;
    public boolean isHidden;
    public boolean isAnimation;
    public boolean isCompleteBonus;
    public boolean isSuperBonus;
    public boolean isSpecialBonus;
    public ArrayList<ArrayList<String>> requiredOptions;

    public Image() {
        super();
        requiredOptions = new ArrayList<>(3);
        sharing = new HashMap<>(3);
    }

    public boolean isBonus() {
        return isSuperBonus || isCompleteBonus || isSpecialBonus;
    }
}
