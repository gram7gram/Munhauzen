package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Image extends Entity {

    public String type;
    public String file;
    public String description;
    public boolean isHiddenFromGallery;
    public Array<ImageTranslation> translations;

    public String getDescription(String locale) {

        for (ImageTranslation translation : translations) {
            if (translation.locale.equals(locale)) {
                return translation.description.trim();
            }
        }

        return name;
    }


}
