package ua.gram.munhauzen.screen.menu.animation;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.menu.ui.MenuButton;

public abstract class IconAnimation extends AnimatedImage {

    public float iconWidth, iconHeight;

    public IconAnimation(MenuButton button) {
        super();

        iconHeight = button.buttonHeight * .3f;

        loop = false;
    }

    @Override
    public void layout() {
        super.layout();

        try {

            float scale = 1f * iconHeight / getCurrentDrawable().getMinHeight();
            iconWidth = scale * getCurrentDrawable().getMinWidth();

            setSize(iconWidth, iconHeight);
        } catch (Throwable ignore) {}

    }
}
