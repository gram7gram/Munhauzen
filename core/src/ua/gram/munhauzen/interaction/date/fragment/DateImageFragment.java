package ua.gram.munhauzen.interaction.date.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.DateInteraction;
import ua.gram.munhauzen.interaction.date.CompleteDialog;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DateImageFragment extends InteractionFragment {

    private final DateInteraction interaction;
    public FragmentRoot root;
    public BackgroundImage backgroundImage;
    Image season1, season2, season3, season4;
    Table season1Table, season2Table, season3Table, season4Table, seasonsTable;
    String currentSeason;
    Table seasonGroup, dialogContainer, dateContainer;
    ImageButton prevBtn, nextBtn;
    PrimaryButton confirmBtn;
    CompleteDialog completeDialog;
    StoryAudio failedAudio;
    Timer.Task failedTask;

    public DateImageFragment(DateInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        interaction.gameScreen.hideImageFragment();

        confirmBtn = interaction.gameScreen.game.buttonBuilder.primary("Confirm", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                confirmBtn.setDisabled(true);

                if ("summer".equals(currentSeason)) {
                    interaction.complete();
                } else {
                    failed();
                }
            }
        });

        season1 = new FitImage();
        season2 = new FitImage();
        season3 = new FitImage();
        season4 = new FitImage();

        season1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                setSeason("spring");
            }
        });

        season2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                setSeason("summer");
            }
        });

        season3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                setSeason("autumn");
            }
        });

        season4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                setSeason("winter");
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
                    case "spring":
                        setSeason("summer");
                        break;
                    case "summer":
                        setSeason("autumn");
                        break;
                    case "autumn":
                        setSeason("winter");
                        break;
                }
            }
        });

        prevBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                switch (currentSeason) {
                    case "summer":
                        setSeason("spring");
                        break;
                    case "autumn":
                        setSeason("summer");
                        break;
                    case "winter":
                        setSeason("autumn");
                        break;
                }
            }
        });

        seasonGroup = new Table();
        completeDialog = new CompleteDialog(interaction);

        seasonsTable = new Table();
        seasonsTable.add(prevBtn).left();
        seasonsTable.add(seasonGroup).center().grow();
        seasonsTable.add(nextBtn).right();

        dateContainer = new Table();
        dateContainer.pad(10);
        dateContainer.setFillParent(true);
        dateContainer.add(seasonsTable).center().grow().row();
        dateContainer.add(confirmBtn).top().expand()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 12f);

        dialogContainer = new Table();

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(backgroundImage);
        root.addContainer(dateContainer);
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

        setSeason("spring");
    }

    public void setSeason(String season) {

        Log.i(tag, "season " + season);

        seasonGroup.clearChildren();

        switch (season) {
            case "spring":
                seasonGroup.add(season1Table);
                break;
            case "summer":
                seasonGroup.add(season2Table);
                break;
            case "autumn":
                seasonGroup.add(season3Table);
                break;
            case "winter":
                seasonGroup.add(season4Table);
                break;
        }

        currentSeason = season;

    }

    public void update() {

        nextBtn.setDisabled("winter".equals(currentSeason));
        prevBtn.setDisabled("spring".equals(currentSeason));

        if (failedAudio != null) {
            interaction.gameScreen.audioService.updateVolume(failedAudio);
        }

    }

    private void failed() {
        Log.i(tag, "failed");

        try {

            playFailed();

            dateContainer.addAction(Actions.sequence(
                    Actions.alpha(0, .3f),
                    Actions.visible(false)
            ));

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
            }, failedAudio.duration / 1000f);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    private void playFailed() {
        try {
            failedAudio = new StoryAudio();
            failedAudio.audio = "smoon_incorrect";

            interaction.gameScreen.audioService.prepareAndPlay(failedAudio);

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

        float width = MunhauzenGame.WORLD_WIDTH / 2f;
        float scale = 1f * width / season1.getDrawable().getMinHeight();
        float height = 1f * season1.getDrawable().getMinWidth() * scale;

        season1Table.getCell(season1)
                .width(width)
                .height(height);
    }

    public void setSeason2Background(Texture texture) {

        season2.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH / 2f;
        float scale = 1f * width / season2.getDrawable().getMinHeight();
        float height = 1f * season2.getDrawable().getMinWidth() * scale;

        season2Table.getCell(season2)
                .width(width)
                .height(height);
    }

    public void setSeason3Background(Texture texture) {

        season3.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH / 2f;
        float scale = 1f * width / season3.getDrawable().getMinHeight();
        float height = 1f * season3.getDrawable().getMinWidth() * scale;

        season3Table.getCell(season3)
                .width(width)
                .height(height);
    }

    public void setSeason4Background(Texture texture) {

        season4.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH / 2f;
        float scale = 1f * width / season4.getDrawable().getMinHeight();
        float height = 1f * season4.getDrawable().getMinWidth() * scale;


        season4Table.getCell(season4)
                .width(width)
                .height(height);
    }


    @Override
    public void dispose() {
        super.dispose();

        if (failedAudio != null) {
            interaction.gameScreen.audioService.stop(failedAudio);
            failedAudio = null;
        }
        if (failedTask != null) {
            failedTask.cancel();
            failedTask = null;
        }
    }


    private ImageButton getPrev() {
        Texture skipBack = interaction.assetManager.get("ui/playbar_skip_backward.png", Texture.class);
        Texture skipBackOff = interaction.assetManager.get("ui/playbar_skip_backward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipBack));
        style.down = new SpriteDrawable(new Sprite(skipBack));
        style.disabled = new SpriteDrawable(new Sprite(skipBackOff));

        return new ImageButton(style);
    }


    private ImageButton getNext() {
        Texture skipBack = interaction.assetManager.get("ui/playbar_skip_forward.png", Texture.class);
        Texture skipBackOff = interaction.assetManager.get("ui/playbar_skip_forward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipBack));
        style.down = new SpriteDrawable(new Sprite(skipBack));
        style.disabled = new SpriteDrawable(new Sprite(skipBackOff));

        return new ImageButton(style);
    }
}
