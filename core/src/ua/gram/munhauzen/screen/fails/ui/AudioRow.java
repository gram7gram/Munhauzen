package ua.gram.munhauzen.screen.fails.ui;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.repository.AudioFailRepository;
import ua.gram.munhauzen.screen.FailsScreen;
import ua.gram.munhauzen.screen.fails.entity.GalleryFail;
import ua.gram.munhauzen.utils.Log;

public class AudioRow extends Table {

    final String tag = getClass().getSimpleName();
    final FailsScreen screen;
    final GalleryFail fail;
    final int index;
    Label title, number;
    Image lock, unlock, play;
    float iconSize = 35;
    Container<Image> iconContainer;
    final Label.LabelStyle openedStyle, hiddenStyle;

    public AudioRow(final FailsScreen screen, final GalleryFail fail, int index, float width) {

        this.index = index;
        this.screen = screen;
        this.fail = fail;

        iconSize *= screen.game.params.scaleFactor;

        openedStyle = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        hiddenStyle = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.SEGUISYM, FontProvider.h5),
                Color.BLACK
        );

        number = new Label(index + ".", openedStyle);
        number.setWrap(false);
        number.setAlignment(Align.left);

        number.layout();

        title = new Label("", openedStyle);
        title.setWrap(true);
        title.setAlignment(Align.left);

        play = new Image();
        unlock = new Image();

        lock = new Image();
        lock.setRotation(-15);

        float lblWidth = width - iconSize - number.getWidth() - 50;

        iconContainer = new Container<>();
        iconContainer.align(Align.topLeft);

        add(iconContainer)
                .minWidth(iconSize).width(iconSize).maxWidth(iconSize)
                .padRight(5)
                .align(Align.topLeft);
        add(number)
                .align(Align.topLeft).padRight(5);
        add(title)
                .width(lblWidth)
                .align(Align.topLeft).row();

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.stopAll();

                    screen.audioService.prepareAndPlay(fail.storyAudio);

                    fail.isPlaying = true;
                    fail.isListened = true;

                    if (fail.storyAudio.player != null)
                        fail.storyAudio.player.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {
                                fail.isPlaying = false;
                            }
                        });

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

        if (fail.isPlaying) {

            if (!play.isVisible()) {
                play.setVisible(true);

                iconContainer.setActor(play);

                setPlayBackground(
                        screen.assetManager.get("ui/playbar_play.png", Texture.class)
                );
            }
        } else {
            play.setVisible(false);
            play.remove();
        }
    }

    public void init() {

        AudioFail audioFail = AudioFailRepository.find(screen.game.gameState, fail.storyAudio.audio);

        String text = audioFail.getDescription(screen.game.params.locale);

        setTouchable(Touchable.enabled);

        title.setStyle(openedStyle);

        if (!fail.isOpened) {

            title.setStyle(hiddenStyle);

            String altText = " ";//space

            for (int i = 0; i < text.length(); i++) {
                String ch = text.charAt(i) + "";

                if (!" ".equals(ch)) {
                    if (i % 4 == 0) {
                        ch = FontProvider.star1;
                    } else if (i % 3 == 0) {
                        ch = FontProvider.star2;
                    } else if (i % 2 == 0) {
                        ch = FontProvider.star3;
                    } else {
                        ch = FontProvider.star4;
                    }
                }

                altText += ch;
            }

            text = altText;

            setTouchable(Touchable.disabled);

            iconContainer.setActor(lock);

            setLockBackground(
                    screen.assetManager.get("gallery/b_closed_0.png", Texture.class)
            );
        } else if (!fail.isListened) {

            iconContainer.setActor(unlock);

            setUnlockBackground(
                    screen.assetManager.get("gallery/b_opened_0.png", Texture.class)
            );
        }

        title.setText(text);
    }

    public void setLockBackground(Texture texture) {

        lock.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / lock.getDrawable().getMinWidth();
        float height = 1f * lock.getDrawable().getMinHeight() * scale;

        getCell(iconContainer)
                .padTop(0)
                .width(width)
                .height(height);
    }

    public void setUnlockBackground(Texture texture) {

        unlock.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / unlock.getDrawable().getMinWidth();
        float height = 1f * unlock.getDrawable().getMinHeight() * scale;

        getCell(iconContainer)
                .padTop(0)
                .width(width)
                .height(height);
    }

    public void setPlayBackground(Texture texture) {
        play.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / play.getDrawable().getMinWidth();
        float height = 1f * play.getDrawable().getMinHeight() * scale;

        getCell(iconContainer)
                .padTop(15)
                .width(width)
                .height(height);
    }
}
