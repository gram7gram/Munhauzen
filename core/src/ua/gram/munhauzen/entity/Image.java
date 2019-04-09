package ua.gram.munhauzen.entity;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Image extends Entity {

    public String file;
    public String description;
    public boolean isHidden;
    public boolean isAnimation;
    public boolean isCompleteBonus;
    public boolean isSuperBonus;
    public boolean isSpecialBonus;

    public boolean isBonus() {
        return isSuperBonus || isCompleteBonus || isSpecialBonus;
    }
}
