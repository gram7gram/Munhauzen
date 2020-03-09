package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.HashMap;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.screen.GameScreen;

public abstract class ScenarioFragment extends Fragment {

    public final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;

    public final GameScreen screen;
    public FitImage imgLeft, imgRight, imgTop;
    public Table decorLeft, decorRight, decorTop;
    public Table blocks;
    public Stack root;
    public Container<Actor> bgContainer;
    public final ArrayList<Actor> buttonList;
    public final HashMap<Integer, String> animatedMap = new HashMap<>(7);
    public boolean isFadeIn;
    public boolean isFadeOut;

    public ScenarioFragment(GameScreen screen) {
        this.game = screen.game;
        this.screen = screen;
        buttonList = new ArrayList<>();

        animatedMap.put(0, "GameScreen/an_letter_A_sheet.png");
        animatedMap.put(1, "GameScreen/an_letter_B_sheet.png");
        animatedMap.put(2, "GameScreen/an_letter_C_sheet.png");
        animatedMap.put(3, "GameScreen/an_letter_D_sheet.png");
        animatedMap.put(4, "GameScreen/an_letter_E_sheet.png");
        animatedMap.put(5, "GameScreen/an_letter_F_sheet.png");
        animatedMap.put(6, "GameScreen/an_letter_G_sheet.png");
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

        buttonList.clear();
    }

    public abstract void create(ArrayList<Decision> decisions);

    public abstract void makeDecision(final int currentIndex, Decision decision);

    public void fadeOut(Runnable task) {

        isFadeIn = false;
        isFadeOut = true;

        float duration = .3f;

        blocks.addAction(Actions.sequence(
                Actions.alpha(0, duration),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                }),
                Actions.run(task)
        ));

        fadeOutDecoration();
    }

    public void fadeOut() {

        fadeOutWithoutDecoration();

        fadeOutDecoration();
    }

    public void fadeOutWithoutDecoration() {

        isFadeIn = false;
        isFadeOut = true;

        float duration = .3f;

        bgContainer.setVisible(false);

        blocks.addAction(Actions.sequence(
                Actions.alpha(0, duration),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
    }

    public void fadeOutDecoration() {
        float duration = .5f;

        decorLeft.addAction(Actions.parallel(
                Actions.moveTo(-decorLeft.getWidth(), 0, duration),
                Actions.alpha(0, duration)
        ));

        decorTop.addAction(Actions.parallel(
                Actions.moveTo(0, decorTop.getHeight(), duration),
                Actions.alpha(0, duration)
        ));

        decorRight.addAction(
                Actions.parallel(
                        Actions.moveTo(decorRight.getWidth(), 0, duration),
                        Actions.alpha(0, duration)
                ));
    }

    public void fadeIn() {

        if (!isMounted()) return;

        root.setVisible(true);

        fadeInWithoutDecoration();

        fadeInDecoration();
    }

    public void fadeInWithoutDecoration() {

        if (!isMounted()) return;

        isFadeIn = true;
        isFadeOut = false;

        bgContainer.setVisible(true);

        blocks.setVisible(true);
        blocks.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
    }

    private void fadeInDecoration() {
        float duration = .3f;

        decorLeft.addAction(Actions.moveBy(-imgLeft.getWidth(), 0));
        decorLeft.addAction(Actions.alpha(0));
        decorLeft.addAction(
                Actions.parallel(
                        Actions.moveTo(0, 0, duration),
                        Actions.alpha(1, duration)
                )

        );

        decorTop.addAction(Actions.moveBy(0, imgTop.getHeight()));
        decorTop.addAction(Actions.alpha(0));
        decorTop.addAction(
                Actions.parallel(
                        Actions.moveTo(0, 0, duration),
                        Actions.alpha(1, duration)
                )

        );

        decorRight.addAction(Actions.moveBy(imgRight.getWidth(), 0));
        decorRight.addAction(Actions.alpha(0));
        decorRight.addAction(
                Actions.parallel(
                        Actions.moveTo(0, 0, duration),
                        Actions.alpha(1, duration)
                )

        );
    }
}
