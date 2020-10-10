package ua.gram.munhauzen.screen.logo.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.LogoScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class ContentFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final LogoScreen screen;
    public FragmentRoot root;

    public ContentFragment(LogoScreen screen) {
        this.screen = screen;
    }

    public void create() {
        Log.i(tag, "create");

        Image logo = new Image(new Texture("logo_500.png"));

        Label title = new Label(screen.game.t("logo.title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

        Table table = new Table();
        table.setFillParent(true);
        table.pad(10);
        table.add(logo).size(MunhauzenGame.WORLD_WIDTH * .5f).pad(10).row();
        table.add(title).width(MunhauzenGame.WORLD_WIDTH * .9f).row();

        Label version = new Label("v" + screen.game.params.versionName + " " + screen.game.params.locale, new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));
        version.setWrap(false);
        version.setAlignment(Align.center);

        Container<Label> versionContainer = new Container<>(version);
        versionContainer.align(Align.bottom);
        versionContainer.pad(10);

        root = new FragmentRoot();
        root.addContainer(table);
        root.addContainer(versionContainer);

        root.setVisible(false);
    }

    public void fadeIn() {
        root.setVisible(false);
        root.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.delay(.3f),
                        Actions.visible(true),
                        Actions.alpha(1, .4f),
                        Actions.delay(2.5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                screen.onComplete();
                            }
                        })
                )
        );

        screen.game.sfxService.onLogoScreenOpened();
    }


    @Override
    public Actor getRoot() {
        return root;
    }
}
