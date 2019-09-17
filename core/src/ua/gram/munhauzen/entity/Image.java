package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Image extends Entity {

    @JsonProperty
    public String type;
    @JsonProperty
    public String file;
    @JsonProperty
    public boolean isHiddenFromGallery;
    @JsonProperty
    public String relatedStatue;
    @JsonProperty
    public String relatedScenario;
    @JsonProperty
    public String description;

    @JsonIgnore
    public boolean isBonus() {
        return "bonus".equals(type);
    }

    @JsonIgnore
    public boolean isStatue() {
        return "statue".equals(type);
    }

    @JsonIgnore
    public boolean isColor() {
        return "color".equals(type);
    }

}
