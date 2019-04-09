package ua.gram.munhauzen.entity;

public class ImageRepository {

    public static Image find(GameState gameState, final String id) {
        if (id == null) return null;

        for (Image image : gameState.getImageRegistry()) {
            if (id.equals(image.id)) {
                return image;
            }
        }

        return null;
    }

}
