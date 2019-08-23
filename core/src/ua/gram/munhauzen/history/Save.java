package ua.gram.munhauzen.history;

import java.util.HashSet;
import java.util.Set;

import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.ServantsState;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.utils.DateUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Save {

    /**
     * Id of the save
     */
    public String id;
    /**
     * Date of last update
     */
    public String updatedAt;
    /**
     * Current chapter
     */
    public String chapter;
    /**
     * Last image from scenario of interaction
     */
    public Image lastImage;
    /**
     * Save order
     */
    public int order;
    /**
     * Information about current story
     */
    public Story story;
    /**
     * Obtained items on current iteration
     */
    public Set<String> inventory;

    public ServantsState servantsInteractionState;

    public Save() {
        this("1");
    }

    public Save(String id) {

        this.id = id;
        updatedAt = DateUtils.now();
        order = 0;
        inventory = new HashSet<>();
        story = new Story();
        servantsInteractionState = new ServantsState();
    }

    public void reset() {
        inventory.clear();

        updatedAt = DateUtils.now();

        story = new Story();
        servantsInteractionState = new ServantsState();
    }

}
