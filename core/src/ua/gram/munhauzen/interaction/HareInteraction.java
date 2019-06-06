package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import ua.gram.munhauzen.interaction.hare.animation.HareAnimation;
import ua.gram.munhauzen.interaction.hare.animation.HorseAnimation;
import ua.gram.munhauzen.interaction.hare.ui.Ground;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HareInteraction extends AbstractInteraction {

    Image ground;
    HareAnimation hare;
    HorseAnimation horse;

    public HareInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        assetManager.load("hare/hare_sheet_4x1.png", Texture.class);
        assetManager.load("hare/horse_sheet_5x1.png", Texture.class);
        assetManager.load("hare/inter_hare_ground.png", Texture.class);

        assetManager.finishLoading();

        Texture groundTexture = assetManager.get("hare/inter_hare_ground.png", Texture.class);
        Texture hareTexture = assetManager.get("hare/hare_sheet_4x1.png", Texture.class);
        Texture horseTexture = assetManager.get("hare/horse_sheet_5x1.png", Texture.class);

        ground = new Ground(groundTexture);
        hare = new HareAnimation(hareTexture);
        horse = new HorseAnimation(horseTexture);

        hare.start();
        horse.start();

        Stack stack = new Stack();
        stack.add(ground);
        stack.add(hare);
        stack.add(horse);

        gameScreen.gameLayers.setInteractionLayer(stack);
    }

    @Override
    public void update() {
        super.update();


    }
}
