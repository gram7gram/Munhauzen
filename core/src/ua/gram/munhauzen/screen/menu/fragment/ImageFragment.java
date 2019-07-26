package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

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

    public ImageFragment(MenuScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(screen);

        decorations = new Group();

        root = new FragmentRoot();
        root.addContainer(backgroundImage);
        root.addContainer(decorations);

        root.setName(tag);

        backgroundImage.setBackgroundDrawable(
                new SpriteDrawable(new Sprite(screen.assetManager.get("menu/mmv_fond_1.jpg", Texture.class)))
        );

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                displayImages();
            }
        }, 1f);
    }

    private void displayImages() {

        DecorationAnimation[] animations = {
                new Axe(backgroundImage, "menu/an_axe_sheet_3x3.png"),
                new Books(backgroundImage, "menu/an_books_sheet_3x2.png"),
                new Cannon(backgroundImage, "menu/an_cannon_sheet_5x2.png"),
                new Dog(backgroundImage, "menu/an_dog_sheet_3x1.png"),
                new EagleLeft(backgroundImage, "menu/an_eagle_1_sheet_3x2.png"),
                new EagleRight(backgroundImage, "menu/an_eagle_2_sheet_3x2.png"),
                new Fire(backgroundImage, "menu/an_fire_sheet_3x2.png"),
                new Horns(backgroundImage, "menu/an_horns_sheet_4x2.png"),
                new Horse(backgroundImage, "menu/an_horse_sheet_2x2.png"),
                new Painting(backgroundImage, "menu/an_painting_sheet_5x4.png"),
                new Puppet(backgroundImage, "menu/an_puppet_sheet_4x2.png"),
                new Rifle(backgroundImage, "menu/an_rifle_sheet_4x2.png"),
                new Sabre(backgroundImage, "menu/an_sabre_sheet_2x2.png"),
                new Scheme(backgroundImage, "menu/an_scheme_sheet_4x2.png"),
                new Worm(backgroundImage, "menu/an_worm_sheet_5x4.png"),
        };

        for (DecorationAnimation decoration : animations) {
            if (decoration.canBeDisplayed()) {

                Log.i(tag, decoration.getClass().getSimpleName() + " displayed");

                decoration.init();

                decorations.addActor(decoration);

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
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

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

            animate(screen.assetManager.get(resource, Texture.class), 2, 3, 6);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    18.083f, 18.038f, 4.083f, 29.311f,
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 2, 3, 6);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    17.750f, 18.464f, 77.667f, 29.982f,
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 3, 3, 9);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    17.667f, 17.124f, 82.417f, 12.919f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 2, 3, 6);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    14.167f, 7.678f, 72.833f, 53.687f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 2, 5, 10);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    19.500f, 6.155f, 84.250f, 47.288f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 1, 3, 3);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    9.083f, 8.227f, 8.052f, 46.313f
            };
        }
    }

    class Fire extends DecorationAnimation {

        public Fire(BackgroundImage backgroundImage, String resource) {
            super(backgroundImage, resource);
        }

        @Override
        public void init() {

            screen.assetManager.load(resource, Texture.class);

            screen.assetManager.finishLoading();

            animate(screen.assetManager.get(resource, Texture.class), 2, 3, 6);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    21.000f, 15.052f, 39.083f, 58.135f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 2, 4, 7);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    29.667f, 21.755f, 35.583f, 1.036f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 2, 2, 4);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    9.667f, 6.703f, 77.333f, 46.009f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 4, 5, 17);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    46.583f, 33.272f, 25.750f, 20.963f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 4, 5, 18);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    21.167f, 8.897f, 20.167f, 68.190f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 2, 4, 7);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    22.917f, 5.302f, 4.167f, 58.806f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 2, 2, 4);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    22.833f, 13.041f, 0.833f, 15.539f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 2, 4, 7);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    54.250f, 22.791f, 21.500f, 6.399f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 2, 4, 8);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    13.333f, 10.299f, 73.250f, 45.582f
            };
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

            animate(screen.assetManager.get(resource, Texture.class), 2, 3, 6);

            start();
        }

        @Override
        public boolean canBeDisplayed() {
            return true;
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    10.417f, 9.263f, 1.667f, 47.227f,
            };
        }
    }
}
