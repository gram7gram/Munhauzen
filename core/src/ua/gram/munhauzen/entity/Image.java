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
    public boolean isAnimation;
    public Array<ImageTranslation> translations;


}
