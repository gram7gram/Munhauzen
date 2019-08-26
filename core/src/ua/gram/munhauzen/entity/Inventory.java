package ua.gram.munhauzen.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Inventory extends Entity {

    @JsonProperty
    public boolean isMenu;
    @JsonProperty
    public boolean isStatue;
    @JsonProperty
    public ArrayList<String> relatedScenario;
    @JsonProperty
    public ArrayList<String> relatedInventory;
    @JsonProperty
    public ArrayList<StatueTranslation> statueTranslations;
    @JsonProperty
    public String statueImage;

    public Inventory() {
        relatedScenario = new ArrayList<>(2);
        relatedInventory = new ArrayList<>(2);
    }

    @JsonIgnore
    public boolean isGlobal() {
        return isMenu || isStatue;
    }

    @JsonIgnore
    public String getStatueDescription(String locale) {

        for (StatueTranslation translation : statueTranslations) {
            if (translation.locale.equals(locale)) {
                return translation.description.trim();
            }
        }

        return name;
    }

}
