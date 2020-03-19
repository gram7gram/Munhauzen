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
    public String statueImage;
    @JsonProperty
    public String description;

    public Inventory() {
        if (relatedScenario == null)
            relatedScenario = new ArrayList<>(2);
        if (relatedInventory == null)
            relatedInventory = new ArrayList<>(2);
    }

    @JsonIgnore
    public boolean isGlobal() {
        return isMenu || isStatue;
    }

}
