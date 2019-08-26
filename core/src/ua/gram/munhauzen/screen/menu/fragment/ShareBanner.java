package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class ShareBanner extends Fragment {

    final MenuScreen screen;
    FragmentRoot root;
    Table content;
    public boolean isFadeIn;
    public boolean isFadeOut;
    StoryAudio introAudio, clickAudio;

    public ShareBanner(MenuScreen screen) {
        this.screen = screen;
    }

    public void create() {

        screen.assetManager.load("ui/banner_fond_1.png", Texture.class);
        screen.assetManager.load("menu/b_share_2.png", Texture.class);
        screen.assetManager.load("menu/vk_icon.jpg", Texture.class);
        screen.assetManager.load("menu/twitter_icon.jpg", Texture.class);
        screen.assetManager.load("menu/instagram_icon.jpg", Texture.class);
        screen.assetManager.load("menu/fb_icon.jpg", Texture.class);

        screen.assetManager.finishLoading();

        String[] sentences = {
                "Tell your friends about audio-book",
        };

        content = new Table();

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        for (String sentence : sentences) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            content.add(label)
                    .width(MunhauzenGame.WORLD_WIDTH / 3f)
                    .padBottom(10).growX().row();
        }

        FitImage img = new FitImage(
                screen.assetManager.get("menu/b_share_2.png", Texture.class)
        );

        Table columns = new Table();
        columns.pad(50, 100, 50, 100);
        columns.add(content).center().expandX();
        columns.add(img).width(MunhauzenGame.WORLD_WIDTH * .3f).center().expandX().row();

        columns.add(getFbBtn()).padBottom(5).padRight(5).growX();
        columns.add(getInstaBtn()).padBottom(5).padRight(5).growX().row();

        columns.add(getVkBtn()).padBottom(5).padRight(5).growX();
        columns.add(getTwBtn()).padBottom(5).padRight(5).growX().row();

        columns.setBackground(new SpriteDrawable(new Sprite(
                screen.assetManager.get("ui/banner_fond_1.png", Texture.class)
        )));

        Container<Table> container = new Container<>(columns);
        container.pad(MunhauzenGame.WORLD_WIDTH * .05f);
        container.setTouchable(Touchable.enabled);

        root = new FragmentRoot();
        root.addContainer(container);

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA4444);
        px.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, .3f);
        px.fill();

        container.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        container.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                Log.i(tag, "root clicked");

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        destroy();
                        screen.rateBanner = null;
                    }
                });
            }
        });

        root.setVisible(false);
    }

    public void update() {
        if (introAudio != null) {
            screen.audioService.updateVolume(introAudio);
        }
        if (clickAudio != null) {
            screen.audioService.updateVolume(clickAudio);
        }
    }

    public void fadeIn() {

        if (!isMounted()) return;
        if (isFadeIn) return;

        isFadeOut = false;
        isFadeIn = true;

        Log.i(tag, "fadeIn");

        root.setVisible(true);

        root.clearActions();
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));

        playIntro();
    }

    public void playIntro() {
        try {
            stopIntro();

            introAudio = new StoryAudio();
            introAudio.audio = "sfx_menu_share_0";

            screen.audioService.prepareAndPlay(introAudio);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void playClick() {
        try {
            stopClick();

            clickAudio = new StoryAudio();
            clickAudio.audio = "sfx_menu_share_1";

            screen.audioService.prepareAndPlay(clickAudio);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void stopIntro() {
        if (introAudio != null) {
            screen.audioService.stop(introAudio);
            introAudio = null;
        }
    }

    public void stopClick() {
        if (clickAudio != null) {
            screen.audioService.stop(clickAudio);
            clickAudio = null;
        }
    }

    public boolean canFadeOut() {
        return isMounted() && root.isVisible() && !isFadeOut;
    }

    public void fadeOut(Runnable task) {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

        root.clearActions();
        root.addAction(Actions.sequence(
                Actions.alpha(0, .5f),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                }),
                Actions.run(task)
        ));
    }

    public void fadeOut() {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

        root.clearActions();
        root.addAction(Actions.sequence(
                Actions.alpha(0, .5f),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

        stopIntro();

        stopClick();
    }

    private Actor getFbBtn() {
        Texture txt = screen.assetManager.get("menu/fb_icon.jpg", Texture.class);

        Label label = new Label("via Facebook", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.WHITE
        ));

        FitImage img = new FitImage(txt);

        Table table = new Table();
        table.add(img).width(80).height(80);
        table.add(label).padLeft(5).padRight(5).expandX();

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(.235f, .341f, .62f, 1);
        px.fill();

        table.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                onBtnCLicked("https://www.facebook.com/photo.php?fbid=233858484056409&set=gm.253300848748389&type=3&theater&ifg=1");

            }
        });

        return table;

    }

    private Actor getTwBtn() {
        Texture txt = screen.assetManager.get("menu/twitter_icon.jpg", Texture.class);

        Label label = new Label("via Twitter", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.WHITE
        ));

        FitImage img = new FitImage(txt);

        Table table = new Table();
        table.add(img).width(80).height(80);
        table.add(label).padLeft(5).padRight(5).expandX();

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(.337f, .557f, .788f, 1);
        px.fill();

        table.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                onBtnCLicked("https://twitter.com/Finger_Tips_C/status/1005920810295611393");
            }
        });

        return table;
    }

    private Actor getVkBtn() {
        Texture txt = screen.assetManager.get("menu/vk_icon.jpg", Texture.class);

        Label label = new Label("via Vkontakte", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.WHITE
        ));

        FitImage img = new FitImage(txt);

        Table table = new Table();
        table.add(img).width(80).height(80);
        table.add(label).padLeft(5).padRight(5).expandX();

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(.322f, .506f, .725f, 1);
        px.fill();

        table.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                onBtnCLicked("https://vk.com/fingertipsandcompany?z=photo491072996_456239025%2Fb76a7cd5942e3325a8");

            }
        });

        return table;
    }

    private Actor getInstaBtn() {
        Texture txt = screen.assetManager.get("menu/instagram_icon.jpg", Texture.class);

        Label label = new Label("via Instagram", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.WHITE
        ));

        FitImage img = new FitImage(txt);

        Table table = new Table();
        table.add(img).width(80).height(80);
        table.add(label).padLeft(5).padRight(5).expandX();

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(.31f, .498f, .655f, 1);
        px.fill();

        table.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                onBtnCLicked("https://www.instagram.com/p/Bj2-Y58gPRR/");

            }
        });

        return table;
    }

    private void onBtnCLicked(final String url) {

        screen.game.gameState.menuState.isShareViewed = true;

        root.setTouchable(Touchable.disabled);

        stopIntro();

        playClick();

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                try {

                    fadeOut(new Runnable() {
                        @Override
                        public void run() {
                            destroy();
                        }
                    });

                    Gdx.net.openURI(url);

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }, clickAudio.duration / 1000f);

    }
}
