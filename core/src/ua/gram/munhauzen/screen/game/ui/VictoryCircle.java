package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.fragment.VictoryFragment;
import ua.gram.munhauzen.utils.Log;

public class VictoryCircle extends AnimatedImage {

    final GameScreen screen;

    String file1 = "victory/an_the_end_sheet_5x6_p1.png";
    String file2 = "victory/an_the_end_sheet_5x6_p2.png";
    String file3 = "victory/an_the_end_sheet_6x6_p3.png";

    final float delay = .08f;

    public VictoryCircle(VictoryFragment fragment) {
        super();

        this.screen = fragment.screen;

        loop = false;
    }

    @Override
    public void start() {

        startPart1();
    }

    public void dispose() {
        screen.internalAssetManager.unload(file1);
        screen.internalAssetManager.unload(file2);
        screen.internalAssetManager.unload(file3);
    }

    private void startPart1() {

        Log.i(tag, "startPart1");

        screen.internalAssetManager.load(file1, Texture.class);
        screen.internalAssetManager.load(file2, Texture.class);

        screen.internalAssetManager.finishLoading();

        animate(screen.internalAssetManager.get(file1, Texture.class), 5, 6, 30, delay);

        isStarted = true;

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                startPart2();
            }
        }, 30 * delay);
    }

    private void startPart2() {

        Log.i(tag, "startPart2");

        screen.internalAssetManager.unload(file1);

        screen.internalAssetManager.load(file2, Texture.class);
        screen.internalAssetManager.load(file3, Texture.class);

        screen.internalAssetManager.finishLoading();

        animate(screen.internalAssetManager.get(file2, Texture.class), 5, 6, 30, delay);

        isStarted = true;

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                startPart3();
            }
        }, 30 * delay);
    }

    private void startPart3() {

        Log.i(tag, "startPart3");

        screen.internalAssetManager.unload(file1);
        screen.internalAssetManager.unload(file2);

        screen.internalAssetManager.load(file3, Texture.class);

        screen.internalAssetManager.finishLoading();

        animate(screen.internalAssetManager.get(file3, Texture.class), 6, 6, 36, delay);

        isStarted = true;
    }

    public float getTotalDuration() {
        return 96 * delay;
    }
}
