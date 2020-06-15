package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.BackgroundImage;
import ua.gram.munhauzen.screen.menu.ui.DecorationAnimation;
import ua.gram.munhauzen.screen.menu.ui.DecorationImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class ImageFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MenuScreen screen;
    public FragmentRoot root;
    public Group decorations;
    public BackgroundImage backgroundImage;
    StoryAudio puppetAudio;

    public ImageFragment(MenuScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(screen);

        decorations = new Group();

        root = new FragmentRoot();
        root.setTouchable(Touchable.disabled);
        root.addContainer(backgroundImage);
        root.addContainer(decorations);

        root.setName(tag);

        backgroundImage.setBackgroundDrawable(
                new SpriteDrawable(new Sprite(screen.assetManager.get("menu/mmv_fond_1.jpg", Texture.class)))
        );

        Timer.instance().postTask(new Timer.Task() {
            @Override
            public void run() {
                try {
                    displayImages();
                } catch (Throwable ignore) {}
            }
        });
    }

    private void displayImages() {

        if (screen.assetManager == null) return;

        DecorationAnimation[] animations = {
                new Axe(backgroundImage, "menu/an_axe_sheet_3x3.png"),
                new Books(backgroundImage, "menu/an_books_sheet_3x2.png"),
                new Cannon(backgroundImage, "menu/an_cannon_sheet_5x2.png"),
                new Dog(backgroundImage, "menu/an_dog_sheet_3x1.png"),
                new EagleLeft(backgroundImage, "menu/an_eagle_1_sheet_3x2.png"),
                new EagleRight(backgroundImage, "menu/an_eagle_2_sheet_3x2.png"),
                new Fire(backgroundImage, "menu/an_fire_sheet_3x2.png"),
                new Horns(backgroundImage, "menu/an_horns_sheet_2x5.png"),
                new Puppet(backgroundImage, "menu/an_puppet_sheet_3x3.png"),
                new Horse(backgroundImage, "menu/an_horse_sheet_2x2.png"),
                new Painting(backgroundImage, "menu/an_painting_sheet_5x4.png"),
                new Rifle(backgroundImage, "menu/an_rifle_sheet_4x2.png"),
                new Sabre(backgroundImage, "menu/an_sabre_sheet_2x2.png"),
                new Ring(backgroundImage, "menu/an_ring_sheet_3x3.png"),
                new Scheme(backgroundImage, "menu/an_scheme_sheet_4x2.png"),
                new Worm(backgroundImage, "menu/an_worm_sheet_5x4.png"),
                new Badge(backgroundImage, "menu/an_badge_sheet_1x5.png"),
                new Bear(backgroundImage, "menu/an_bear_sheet_3x5.png"),
                new Bomb(backgroundImage, "menu/an_bomb_sheet_2x19.png"),
                new CandleLeft(backgroundImage, "menu/an_candle_sheet_1x4.png"),
                new CandleRight(backgroundImage, "menu/an_candle_sheet_1x4.png"),
                new Clock(backgroundImage, "menu/an_clocks_sheet_3x4.png"),
        };

        for (final DecorationAnimation decoration : animations) {
            if (decoration.canBeDisplayed()) {

                try {
                    Log.i(tag, decoration.getClass().getSimpleName() + " displayed");

                    decoration.init();

                    float delay = decoration.getDelay();
                    float interval = decoration.getInterval();

                    if (interval > 0) {

                        Timer.instance().scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {
                                try {
                                    decoration.start();
                                } catch (Throwable ignore) {}
                            }
                        }, delay, interval);
                    } else {
                        decoration.start();
                    }

                    decorations.addActor(decoration);
                } catch (Throwable ignore) {}
            }
        }

        DecorationImage[] images = {
                new MunhauzenAndDaughter(backgroundImage, "menu/mmv_fond_2.png"),
        };

        for (DecorationImage decoration : images) {
            if (decoration.canBeDisplayed()) {

                Log.i(tag, decoration.getClass().getSimpleName() + " displayed");

                decoration.init();

                decorations.addActor(decoration);
            }
        }

        decorations.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .2f)
        ));
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        if (puppetAudio != null) {
            screen.audioService.updateVolume(puppetAudio);
        }

    }

    public boolean hasItem(String name) {
        try {
            Inventory item = InventoryRepository.find(screen.game.gameState, name);

            return screen.game.inventoryService.isInInventory(item);
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        return false;
    }

    public void fadeOut() {

        root.addAction(Actions.sequence(
                Actions.alpha(0, .5f),
                Actions.visible(false)
        ));
    }

    @Override
    public void dispose() {
        super.dispose();

        if (puppetAudio != null) {
            screen.audioService.stop(puppetAudio);
            puppetAudio = null;
        }
    }

    class MunhauzenAndDaughter extends DecorationImage {

        public MunhauzenAndDaughter(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            setDrawable(new SpriteDrawable(new Sprite(screen.assetManager.get(resource, Texture.class))));
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    27.667f, 35.771f, 53.167f, 45.765f,
            };
        }
    }

    class EagleLeft extends DecorationAnimation {

        public EagleLeft(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 3, 6, .18f);


        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("EAGLES");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    18.083f, 18.038f, 4.083f, 29.311f,
            };
        }

        @Override
        public float getInterval() {
            return 25;
        }

        @Override
        public float getDelay() {
            return 27;
        }
    }

    class CandleLeft extends DecorationAnimation {

        public CandleLeft(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            loop = true;

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 1, 4, 4, .09f);
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    10.083f, 13.894f, 31.917f, 45.094f
            };
        }

        @Override
        public float getInterval() {
            return 0;
        }

        @Override
        public float getDelay() {
            return 0;
        }
    }

    class CandleRight extends DecorationAnimation {

        public CandleRight(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            loop = true;

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 1, 4, 4, .09f);
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    10.083f, 13.894f, 58.333f, 45.094f
            };
        }

        @Override
        public float getInterval() {
            return 0;
        }

        @Override
        public float getDelay() {
            return 0;
        }
    }

    class Clock extends DecorationAnimation {

        public Clock(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            loop = true;

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 3, 4, 12, .08f);
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    20.417f, 10.908f, 39.333f, 47.959f
            };
        }

        @Override
        public float getInterval() {
            return 0;
        }

        @Override
        public float getDelay() {
            return 0;
        }
    }

    class EagleRight extends DecorationAnimation {

        public EagleRight(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 3, 6, .18f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("EAGLES");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    17.750f, 18.464f, 77.667f, 29.982f,
            };
        }

        @Override
        public float getInterval() {
            return 25;
        }

        @Override
        public float getDelay() {
            return 25;
        }
    }

    class Axe extends DecorationAnimation {

        public Axe(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 3, 3, 9, .1f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("AXE");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    17.667f, 17.124f, 80.25f, 12.919f
            };
        }

        @Override
        public float getInterval() {
            return 16;
        }

        @Override
        public float getDelay() {
            return 38;
        }
    }

    class Books extends DecorationAnimation {

        public Books(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);

        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 3, 6, .15f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("BOOKS");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    14.167f, 7.678f, 72.833f, 53.687f
            };
        }

        @Override
        public float getInterval() {
            return 16;
        }

        @Override
        public float getDelay() {
            return 21;
        }
    }

    class Cannon extends DecorationAnimation {

        public Cannon(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 5, 10, .11f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("CANNON");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    19.500f, 6.155f, 84.250f, 47.288f
            };
        }

        @Override
        public float getInterval() {
            return 10;
        }

        @Override
        public float getDelay() {
            return 5;
        }
    }

    class Dog extends DecorationAnimation {

        public Dog(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);

        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 1, 3, 3, .25f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("DOG");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    9.083f, 8.226f, 11f, 46.313f
            };
        }

        @Override
        public float getInterval() {
            return 8;
        }

        @Override
        public float getDelay() {
            return 8;
        }
    }

    class Fire extends DecorationAnimation {

        public Fire(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {
            loop = true;

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 3, 6, .15f);
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    19.42f, 13.89f, 39.58f, 59.35f,
            };
        }

        @Override
        public float getInterval() {
            return 0;
        }

        @Override
        public float getDelay() {
            return 0;
        }
    }

    class Horns extends DecorationAnimation {

        public Horns(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 5, 10, .08f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("HORNS");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    29.667f, 21.755f, 35.583f, 1.036f
            };
        }

        @Override
        public float getInterval() {
            return 11;
        }

        @Override
        public float getDelay() {
            return 33;
        }
    }

    class Horse extends DecorationAnimation {

        public Horse(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 2, 4, .14f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("HORSE");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    9.667f, 6.703f, 77.333f, 46.009f
            };
        }

        @Override
        public float getInterval() {
            return 6;
        }

        @Override
        public float getDelay() {
            return 11;
        }
    }

    class Painting extends DecorationAnimation {

        public Painting(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 6, 4, 22, .07f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("PAINTING");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    46.583f, 33.272f, 28.750f, 20.963f
            };
        }

        @Override
        public float getInterval() {
            return 18;
        }

        @Override
        public float getDelay() {
            return 18;
        }
    }

    class Worm extends DecorationAnimation {

        public Worm(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 4, 5, 18, .09f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("WORM");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    21.167f, 8.897f, 20.167f, 68.190f
            };
        }

        @Override
        public float getInterval() {
            return 40;
        }

        @Override
        public float getDelay() {
            return 40;
        }
    }

    class Scheme extends DecorationAnimation {

        public Scheme(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);

        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 4, 7, .12f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("SCHEME");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    22.917f, 5.302f, 4.167f, 58.806f
            };
        }

        @Override
        public float getInterval() {
            return 10;
        }

        @Override
        public float getDelay() {
            return 13;
        }
    }

    class Sabre extends DecorationAnimation {

        public Sabre(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 2, 4, .23f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("SABRE");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    22.833f, 13.041f, 0.833f, 15.539f
            };
        }

        @Override
        public float getInterval() {
            return 35;
        }

        @Override
        public float getDelay() {
            return 20;
        }
    }

    class Rifle extends DecorationAnimation {

        public Rifle(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 4, 7, .08f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("RIFLE");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    54.250f, 22.791f, 21.500f, 6.399f
            };
        }

        @Override
        public float getInterval() {
            return 21;
        }

        @Override
        public float getDelay() {
            return 14;
        }
    }

    class Puppet extends DecorationAnimation {

        public Puppet(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);

        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 3, 3, 9, .15f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("CROW");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    13.333f, 10.299f, 73.250f, 45.582f
            };
        }

        @Override
        public float getInterval() {
            return 60;
        }

        @Override
        public float getDelay() {
            return 60;
        }
    }

    class Badge extends DecorationAnimation {

        public Badge(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 1, 5, 5, .09f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("BADGE");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    10.417f, 9.263f, 1.667f, 47.227f
            };
        }

        @Override
        public float getInterval() {
            return 13;
        }

        @Override
        public float getDelay() {
            return 3;
        }
    }

    class Bear extends DecorationAnimation {

        public Bear(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 3, 5, 13, .16f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("BEAR");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    76.250f, 29.738f, 9.917f, 72.395f
            };
        }

        @Override
        public float getInterval() {
            return 9;
        }

        @Override
        public float getDelay() {
            return 16;
        }
    }

    class Bomb extends DecorationAnimation {

        public Bomb(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 19, 2, 38, .09f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("BOMB");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    100f, 12.431f, 0, 87.568f
            };
        }

        @Override
        public float getInterval() {
            return 19;
        }

        @Override
        public float getDelay() {
            return 29;
        }
    }

    class Ring extends DecorationAnimation {

        public Ring(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 3, 6, .13f);
        }

        @Override
        public boolean canBeDisplayed() {
            return hasItem("RING");
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    8.50f, 5.73f, 83.83f, 56.55f
            };
        }

        @Override
        public float getInterval() {
            return 7;
        }

        @Override
        public float getDelay() {
            return 7;
        }
    }
}
