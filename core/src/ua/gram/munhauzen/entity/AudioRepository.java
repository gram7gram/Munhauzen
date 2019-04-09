package ua.gram.munhauzen.entity;

public class AudioRepository {

    public static Audio find(GameState gameState, String id) {
        if (id == null) return null;

        for (Audio image : gameState.getAudioRegistry()) {
            if (id.equals(image.id)) {
                return image;
            }
        }

        return null;
    }
}
