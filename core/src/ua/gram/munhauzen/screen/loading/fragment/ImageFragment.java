package ua.gram.munhauzen.screen.loading.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.LoadingScreen;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.loading.ui.Axe;
import ua.gram.munhauzen.screen.loading.ui.Balloon;
import ua.gram.munhauzen.screen.loading.ui.Baron;
import ua.gram.munhauzen.screen.loading.ui.Bomb;
import ua.gram.munhauzen.screen.loading.ui.Clock;
import ua.gram.munhauzen.screen.loading.ui.Cloud;
import ua.gram.munhauzen.screen.loading.ui.Cup;
import ua.gram.munhauzen.screen.loading.ui.Dog;
import ua.gram.munhauzen.screen.loading.ui.DucksAnimation;
import ua.gram.munhauzen.screen.loading.ui.Hair;
import ua.gram.munhauzen.screen.loading.ui.Hat;
import ua.gram.munhauzen.screen.loading.ui.Hat1;
import ua.gram.munhauzen.screen.loading.ui.Hat2;
import ua.gram.munhauzen.screen.loading.ui.Hat3;
import ua.gram.munhauzen.screen.loading.ui.Moon;
import ua.gram.munhauzen.screen.loading.ui.NotRotatingObject;
import ua.gram.munhauzen.screen.loading.ui.Painting;
import ua.gram.munhauzen.screen.loading.ui.RotatingObject;
import ua.gram.munhauzen.screen.loading.ui.Sheep;
import ua.gram.munhauzen.screen.loading.ui.Shoes;
import ua.gram.munhauzen.screen.loading.ui.Shovel;
import ua.gram.munhauzen.screen.loading.ui.Statue;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ImageFragment extends InteractionFragment {

    final LoadingScreen screen;
    FragmentRoot root;
    Group items;
    DucksAnimation ducks;
    Balloon balloon;
    Random r;
    RotatingObject painting, hair, axe, bomb, clock, shoes, statue, moon;
    NotRotatingObject baron, cup, dog, shovel, sheep;
    int nextCount;

    public ImageFragment(LoadingScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        r = new Random();

        Texture cloud1Texture = screen.assetManager.get("loading/lv_cloud_1.png", Texture.class);
        Texture cloud2Texture = screen.assetManager.get("loading/lv_cloud_2.png", Texture.class);
        Texture cloud3Texture = screen.assetManager.get("loading/lv_cloud_3.png", Texture.class);

        ducks = new DucksAnimation(
                screen.assetManager.get("loading/ducks_sheet_1x6.png", Texture.class)
        );

        balloon = new Balloon(
                screen.assetManager.get("loading/lv_balloon.png", Texture.class)
        );

        final Hat1 hat1 = new Hat1(
                screen.assetManager.get("loading/lv_hat.png", Texture.class)
        );

        final Hat2 hat2 = new Hat2(
                screen.assetManager.get("loading/lv_hat2.png", Texture.class)
        );

        final Hat3 hat3 = new Hat3(
                screen.assetManager.get("loading/lv_hat3.png", Texture.class)
        );

        painting = new Painting(
                screen.assetManager.get("loading/an_painting.png", Texture.class)
        );

        hair = new Hair(
                screen.assetManager.get("loading/lv_hair.png", Texture.class)
        );

        axe = new Axe(
                screen.assetManager.get("loading/lv_axe.png", Texture.class)
        );

        bomb = new Bomb(
                screen.assetManager.get("loading/lv_bomb.png", Texture.class)
        );

        clock = new Clock(
                screen.assetManager.get("loading/lv_clocks.png", Texture.class)
        );

        shoes = new Shoes(
                screen.assetManager.get("loading/lv_shoes.png", Texture.class)
        );

        statue = new Statue(
                screen.assetManager.get("loading/lv_statue.png", Texture.class)
        );

        cup = new Cup(
                screen.assetManager.get("loading/lv_cup.png", Texture.class)
        );

        baron = new Baron(
                screen.assetManager.get("loading/lv_baron.png", Texture.class)
        );

        dog = new Dog(
                screen.assetManager.get("loading/lv_dog.png", Texture.class)
        );

        shovel = new Shovel(
                screen.assetManager.get("loading/lv_shovel.png", Texture.class)
        );

        sheep = new Sheep(
                screen.assetManager.get("loading/lv_sheep.png", Texture.class)
        );

        moon = new Moon(
                screen.assetManager.get("loading/lv_moon.png", Texture.class)
        );

        Cloud cloud1 = new Cloud(cloud1Texture, 200, 100, -100, MunhauzenGame.WORLD_HEIGHT * .9f);
        Cloud cloud2 = new Cloud(cloud2Texture, 200, 100, -200, MunhauzenGame.WORLD_HEIGHT * .8f);
        Cloud cloud3 = new Cloud(cloud3Texture, 200, 100, -180, MunhauzenGame.WORLD_HEIGHT * .75f);

        items = new Group();
        items.addActor(cloud1);
        items.addActor(cloud2);
        items.addActor(cloud3);
        items.addActor(ducks);
        items.addActor(balloon);
        items.addActor(hat1);
        items.addActor(hat2);
        items.addActor(hat3);
        items.addActor(painting);
        items.addActor(hair);
        items.addActor(bomb);
        items.addActor(clock);
        items.addActor(axe);
        items.addActor(shoes);
        items.addActor(statue);
        items.addActor(cup);
        items.addActor(baron);
        items.addActor(dog);
        items.addActor(sheep);
        items.addActor(shovel);
        items.addActor(moon);

        for (Actor child : items.getChildren()) {
            child.setVisible(false);
        }

        root = new FragmentRoot();
        root.addContainer(items);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (isMounted())
                    ducks.start();
            }
        }, 40, 40);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (isMounted())
                    balloon.start();
            }
        }, 60, 60);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {

                Hat next = MathUtils.random(new Hat[]{
                        hat1, hat2, hat3
                });

                if (isMounted()) {
                    next.start();
                } else {
                    cancel();
                }

            }
        }, 30, 25);

        cloud1.start();
        cloud2.start();
        cloud3.start();

        scheduleNext();
    }

    private void scheduleNext() {

        final RotatingObject[] items = new RotatingObject[]{
                hair, painting, bomb, clock, statue, baron, moon, sheep,
                axe, shoes, dog, cup, shovel,
        };

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {

                ++nextCount;

                if (nextCount == items.length) nextCount = 0;

                RotatingObject next = items[nextCount];
                if (isMounted()) {
                    next.start();
                } else {
                    cancel();
                }

            }
        }, 2, 5);
    }

    public void update() {

    }

    @Override
    public Actor getRoot() {
        return root;
    }
}
