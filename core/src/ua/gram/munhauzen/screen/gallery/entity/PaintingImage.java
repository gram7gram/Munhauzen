package ua.gram.munhauzen.screen.gallery.entity;

import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;

public class PaintingImage {
    public Image image;
    public Inventory inventory;
    public String imageResource, statueResource;
    public PaintingImage next, prev;
    public boolean isOpened, isViewed;

    public boolean isStatue() {
        return statueResource != null;
    }

    public boolean canDisplayStatueItem() {
        return isOpened && isStatue();
    }

    public boolean canOpenLink() {
        return inventory != null && "ST_SONG".equals(inventory.name);
    }
}
