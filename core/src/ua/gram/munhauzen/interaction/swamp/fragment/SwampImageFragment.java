package ua.gram.munhauzen.interaction.swamp.fragment;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.SwampInteraction;
import ua.gram.munhauzen.interaction.swamp.ui.MunchausenInSwamp;
import ua.gram.munhauzen.interaction.swamp.ui.Swamp;
import ua.gram.munhauzen.interaction.swamp.ui.SwampBackground;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SwampImageFragment extends InteractionFragment {

    private final SwampInteraction interaction;
    public FragmentRoot root;
    public Swamp swamp;
    public MunchausenInSwamp munhauzen;
    public SwampBackground swampBackground;
    Container<MunchausenInSwamp> munchausenContainer;
    StoryAudio audio;

    public SwampImageFragment(SwampInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        swampBackground = new SwampBackground(interaction);
        swamp = new Swamp(interaction);
        munhauzen = new MunchausenInSwamp(interaction);

        munchausenContainer = new Container<>(munhauzen);
        munchausenContainer.setClip(true);

        Group group = new Group();

        group.addActor(swampBackground);
        group.addActor(munchausenContainer);
        group.addActor(swamp);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(group);

        root.setName(tag);

        munhauzen.enableGravity();
    }

    public void update() {

        munchausenContainer.setBounds(
                swampBackground.getX(),
                swampBackground.getY(),
                MunhauzenGame.WORLD_WIDTH,
                MunhauzenGame.WORLD_HEIGHT
        );

        if (audio != null) {
            interaction.gameScreen.audioService.updateVolume(audio);
        }
    }

    public void checkIfWinner() {

        if (root.getTouchable() == Touchable.disabled) {
            return;
        }

        Vector2 screenCoord = munhauzen.localToScreenCoordinates(new Vector2());

        if (screenCoord.y > munhauzen.winLimit) {
            complete();
        }
    }

    private void complete() {
        Log.i(tag, "complete");

        try {

            Timer.instance().clear();

            root.setTouchable(Touchable.disabled);

            munhauzen.complete();

            playBounceBeforeWin();

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    try {

                        playWin();

                        Timer.instance().scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {
                                try {

                                    interaction.gameScreen.interactionService.complete();

                                    interaction.gameScreen.interactionService.findStoryAfterInteraction();

                                    interaction.gameScreen.restoreProgressBarIfDestroyed();

                                } catch (Throwable e) {
                                    Log.e(tag, e);

                                    interaction.gameScreen.onCriticalError(e);
                                }
                            }
                        }, audio.duration / 1000f);

                    } catch (Throwable e) {
                        Log.e(tag, e);

                        interaction.gameScreen.onCriticalError(e);
                    }
                }
            }, audio.duration / 1000f);
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }

    }

    private void playBounceBeforeWin() {
        try {

            stopAudio();

            audio = new StoryAudio();
            audio.audio = "sfx_inter_swamp_2";

            interaction.gameScreen.audioService.prepareAndPlay(audio);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    private void playWin() {
        try {

            stopAudio();

            audio = new StoryAudio();
            audio.audio = "s24_a";

            interaction.gameScreen.audioService.prepareAndPlay(audio);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void pausePull() {
        if (audio != null && audio.player != null) {
            audio.player.pause();
        }
    }

    public void playPull() {
        try {
            if (audio != null && audio.audio.equals("sfx_inter_swamp_1")) {
                if (audio.player != null)
                    audio.player.play();
                return;
            }

            stopAudio();

            audio = new StoryAudio();
            audio.audio = "sfx_inter_swamp_1";

            interaction.gameScreen.audioService.prepareAndPlay(audio);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    private void stopAudio() {
        if (audio != null) {
            interaction.gameScreen.audioService.stop(audio);
            audio = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        munhauzen.cancelTask();

        stopAudio();

    }
}
