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
    public String statueImage;

    public Inventory() {
        relatedScenario = new ArrayList<>(2);
        relatedInventory = new ArrayList<>(2);
    }

    public boolean isGlobal() {
        return isMenu || isStatue;
    }

    public String getStatueDescription(String locale) {

        for (StatueTranslation translation : statueTranslations) {
            if (translation.locale.equals(locale)) {
                return translation.description.trim();
            }
        }

        return name;
    }

}
