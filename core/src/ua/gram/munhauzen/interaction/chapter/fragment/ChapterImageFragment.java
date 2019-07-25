package ua.gram.munhauzen.interaction.chapter.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.ChapterTranslation;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.ChapterInteraction;
import ua.gram.munhauzen.repository.ChapterRepository;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.VerticalScrollPane;
import ua.gram.munhauzen.ui.WrapLabel;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ChapterImageFragment extends Fragment {

    private final ChapterInteraction interaction;
    public ScrollPane root;
    Timer.Task task;
    StoryAudio chapterAudio;

    public ChapterImageFragment(ChapterInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        interaction.gameScreen.hideImageFragment();

        Texture texFrame = interaction.assetManager.get("chapter/frame_2.png", Texture.class);

        Texture texHead;
        if (interaction.gameScreen.game.params.isPro) {
            texHead = interaction.assetManager.get("chapter/b_full_version_1.png", Texture.class);
        } else {
            texHead = interaction.assetManager.get("chapter/b_demo_version.png", Texture.class);
        }

        MunhauzenGame game = interaction.gameScreen.game;

        Story story = interaction.gameScreen.getStory();

        String chapterName = story.currentScenario.scenario.chapter;
        Chapter chapter = ChapterRepository.find(game.gameState, chapterName);

        Sprite frameBottomSprite = new Sprite(texFrame);
        frameBottomSprite.setFlip(false, true);

        float labelWidth = MunhauzenGame.WORLD_WIDTH * .9f;
        String headerPrefix = "intro".equals(chapter.name) ? "Part" : "Chapter";

        Label header = new WrapLabel(headerPrefix + " " + Math.max(1, chapter.number), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ), labelWidth);
        header.setAlignment(Align.center);

        String text = chapter.name;
        for (ChapterTranslation item : chapter.translations) {
            if (item.locale.equals(game.params.locale)) {
                text = item.description;
                break;
            }
        }

        Label description = new WrapLabel(text, new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ), labelWidth);
        description.setAlignment(Align.center);

        Image frameTop = new FitImage(texFrame);
        Image frameBottom = new FitImage(new SpriteDrawable(frameBottomSprite));
        Image head = new FitImage(texHead);

        Table table1 = new Table();
        table1.pad(10);
        table1.add(frameTop).center().height(80).padBottom(5).expandX().row();
        table1.add(header).width(labelWidth).expandX().row();
        table1.add(description).width(labelWidth).expandX().row();
        table1.add(frameBottom).center().height(80).padTop(5).expandX().row();

        Table table2 = new Table();
        table2.pad(10);
        table2.add(head).center().height(300).row();

        Table table3 = new Table();
        table3.pad(10);
        table3.add(table1).expand().row();
        table3.add(table2).row();

        root = new VerticalScrollPane(table3);
        root.setFillParent(true);

        root.setName(tag);

        header.addAction(Actions.alpha(0));
        header.addAction(Actions.sequence(
                Actions.alpha(1, .3f)
        ));

        description.addAction(Actions.alpha(0));
        description.addAction(Actions.sequence(
                Actions.delay(.2f),
                Actions.alpha(1, .3f)
        ));

        head.addAction(Actions.alpha(0));
        head.addAction(Actions.sequence(
                Actions.alpha(1, .3f)
        ));

        frameTop.addAction(Actions.alpha(0));
        frameTop.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, 10, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        frameBottom.addAction(Actions.alpha(0));
        frameBottom.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, -10, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        int duration = 3000;
        if (chapter.chapterAudio != null) {
            chapterAudio = new StoryAudio();
            chapterAudio.audio = chapter.chapterAudio;

            interaction.gameScreen.audioService.prepareAndPlay(chapterAudio);

            duration = chapterAudio.duration;
        }

        task = Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                try {

                    root.addAction(Actions.sequence(
                            Actions.alpha(0, .4f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        interaction.gameScreen.interactionService.complete();

                                        interaction.gameScreen.interactionService.findStoryAfterInteraction();

                                        interaction.gameScreen.restoreProgressBarIfDestroyed();
                                    } catch (Throwable e) {
                                        Log.e(tag, e);

                                        interaction.gameScreen.onCriticalError(e);
                                    }
                                }
                            })
                    ));
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }, duration / 1000f);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {
        if (chapterAudio != null) {
            interaction.gameScreen.audioService.updateVolume(chapterAudio);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (task != null) {
            task.cancel();
            task = null;
        }

        if (chapterAudio != null) {
            interaction.gameScreen.audioService.stop(chapterAudio);
            chapterAudio = null;
        }


    }
}
