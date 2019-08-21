package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Image extends Entity {

    public String type;
    public String file;
    public boolean isHiddenFromGallery;
    public Array<ImageTranslation> translations;
    public String relatedStatue;
    public String relatedScenario;

    public String getDescription(String locale) {

        for (ImageTranslation translation : translations) {
            if (translation.locale.equals(locale)) {
                return translation.description.trim();
            }
        }

        return name;
    }

    public boolean isBonus() {
        return "bonus".equals(type);
    }

    public boolean isStatue() {
        return "statue".equals(type);
    }

    public boolean isColor() {
        return "color".equals(type);
    }


}
