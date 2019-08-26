package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

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
    public ArrayList<ImageTranslation> translations;
    @JsonProperty
    public String relatedStatue;
    @JsonProperty
    public String relatedScenario;

    @JsonIgnore
    public String getDescription(String locale) {

        for (ImageTranslation translation : translations) {
            if (translation.locale.equals(locale)) {
                return translation.description.trim();
            }
        }

        return name;
    }

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
