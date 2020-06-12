package ua.gram.munhauzen.screen.debug.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.screen.DebugAudioScreen;
import ua.gram.munhauzen.screen.debug.ui.AudioTestRow;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.VerticalScrollPane;
import ua.gram.munhauzen.utils.Log;

public class AudioFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    private final DebugAudioScreen screen;
    public FragmentRoot root;

    ArrayList<AudioTestRow> rows;
    int currentTest;
    Timer.Task task;

    public AudioFragment(DebugAudioScreen screen) {
        this.screen = screen;
        this.game = screen.game;
    }

    public void create() {
        rows = new ArrayList<>();
        currentTest = 0;

        for (Audio audio : game.gameState.audioRegistry) {
            rows.add(new AudioTestRow(screen, audio.name, audio.file));
        }

        for (AudioFail audio : game.gameState.audioFailRegistry) {
            rows.add(new AudioTestRow(screen, audio.name, audio.file));
        }

        Table table = new Table();
        table.pad(10);
        table.setFillParent(true);

        Label title = new Label("AUDIO TEST", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.h3),
                Color.BLACK
        ));
        title.setAlignment(Align.center);

        table.add(title).center().row();

        for (AudioTestRow row : rows) {
            table.add(row).left().row();
        }

        Stack stack = new Stack();
        stack.add(table);

        VerticalScrollPane scrollPane = new VerticalScrollPane(stack);

        root = new FragmentRoot();
        root.addContainer(scrollPane);
    }

    public void startTesting() {

        currentTest = 0;

        task = Timer.instance().scheduleTask(new Timer.Task() {

            private void complete() {
                cancel();

                String errors = "";

                for (AudioTestRow row : rows) {
                    if (row.status == AudioTestRow.Status.FAIL) {
                        errors += "\n" + row.name;
                    }
                }

                Log.e(tag, "Errors:" + errors);

            }

            @Override
            public void run() {

                if (currentTest >= rows.size()) {
                    complete();
                    return;
                }

                final AudioTestRow row = rows.get(currentTest);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        row.test();

                    }
                }).start();


                ++currentTest;

            }
        }, .15f, .3f);

    }

    public void update() {
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

        rows.clear();

        task.cancel();
    }
}
