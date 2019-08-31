package ua.gram.munhauzen.screen.fails.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.repository.AudioFailRepository;
import ua.gram.munhauzen.screen.FailsScreen;
import ua.gram.munhauzen.screen.fails.entity.GalleryFail;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

public class AudioRow extends Stack {

    final String tag = getClass().getSimpleName();
    final FailsScreen screen;
    final GalleryFail fail;
    final int index;
    Label title, number;
    FitImage lock, unlock, play;
    Table content;
    float iconSize = 30;

    public AudioRow(final FailsScreen screen, final GalleryFail fail, int index, float width) {

        this.index = index;
        this.screen = screen;
        this.fail = fail;

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        number = new Label(index + ".", style);
        number.setWrap(false);
        number.setAlignment(Align.left);

        title = new Label("", style);
        title.setWrap(true);
        title.setAlignment(Align.left);

        play = new FitImage();
        unlock = new FitImage();

        lock = new FitImage();
        lock.setRotation(-15);

        content = new Table();

        float lblWidth = width - iconSize - number.getWidth();

        content.add().width(iconSize).padTop(15).padRight(5).align(Align.topLeft);
        content.add(number).align(Align.topLeft).padRight(5);
        content.add(title)
                .width(lblWidth)
                .grow()
                .align(Align.topLeft).row();

        addActor(content);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.game.sfxService.onListItemClicked();

                    screen.stopAll();

                    screen.audioService.prepareAndPlay(fail.storyAudio);

                    fail.isPlaying = true;
                    fail.isListened = true;

                    screen.game.gameState.failsState.listenedAudio.add(fail.storyAudio.audio);

                    lock.remove();
                    unlock.remove();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        init();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        play.setSize(iconSize, iconSize);

        if (fail.isPlaying) {
            play.setVisible(true);
            content.getCells().get(0).setActor(play);
            content.getCells().get(0).padTop(0);
        } else {
            play.setVisible(false);
            play.remove();
        }
    }

    public void init() {

        Cell iconCell = content.getCells().get(0);
        iconCell.clearActor();

        AudioFail audioFail = AudioFailRepository.find(screen.game.gameState, fail.storyAudio.audio);

        String text = audioFail.getDescription(screen.game.params.locale);

        setTouchable(Touchable.enabled);

        if (!fail.isOpened) {

            String altText = "";

            for (int i = 0; i < text.length(); i++) {
                String ch = text.charAt(i) + "";

                if (!" ".equals(ch)) {
                    if (i % 4 == 0) {
                        ch = "@";
                    } else if (i % 3 == 0) {
                        ch = "$";
                    } else if (i % 2 == 0) {
                        ch = "!";
                    } else {
                        ch = "#";
                    }
                }

                altText += ch;
            }

            text = altText;

            setTouchable(Touchable.disabled);

            iconCell.setActor(lock);

            setLockBackground(
                    screen.assetManager.get("gallery/b_closed_0.png", Texture.class)
            );
        } else if (!fail.isListened) {

            iconCell.setActor(unlock);

            setUnlockBackground(
                    screen.assetManager.get("gallery/b_opened_0.png", Texture.class)
            );
        }

        setPlayBackground(
                screen.assetManager.get("ui/playbar_play.png", Texture.class)
        );

        title.setText(text);
    }

    public void setLockBackground(Texture texture) {

        lock.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / lock.getDrawable().getMinWidth();
        float height = 1f * lock.getDrawable().getMinHeight() * scale;

        content.getCell(lock)
                .width(width)
                .height(height);
    }

    public void setUnlockBackground(Texture texture) {

        unlock.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / unlock.getDrawable().getMinWidth();
        float height = 1f * unlock.getDrawable().getMinHeight() * scale;

        content.getCell(unlock)
                .width(width)
                .height(height);
    }

    public void setPlayBackground(Texture texture) {
        play.setDrawable(new SpriteDrawable(new Sprite(texture)));
    }
}
