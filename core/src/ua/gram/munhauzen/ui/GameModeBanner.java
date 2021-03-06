package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.MunhauzenScreen;
import ua.gram.munhauzen.utils.Log;

public class GameModeBanner extends Banner<MunhauzenScreen> {

    final BannerFragment<?> fragment;
    final Runnable action;
    PrimaryButton btnOffline, btnOnline;

    public GameModeBanner(BannerFragment<?> fragment, Runnable action) {
        super(fragment.screen);

        this.fragment = fragment;
        this.action = action;
    }

    @Override
    public Texture getBackgroundTexture() {
        return screen.game.internalAssetManager.get("ui/banner_fond_0.png", Texture.class);
    }

    @Override
    public Table createContent() {

        float minWidth = MunhauzenGame.WORLD_WIDTH * .9f;
        float pad = MunhauzenGame.WORLD_WIDTH * .1f;

        Table content = new Table();
        content.pad(20, pad, 40, pad);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        Table rows = new Table();

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        Label.LabelStyle titleStyle = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        );

        Label title = new Label(screen.game.t("game_mode_banner.title"), titleStyle);
        title.setAlignment(Align.center);
        title.setWrap(true);

        rows.add(title)
                .minWidth(cellMinWidth - 40)
                .padBottom(10)
                .row();

        for (String sentence : screen.game.t("game_mode_banner.content").split("\n")) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            rows.add(label)
                    .minWidth(cellMinWidth - 40)
                    .padBottom(10)
                    .row();
        }

        Texture txt = screen.game.internalAssetManager.get("ui/banner_version.png", Texture.class);
        FixedImage img = new FixedImage(txt, cellMinWidth);

        btnOnline = screen.game.buttonBuilder.primary(
                screen.game.t("game_mode_banner.btn_online"),
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);

                        try {
                            btnOnline.setDisabled(true);

                            screen.game.stopCurrentSfx();

                            StoryAudio audio = null;
                            try {
                                if (!game.gameState.preferences.isGameModeSelected) {
                                    audio = screen.game.sfxService.onGameModeSwitch();
                                } else {
                                    if (!game.isOnlineMode()) {
                                        audio = screen.game.sfxService.onGameModeSwitch();
                                    } else {
                                        audio = screen.game.sfxService.onGameModeLeave();
                                    }
                                }
                            } catch (Throwable ignore) {
                            }

                            game.setGameMode(true);

                            Timer.Task task = new Timer.Task() {
                                @Override
                                public void run() {
                                    try {
                                        fragment.fadeOut(action);
                                    } catch (Throwable e) {
                                        Log.e(tag, e);

                                        screen.destroyBanners();
                                    }
                                }
                            };

                            try {
                                Timer.instance().scheduleTask(task, audio.duration / 1000f);
                            } catch (Throwable ignore) {
                                task.run();
                            }

                        } catch (Throwable e) {
                            Log.e(tag, e);

                            screen.destroyBanners();
                        }
                    }
                });

        btnOffline = screen.game.buttonBuilder.primary(
                screen.game.t("game_mode_banner.btn_offline"),
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);

                        try {

                            btnOffline.setDisabled(true);

                            screen.game.stopCurrentSfx();

                            StoryAudio audio = null;
                            try {
                                if (!game.gameState.preferences.isGameModeSelected) {
                                    audio = screen.game.sfxService.onGameModeSwitch();
                                } else {
                                    if (game.isOnlineMode()) {
                                        audio = screen.game.sfxService.onGameModeSwitch();
                                    } else {
                                        audio = screen.game.sfxService.onGameModeLeave();
                                    }
                                }
                            } catch (Throwable ignore) {
                            }

                            game.setGameMode(false);

                            Timer.Task task = new Timer.Task() {
                                @Override
                                public void run() {
                                    try {
                                        fragment.fadeOut(action);
                                    } catch (Throwable e) {
                                        Log.e(tag, e);

                                        screen.destroyBanners();
                                    }

                                }
                            };

                            try {
                                Timer.instance().scheduleTask(task, audio.duration / 1000f);
                            } catch (Throwable ignore) {
                                task.run();
                            }

                        } catch (Throwable e) {
                            Log.e(tag, e);

                            screen.destroyBanners();
                        }
                    }
                });

        Table columns = new Table();

        columns.add(rows)
                .minWidth(cellMinWidth)
                .center().row();

        Table buttons = new Table();

        buttons.add(btnOffline)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT)
                .pad(10);

        buttons.add(btnOnline)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT)
                .pad(10).row();

        content.add(columns).row();
        content.add(img).center().size(img.width, img.height)
                .pad(20, 0, 20, 0).row();
        content.add(buttons).row();

        return content;
    }

}
