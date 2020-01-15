package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.GameScreen;

public abstract class Banner extends Group {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    final GameScreen screen;
    Image back;
    Table content;

    public Banner(GameScreen screen) {

        this.screen = screen;
        this.game = screen.game;

        content = createContent();

        back = new Image(getBackgroundTexture());

        addActor(back);
        addActor(content);

        setTouchable(Touchable.childrenOnly);

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                //ignore
            }
        });
    }

    abstract Texture getBackgroundTexture();

    abstract Table createContent();

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
