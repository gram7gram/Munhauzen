package ua.gram.munhauzen.screen.debug.ui;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.screen.DebugAudioScreen;
import ua.gram.munhauzen.utils.Log;

public class AudioTestRow extends Label {

    public enum Status {
        PENDING, ACTIVE, SUCCESS, FAIL
    }

    final String tag = getClass().getSimpleName();
    final DebugAudioScreen screen;
    public final String file, name;
    public Status status = Status.PENDING;

    public AudioTestRow(DebugAudioScreen screen, String name, String file) {

        super("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.h5),
                Color.BLACK
        ));

        setAlignment(Align.left);

        this.screen = screen;
        this.name = name;
        this.file = file;
    }

    public void test() {

        Log.i(tag, "test " + file);

        status = Status.ACTIVE;

        try {

            screen.assetManager.load(file, Music.class);

            screen.assetManager.finishLoading();

            final Music music = screen.assetManager.get(file, Music.class);

            music.play();

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {

                    try {

                        music.stop();

                        screen.assetManager.unload(file);
                    } finally {

                    }

                }
            }, 1);

            status = Status.SUCCESS;

        } catch (Throwable e) {

            status = Status.FAIL;

        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        try {
            if (status == Status.PENDING) {
                setText(" " + name);
            } else if (status == Status.ACTIVE) {
                setText("? " + name);
            } else if (status == Status.SUCCESS) {
                setText("+ " + name);
//                setColor(Color.GREEN);
            } else if (status == Status.FAIL) {
                setText("- " + name);
//                setColor(Color.RED);
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}
