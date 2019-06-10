package ua.gram.munhauzen.transition;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.fragment.ImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class NormalTransition extends Transition {

    public NormalTransition(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void prepare(final StoryImage item) {

        final ImageFragment fragment = gameScreen.imageFragment;

        fragment.layer1Image.clear();
        fragment.layer2Image.clear();

        fragment.layer1ImageGroup.setVisible(true);
        fragment.layer2ImageGroup.setVisible(false);

        fragment.layer1Image.addAction(Actions.alpha(1));
        //fragment.layer2Image.addAction(Actions.alpha(1));

        final Image targetImage = fragment.layer1Image;

        targetImage.setDrawable(item.drawable);
        targetImage.setName(item.image);
        targetImage.setTouchable(Touchable.enabled);

        if (item.drawable.getMinWidth() > item.drawable.getMinHeight()) {

            float scale = 1f * MunhauzenGame.WORLD_HEIGHT / item.drawable.getMinHeight();
            float width = 1f * item.drawable.getMinWidth() * scale;

            item.height = MunhauzenGame.WORLD_HEIGHT;
            item.width = width;

            targetImage.addAction(
                    Actions.forever(
                            Actions.sequence(
                                    Actions.moveBy(10, 0, .5f),
                                    Actions.moveBy(0, 10, .5f),
                                    Actions.moveBy(-10, 0, .5f),
                                    Actions.moveBy(0, -10, .5f)
                            )
                    )
            );

            targetImage.addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    Log.i(tag, "clicked");
                }
            });

            targetImage.addCaptureListener(new ActorGestureListener() {

                @Override
                public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                    super.pan(event, x, y, deltaX, deltaY);

                    Log.i(tag, "pan");

                    try {
                        float newX = targetImage.getX() + deltaX;
                        float currentWidth = item.width;
                        int viewportWidth = gameScreen.game.view.getScreenWidth();

                        float leftBound = -currentWidth + viewportWidth;
                        float rightBound = 0;

                        if (leftBound < newX && newX < rightBound) {
                            targetImage.addAction(Actions.moveBy(deltaX, 0));
                        }
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }

                }
            });

        } else {

            float scale = 1f * MunhauzenGame.WORLD_WIDTH / item.drawable.getMinWidth();
            float height = 1f * item.drawable.getMinHeight() * scale;

            item.width = MunhauzenGame.WORLD_WIDTH;
            item.height = height;
        }

        fragment.layer1ImageTable.getCell(targetImage)
                .width(item.width)
                .height(item.height);

    }
}
