package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.AbstractScreen;

public abstract class Banner<S extends AbstractScreen> extends Group {

    public final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;
    public final S screen;
    public Image back;
    public Table content;

    public Banner(S screen) {

        this.screen = screen;
        this.game = screen.game;

        content = createContent();

        back = new Image(getBackgroundTexture());

        addActor(back);
        addActor(content);

        setTouchable(Touchable.childrenOnly);
    }

    public abstract Texture getBackgroundTexture();

    public abstract Table createContent();

    @Override
    public void act(float delta) {
        super.act(delta);

        content.setPosition(
                (MunhauzenGame.WORLD_WIDTH - content.getWidth()) / 2f,
                (MunhauzenGame.WORLD_HEIGHT - content.getHeight()) / 2f
        );

        back.setPosition(
                (MunhauzenGame.WORLD_WIDTH - back.getWidth()) / 2f,
                (MunhauzenGame.WORLD_HEIGHT - back.getHeight()) / 2f
        );

        back.setSize(
                content.getPrefWidth(),
                content.getPrefHeight()
        );
    }
}
