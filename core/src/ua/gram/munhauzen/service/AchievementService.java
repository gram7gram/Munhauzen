package ua.gram.munhauzen.service;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Image;

public class AchievementService {

    final MunhauzenGame game;

    public AchievementService(MunhauzenGame game) {
        this.game = game;
    }

    public void onImageViewed(String name) {

        for (Image o : game.gameState.imageRegistry) {
            if (o.name.equals(name)) {
                game.gameState.history.viewedImages.add(name);
            }
        }
    }

}
