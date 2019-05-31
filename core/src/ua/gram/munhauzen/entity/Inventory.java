package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Inventory extends Entity {

    public String text;
    public boolean isMenu;
    public boolean isStatue;
    public Array<String> relatedScenario;
    public Array<String> relatedInventory;
    public Array<StatueTranslation> statueTranslations;

}
