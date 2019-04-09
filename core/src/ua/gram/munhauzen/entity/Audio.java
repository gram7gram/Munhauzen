package ua.gram.munhauzen.entity;

import java.util.ArrayList;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Audio extends Entity {

    public String file;
    public String description;
    public int duration;
    public boolean isFail;
    public boolean isVideo;
    public ArrayList<String> fails;

    public Audio() {
        super();
        fails = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id != null ? id.toLowerCase() : null;
    }
}
