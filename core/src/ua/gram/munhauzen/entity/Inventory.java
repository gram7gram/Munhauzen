package ua.gram.munhauzen.entity;


import java.util.ArrayList;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Inventory extends Entity {

    public boolean isMenu;
    public boolean isStatue;
    public ArrayList<String> relatedScenario;
    public ArrayList<String> relatedInventory;
    public ArrayList<StatueTranslation> statueTranslations;

    public Inventory() {
        relatedScenario = new ArrayList<>(2);
        relatedInventory = new ArrayList<>(2);
    }

    public boolean isGlobal() {
        return isMenu || isStatue;
    }

}
