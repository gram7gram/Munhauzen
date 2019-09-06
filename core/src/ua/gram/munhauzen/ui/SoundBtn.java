package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.AbstractScreen;

public class SoundBtn extends ImageButton {

    public SoundBtn(final AbstractScreen screen) {
        super(new ImageButtonStyle());

        Texture txt = screen.assetManager.get("ui/b_sound_on.png", Texture.class);
        Texture txtOff = screen.assetManager.get("ui/b_sound_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(txt));
        style.down = new SpriteDrawable(new Sprite(txt));
        style.disabled = new SpriteDrawable(new Sprite(txtOff));

        setStyle(style);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                GameState.isMute = !GameState.isMute;

                if (GameState.isMute) {
                    screen.game.sfxService.onSoundDisabled();
                } else {
                    screen.game.sfxService.onSoundEnabled();
                }
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setDisabled(GameState.isMute);
    }
}
