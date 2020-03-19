package ua.gram.munhauzen.screen.chapters.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.interaction.InteractionFactory;
import ua.gram.munhauzen.screen.ChaptersScreen;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.StoryManager;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

public class LoadBanner extends Banner<ChaptersScreen> {

    final Chapter chapter;

    public LoadBanner(ChaptersScreen screen, Chapter chapter) {
        super(screen);

        this.chapter = chapter;
    }

    @Override
    public Texture getBackgroundTexture() {
        return screen.assetManager.get("ui/banner_fond_0.png", Texture.class);
    }

    @Override
    public Table createContent() {

        float minWidth = MunhauzenGame.WORLD_WIDTH * .9f;

        Table content = new Table();
        content.pad(20, MunhauzenGame.WORLD_WIDTH * .1f, 40, MunhauzenGame.WORLD_WIDTH * .1f);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        PrimaryButton yesBtn = screen.game.buttonBuilder.danger(screen.game.t("chapter_banner.yes_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    GameState state = screen.game.gameState;

                    Scenario match = null;
                    for (Scenario scenario : state.scenarioRegistry) {
                        if (InteractionFactory.CHAPTER.equals(scenario.interaction)) {
                            if (scenario.chapter.equals(chapter.name)) {
                                match = scenario;
                                break;
                            }
                        }
                    }

                    if (match == null) return;

                    screen.game.stopAllAudio();
                    screen.game.sfxService.onLoadOptionYesClicked();

                    try {
                        StoryManager storyManager = new StoryManager(null, state);
                        Story story = storyManager.create(match.name);

                        state.activeSave.story = story;
                        state.activeSave.chapter = chapter.name;
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }

                    try {
                        String[] inventory = new String[]{
                                "TEETH",
                                "KOZ",
                                "HALF_SLING",
                                "STRAW",
                                "BALLOON",
                                "LION_HANDS",
                                "LION_PUNCH",
                                "LION_HIT",
                                "LION_SKY",
                                "LION_SHOT",
                                "CARROT",
                                "WHIP",
                                "LETTER"
                        };

                        for (String s : inventory) {
                            state.history.globalInventory.remove(s);
                            state.activeSave.inventory.remove(s);
                        }
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }

                    screen.game.syncState();

                    screen.banner.fadeOut(new Runnable() {
                        @Override
                        public void run() {
                            screen.navigateTo(new GameScreen(screen.game));
                        }
                    });

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        PrimaryButton noBtn = screen.game.buttonBuilder.danger(screen.game.t("chapter_banner.no_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.game.stopAllAudio();
                    screen.game.sfxService.onSaveOptionNoClicked();

                    screen.banner.fadeOut(new Runnable() {
                        @Override
                        public void run() {
                            screen.destroyBanners();
                        }
                    });

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        Label title = new Label(screen.game.t("chapter_banner.title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        ));
        title.setAlignment(Align.center);
        title.setWrap(true);

        Table rows = new Table();
        for (String sentence : screen.game.t("chapter_banner.content").split("\n")) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            rows.add(label)
                    .minWidth(cellMinWidth - 40)
                    .padBottom(10)
                    .row();
        }

        Table columns = new Table();

        columns.add(title)
                .minWidth(cellMinWidth)
                .padBottom(10)
                .center().row();

        columns.add(rows)
                .minWidth(cellMinWidth)
                .center().row();

        Table buttons = new Table();
        buttons.add(yesBtn).pad(5)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT);
        buttons.add(noBtn).pad(5)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT);

        content.add(columns).row();
        content.add(buttons).row();

        return content;
    }

}
