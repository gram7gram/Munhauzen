package ua.gram.munhauzen.interaction.date.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.DateInteraction;
import ua.gram.munhauzen.interaction.date.CompleteDialog;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.ui.ProgressIconButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DateImageFragment extends InteractionFragment {

    enum SEASON {
        SUMMER, SPRING, AUTUMN, WINTER
    }
    final SEASON correctSeason = SEASON.SUMMER;
    SEASON currentSeason;

    private final DateInteraction interaction;
    public FragmentRoot root;
    public BackgroundImage backgroundImage;
    Image season1, season2, season3, season4;
    Table season1Table, season2Table, season3Table, season4Table, seasonsTable;
    Table seasonGroup, dialogContainer, dateContainer;
    ImageButton prevBtn, nextBtn;
    PrimaryButton confirmBtn;
    CompleteDialog completeDialog;
    Timer.Task failedTask;

    public DateImageFragment(DateInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        interaction.gameScreen.hideImageFragment();

        confirmBtn = interaction.gameScreen.game.buttonBuilder.primary(interaction.t("date_inter.confirm_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                confirmBtn.setDisabled(true);

                if (correctSeason == currentSeason) {
                    interaction.complete();
                } else {
                    failed();
                }
            }
        });

        season1 = new Image();
        season2 = new Image();
        season3 = new Image();
        season4 = new Image();

        season1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                setSeason(SEASON.SPRING);
            }
        });

        season2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                setSeason(SEASON.SUMMER);
            }
        });

        season3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                setSeason(SEASON.AUTUMN);
            }
        });

        season4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                setSeason(SEASON.WINTER);
            }
        });

        season1Table = new Table();
        season1Table.add(season1).expand();
        season2Table = new Table();
        season2Table.add(season2).expand();
        season3Table = new Table();
        season3Table.add(season3).expand();
        season4Table = new Table();
        season4Table.add(season4).expand();

        prevBtn = getPrev();
        nextBtn = getNext();

        nextBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                switch (currentSeason) {
                    case SPRING:
                        setSeason(SEASON.SUMMER);
                        break;
                    case SUMMER:
                        setSeason(SEASON.AUTUMN);
                        break;
                    case AUTUMN:
                        setSeason(SEASON.WINTER);
                        break;
                }
            }
        });

        prevBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                switch (currentSeason) {
                    case SUMMER:
                        setSeason(SEASON.SPRING);
                        break;
                    case AUTUMN:
                        setSeason(SEASON.SUMMER);
                        break;
                    case WINTER:
                        setSeason(SEASON.AUTUMN);
                        break;
                }
            }
        });

        seasonGroup = new Table();
        completeDialog = new CompleteDialog(interaction);

        seasonsTable = new Table();
        seasonsTable.add(prevBtn).left().expandX();
        seasonsTable.add(seasonGroup).center().expandX();
        seasonsTable.add(nextBtn).right().expandX();

        float pad = MunhauzenGame.WORLD_HEIGHT * .125f;

        dateContainer = new Table();
        dateContainer.add(seasonsTable).center().expandX()
                .padBottom(pad).row();
        dateContainer.add(confirmBtn).top().expandX()
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT);

        dialogContainer = new Table();

        Container<Table> c1 = new Container<>(dateContainer);
        c1.align(Align.top);
        c1.pad(10);
        c1.padTop(pad);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(backgroundImage);
        root.addContainer(c1);
        root.addContainer(dialogContainer);

        setBackground(
                interaction.assetManager.get("date/back.jpg", Texture.class),
                "date/back.jpg"
        );

        setSeason1Background(
                interaction.assetManager.get("date/inter_date_spring.png", Texture.class)
        );

        setSeason2Background(
                interaction.assetManager.get("date/inter_date_summer.png", Texture.class)
        );

        setSeason3Background(
                interaction.assetManager.get("date/inter_date_autumn.png", Texture.class)
        );

        setSeason4Background(
                interaction.assetManager.get("date/inter_date_winter.png", Texture.class)
        );

        start();
    }

    public void start() {

        confirmBtn.setDisabled(false);

        dateContainer.setVisible(true);
        dateContainer.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f)
        ));

        setSeason(SEASON.SPRING);
    }

    public void setSeason(SEASON season) {

        Log.i(tag, "season " + season);

        seasonGroup.clearChildren();

        switch (season) {
            case SPRING:
                seasonGroup.add(season1Table);
                break;
            case SUMMER:
                seasonGroup.add(season2Table);
                break;
            case AUTUMN:
                seasonGroup.add(season3Table);
                break;
            case WINTER:
                seasonGroup.add(season4Table);
                break;
        }

        currentSeason = season;

    }

    public void update() {

        nextBtn.setDisabled(SEASON.WINTER == currentSeason);
        prevBtn.setDisabled(SEASON.SPRING == currentSeason);

    }

    private void failed() {
        Log.i(tag, "failed");

        try {

            switch (currentSeason) {
                case SPRING:
                    interaction.playFailA();
                    break;
                case AUTUMN:
                    interaction.playFailC();
                    break;
                case WINTER:
                    interaction.playFailD();
                    break;
            }

            dateContainer.addAction(Actions.sequence(
                    Actions.alpha(0, .3f),
                    Actions.visible(false)
            ));

            failedTask = Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {

                    try {

                        interaction.playFailEnd();

                        failedTask = Timer.instance().scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {

                                try {

                                    completeDialog.create();

                                    dialogContainer.clearChildren();
                                    dialogContainer.add(completeDialog.getRoot());
                                } catch (Throwable e) {
                                    Log.e(tag, e);
                                }
                            }
                        }, interaction.storyAudio.duration / 1000f);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }, interaction.storyAudio.duration / 1000f);


        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void setBackground(Texture texture, String file) {

        backgroundImage.setBackgroundDrawable(new SpriteDrawable(new Sprite(texture)));

        interaction.gameScreen.setLastBackground(file);
    }

    public void setSeason1Background(Texture texture) {

        season1.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH * .75f;
        float scale = 1f * width / season1.getDrawable().getMinWidth();
        float height = 1f * season1.getDrawable().getMinHeight() * scale;

        season1Table.getCell(season1)
                .width(width)
                .height(height);
    }

    public void setSeason2Background(Texture texture) {

        season2.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH * .75f;
        float scale = 1f * width / season2.getDrawable().getMinWidth();
        float height = 1f * season2.getDrawable().getMinHeight() * scale;

        season2Table.getCell(season2)
                .width(width)
                .height(height);
    }

    public void setSeason3Background(Texture texture) {

        season3.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH * .75f;
        float scale = 1f * width / season3.getDrawable().getMinWidth();
        float height = 1f * season3.getDrawable().getMinHeight() * scale;

        season3Table.getCell(season3)
                .width(width)
                .height(height);
    }

    public void setSeason4Background(Texture texture) {

        season4.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH * .75f;
        float scale = 1f * width / season4.getDrawable().getMinWidth();
        float height = 1f * season4.getDrawable().getMinHeight() * scale;

        season4Table.getCell(season4)
                .width(width)
                .height(height);
    }


    @Override
    public void dispose() {
        super.dispose();

        if (failedTask != null) {
            failedTask.cancel();
            failedTask = null;
        }
    }


    private ImageButton getPrev() {
        Texture img = interaction.assetManager.get("ui/playbar_skip_backward.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(img));
        style.down = new SpriteDrawable(new Sprite(img));
        style.disabled = new SpriteDrawable(new Sprite(img));

        return new ProgressIconButton(style);
    }


    private ImageButton getNext() {
        Texture img = interaction.assetManager.get("ui/playbar_skip_forward.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(img));
        style.down = new SpriteDrawable(new Sprite(img));
        style.disabled = new SpriteDrawable(new Sprite(img));

        return new ProgressIconButton(style);
    }
}
