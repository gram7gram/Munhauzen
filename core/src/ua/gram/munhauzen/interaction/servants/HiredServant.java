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

    public abstract float[] getPercentBounds();

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

        float[] bounds = getPercentBounds();

        float width = bounds[0] / 100 * fragment.backgroundImage.backgroundWidth;
        float height = bounds[1] / 100 * fragment.backgroundImage.backgroundHeight;
        float x = bounds[2] / 100 * fragment.backgroundImage.backgroundWidth;
        float y = (100 - bounds[3]) / 100 * fragment.backgroundImage.backgroundHeight;

        setBounds(
                fragment.backgroundImage.background.getX() + x,
                fragment.backgroundImage.background.getY() + y - height,
                width, height);
    }

    private void updateDrawable() {
        if (disabled) {
            setDrawable(off);
        } else {
            setDrawable(on);
        }
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
