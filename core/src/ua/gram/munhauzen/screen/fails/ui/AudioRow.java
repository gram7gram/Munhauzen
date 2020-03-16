package ua.gram.munhauzen.screen.fails.ui;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
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
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.repository.AudioFailRepository;
import ua.gram.munhauzen.screen.FailsScreen;
import ua.gram.munhauzen.screen.fails.entity.GalleryFail;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

public class AudioRow extends Table {

    final String tag = getClass().getSimpleName();
    final FailsScreen screen;
    final GalleryFail fail;
    final int index;
    Label title, number;
    Image icon;
    float iconSize, width;
    Container<Image> iconContainer;
    final Label.LabelStyle openedStyle, hiddenStyle, errorStyle;

    public AudioRow(final FailsScreen screen, final GalleryFail fail, final int index, float width) {

        this.index = index;
        this.width = width;
        this.screen = screen;
        this.fail = fail;

        iconSize = MunhauzenGame.WORLD_WIDTH * .05f;

        errorStyle = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.RED
        );

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

        icon = new Image();

        iconContainer = new Container<>(icon);
        iconContainer.align(Align.topLeft);

        add(iconContainer).align(Align.topLeft).padRight(5);
        add(number).align(Align.topLeft).padRight(5);
        add(title).align(Align.topLeft).row();

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    Log.i(tag, "clicked on " + fail.storyAudio.audio);

                    screen.stopAll();

                    screen.audioService.prepareAndPlay(fail.storyAudio);

                    if (fail.storyAudio.player != null) {
                        fail.isPlaying = true;

                        fail.storyAudio.player.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {
                                screen.stopAll();
                            }
                        });
                    }

                    fail.isListened = true;

                    screen.game.gameState.failsState.listenedAudio.add(fail.storyAudio.audio);

                    init();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        init();
    }

    public void init() {

        AudioFail audioFail = AudioFailRepository.find(screen.game.gameState, fail.storyAudio.audio);

        String text = audioFail.description;

        setTouchable(Touchable.enabled);

        number.setStyle(openedStyle);
        title.setStyle(openedStyle);

        icon.setVisible(true);

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

            setLockBackground(
                    screen.assetManager.get("gallery/b_closed_0.png", Texture.class)
            );
        } else if (fail.isPlaying) {

            setPlayBackground(
                    screen.assetManager.get("ui/playbar_play.png", Texture.class)
            );
        } else if (!fail.isListened) {

            setUnlockBackground(
                    screen.assetManager.get("gallery/b_opened_0.png", Texture.class)
            );
        } else {
            icon.setVisible(false);
            getCell(iconContainer).size(iconSize);
        }

        title.setText(text);

        float lblWidth = width - iconSize - number.getWidth() - 50;

        getCell(title).width(lblWidth);

        FileHandle file = ExternalFiles.getExpansionAudio(screen.game.params, audioFail);
        if (!file.exists()) {
            title.setStyle(errorStyle);
            number.setStyle(errorStyle);
        }
    }

    public void setLockBackground(Texture texture) {

        icon.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / texture.getWidth();
        float height = 1f * texture.getHeight() * scale;

        getCell(iconContainer)
                .padTop(0)
                .width(width)
                .height(height);
    }

    public void setUnlockBackground(Texture texture) {

        icon.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / texture.getWidth();
        float height = 1f * texture.getHeight() * scale;

        getCell(iconContainer)
                .padTop(0)
                .width(width)
                .height(height);
    }

    public void setPlayBackground(Texture texture) {
        icon.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / texture.getWidth();
        float height = 1f * texture.getHeight() * scale;

        getCell(iconContainer)
                .padTop(15)
                .width(width)
                .height(height);
    }
}
