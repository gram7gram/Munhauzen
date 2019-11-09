package ua.gram.munhauzen.interaction.swamp.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.SwampInteraction;
import ua.gram.munhauzen.interaction.swamp.ui.MunchausenInSwamp;
import ua.gram.munhauzen.interaction.swamp.ui.Overlay;
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
    StoryAudio audio;

    public SwampImageFragment(SwampInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {
        Log.i(tag, "create");

        Texture img = interaction.gameScreen.game.internalAssetManager.get("p0.jpg", Texture.class);

        swampBackground = new SwampBackground(interaction);
        swamp = new Swamp(interaction);
        munhauzen = new MunchausenInSwamp(interaction);
        Overlay overlay = new Overlay(this, img);

        Group group = new Group();
        group.addActor(swampBackground);
        group.addActor(munhauzen);
        group.addActor(swamp);
        group.addActor(overlay);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(group);

        root.setName(tag);

        munhauzen.enableGravity();
    }

    public void update() {

        if (audio != null) {
            interaction.gameScreen.audioService.updateVolume(audio);
        }
    }

    public void checkIfWinner() {

        if (munhauzen.getY() >= munhauzen.limit) {
            complete();
        }
    }

    private void complete() {
        Log.i(tag, "complete y=" + munhauzen.getY());

        try {

            Timer.instance().clear();

            root.setTouchable(Touchable.disabled);
            munhauzen.setTouchable(Touchable.disabled);

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
                                }
                            }
                        }, audio.duration / 1000f);

                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }, audio.duration / 1000f);
        } catch (Throwable e) {
            Log.e(tag, e);
        }

    }

    private void playBounceBeforeWin() {
        stopAudio();

        audio = new StoryAudio();
        audio.audio = "sfx_inter_swamp_2";

        interaction.gameScreen.audioService.prepareAndPlay(audio);
    }

    private void playWin() {
        stopAudio();

        audio = new StoryAudio();
        audio.audio = "s24_a";

        interaction.gameScreen.audioService.prepareAndPlay(audio);
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
