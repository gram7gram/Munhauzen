package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.hare.animation.HareAnimation;
import ua.gram.munhauzen.interaction.hare.animation.HorseAnimation;
import ua.gram.munhauzen.interaction.hare.ui.Ground;
import ua.gram.munhauzen.interaction.hare.ui.Misc;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HareInteraction extends AbstractInteraction {

    public HareInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        assetManager.load("hare/hare_sheet_4x1.png", Texture.class);
        assetManager.load("hare/horse_sheet_5x1.png", Texture.class);
        assetManager.load("hare/inter_hare_ground.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_1.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_2.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_3.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_4.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_5.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_6.png", Texture.class);

        assetManager.finishLoading();

        Texture groundTexture = assetManager.get("hare/inter_hare_ground.png", Texture.class);
        Texture hareTexture = assetManager.get("hare/hare_sheet_4x1.png", Texture.class);
        Texture horseTexture = assetManager.get("hare/horse_sheet_5x1.png", Texture.class);
        Texture miscTexture1 = assetManager.get("hare/inter_hare_misc_1.png", Texture.class);
        Texture miscTexture2 = assetManager.get("hare/inter_hare_misc_2.png", Texture.class);
        Texture miscTexture3 = assetManager.get("hare/inter_hare_misc_3.png", Texture.class);
        Texture miscTexture4 = assetManager.get("hare/inter_hare_misc_4.png", Texture.class);
        Texture miscTexture5 = assetManager.get("hare/inter_hare_misc_5.png", Texture.class);
        Texture miscTexture6 = assetManager.get("hare/inter_hare_misc_6.png", Texture.class);

        Image ground = new Ground(groundTexture);
        HareAnimation hare = new HareAnimation(hareTexture, ground);
        HorseAnimation horse = new HorseAnimation(horseTexture, ground);

        Image misc1 = new Misc(miscTexture1, ground, 100, 100, MunhauzenGame.WORLD_WIDTH/2, MunhauzenGame.WORLD_HEIGHT/2);
        Image misc2 = new Misc(miscTexture2, ground, 100, 100, MunhauzenGame.WORLD_WIDTH/2 -50, MunhauzenGame.WORLD_HEIGHT/2 -50);
        Image misc3 = new Misc(miscTexture3, ground, 100, 100,  MunhauzenGame.WORLD_WIDTH/2 + 50, MunhauzenGame.WORLD_HEIGHT/2-50);
        Image misc4 = new Misc(miscTexture4, ground, 100, 100,  MunhauzenGame.WORLD_WIDTH/2-100, MunhauzenGame.WORLD_HEIGHT/2-50);
        Image misc5 = new Misc(miscTexture5, ground, 100, 100,  MunhauzenGame.WORLD_WIDTH/2+100, MunhauzenGame.WORLD_HEIGHT/2-50);
        Image misc6 = new Misc(miscTexture6, ground, 100, 100,  MunhauzenGame.WORLD_WIDTH/2-150, MunhauzenGame.WORLD_HEIGHT/2-100);

        hare.start();
        horse.start();

        Stack stack = new Stack();
        stack.add(ground);
        stack.add(misc1);
        stack.add(misc2);
        stack.add(misc3);
        stack.add(misc4);
        stack.add(misc5);
        stack.add(misc6);
        stack.add(hare);
        stack.add(horse);

        gameScreen.gameLayers.setInteractionLayer(stack);
    }

    @Override
    public void update() {
        super.update();


    }
}
