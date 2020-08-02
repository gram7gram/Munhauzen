package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.AchievementState;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.Progress;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AchievementFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MenuScreen screen;
    public FragmentRoot root;
    Label progressLbl;

    public AchievementFragment(MenuScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        Progress progress = new Progress(screen);

        progressLbl = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        ));
        progressLbl.setAlignment(Align.center);
        progressLbl.setWrap(false);

        Table table = new Table();
        table.add(progressLbl).expandX().center().padBottom(5).row();
        table.add(progress).size(progress.width, progress.height).center().row();

        Container<?> container = new Container<>(table);
        container.pad(10, 10, 30, 10);
        container.align(Align.bottom);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(container);

        root.setName(tag);

        root.setVisible(false);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                fadeInFancy();
            }
        }, .4f);

        update();
    }

    public void update() {
        AchievementState state = screen.game.gameState.achievementState;

        int progress;
        if (state == null) {
            progress = 0;
        } else {
            progress = (int) (100f * state.points / screen.game.params.achievementPoints);
        }

        progress = Math.min(100, progress);

        progressLbl.setText(screen.game.t("menu_achievement.progress") + ": " + progress + "%");
    }

    public void fadeInFancy() {

        root.addAction(Actions.sequence(
                Actions.visible(false),
                Actions.moveBy(0, -20),
                Actions.alpha(0),
                Actions.parallel(
                        Actions.visible(true),
                        Actions.moveBy(0, 20, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

    }

    public void fadeOutFancy() {

        root.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.parallel(
                        Actions.moveBy(0, -20, .2f),
                        Actions.alpha(0, .2f)
                ),
                Actions.visible(false),
                Actions.moveBy(0, 20)
        ));

    }

    @Override
    public Actor getRoot() {
        return root;
    }

}
