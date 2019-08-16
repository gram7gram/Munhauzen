package ua.gram.munhauzen.screen.gallery.entity;

import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;

public class PaintingImage {
    public Image image;
    public Inventory inventory;
    public String imageResource, statueResource;
    public PaintingImage next, prev;
    public boolean isOpened, isViewed;
}
