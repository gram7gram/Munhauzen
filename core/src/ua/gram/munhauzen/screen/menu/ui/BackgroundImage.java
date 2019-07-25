package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.FitImage;

public class BackgroundImage extends Group {

    final MenuScreen screen;
    public final Image background;
    public final Table backgroundTable;
    public float backgroundWidth, backgroundHeight, backgroundScale;
    public boolean isWide;

    public BackgroundImage(MenuScreen screen) {
        super();

        this.screen = screen;

        background = new FitImage();

        backgroundTable = new Table();
        backgroundTable.setTouchable(Touchable.childrenOnly);
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        addActor(backgroundTable);
    }

    public void setBackgroundDrawable(SpriteDrawable drawable) {

        background.clear();

        background.setDrawable(drawable);

        isWide = drawable.getMinWidth() > drawable.getMinHeight();

        if (isWide) {

            backgroundHeight = MunhauzenGame.WORLD_HEIGHT;
            backgroundScale = 1f * backgroundHeight / drawable.getMinHeight();
            backgroundWidth = 1f * drawable.getMinWidth() * backgroundScale;

        } else {

            backgroundWidth = MunhauzenGame.WORLD_WIDTH;
            backgroundScale = 1f * backgroundWidth / drawable.getMinWidth();
            backgroundHeight = 1f * drawable.getMinHeight() * backgroundScale;
        }

        backgroundTable.getCell(background)
                .width(backgroundWidth)
                .height(backgroundHeight);
    }
}
