package ua.gram.munhauzen.interaction.servants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.servants.fragment.ServantsFireImageFragment;

public abstract class HiredServant extends Image {

    final ServantsFireImageFragment fragment;
    boolean disabled;
    Drawable on, off;

    public HiredServant(Texture on, Texture off, ServantsFireImageFragment fragment) {

        this.on = new SpriteDrawable(new Sprite(on));
        this.off = new SpriteDrawable(new Sprite(off));

        this.fragment = fragment;
    }

    public void init() {

        updateDrawable();

        setOrigin(Align.bottom);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                fragment.fireContainer.clearChildren();

                fragment.fireDialog.create(HiredServant.this);

                fragment.fireContainer.add(fragment.fireDialog.root);

                fragment.toggleFireDialog();

            }
        });

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        updateDrawable();
    }

    private void updateDrawable() {
        if (disabled) {
            setDrawable(off);
        } else {
            setDrawable(on);
        }

        float width = MunhauzenGame.WORLD_WIDTH / 2f;
        float scale = 1f * width / getDrawable().getMinWidth();
        float height = 1f * getDrawable().getMinHeight() * scale;

        setSize(width, height);
    }

    public void activate() {

        setDisabled(true);

        addAction(Actions.scaleTo(1.1f, 1.1f, .2f));
    }

    public void discard() {
        setScale(1);

        clearActions();

        setDisabled(false);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
