package ua.gram.munhauzen.interaction.puzzle;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashSet;

import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.ui.FitImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PuzzleDecisionManager {

    public final PuzzleInteraction interaction;
    public final HashSet<String> items;
    public ActivePuzzleItem activeStick, activeSpoon, activeShoes, activePeas, activeKey, activeHair, activeClocks, activeArrows, activePowder, activeRope;

    public PuzzleDecisionManager(PuzzleInteraction interaction) {
        this.interaction = interaction;
        items = new HashSet<>();
    }

    public void decide() {

        if (activePowder != null) {
            activePowder.remove();
            activePowder = null;
        }

        if (activeStick != null) {
            activeStick.remove();
            activeStick = null;
        }

        if (activeSpoon != null) {
            activeSpoon.remove();
            activeSpoon = null;
        }

        if (activeShoes != null) {
            activeShoes.remove();
            activeShoes = null;
        }

        if (activePeas != null) {
            activePeas.remove();
            activePeas = null;
        }

        if (activeKey != null) {
            activeKey.remove();
            activeKey = null;
        }

        if (activeHair != null) {
            activeHair.remove();
            activeHair = null;
        }

        if (activeClocks != null) {
            activeClocks.remove();
            activeClocks = null;
        }

        if (activeArrows != null) {
            activeArrows.remove();
            activeArrows = null;
        }

        if (activeRope != null) {
            activeRope.remove();
            activeRope = null;
        }

        for (String item : items) {
            String res = "puzzle/inter_puzzle_" + item + "_2.png";

            switch (item) {
                case "clocks":

                    if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                        interaction.assetManager.load(res, Texture.class);

                        interaction.assetManager.finishLoading();
                    }

                    activeClocks = new ActiveClocks(
                            interaction.assetManager.get(res, Texture.class)
                    );

                    activeClocks.init();

                    interaction.imageFragment.root.addActor(activeClocks);
                    break;
                case "spoon":

                    if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                        interaction.assetManager.load(res, Texture.class);

                        interaction.assetManager.finishLoading();
                    }

                    activeSpoon = new ActiveSpoon(
                            interaction.assetManager.get(res, Texture.class)
                    );

                    activeSpoon.init();

                    interaction.imageFragment.root.addActor(activeSpoon);
                    break;
                case "shoes":

                    if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                        interaction.assetManager.load(res, Texture.class);

                        interaction.assetManager.finishLoading();
                    }

                    activeShoes = new ActiveShoes(
                            interaction.assetManager.get(res, Texture.class)
                    );

                    activeShoes.init();

                    interaction.imageFragment.root.addActor(activeShoes);
                    break;
                case "peas":

                    if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                        interaction.assetManager.load(res, Texture.class);

                        interaction.assetManager.finishLoading();
                    }

                    activePeas = new ActivePeas(
                            interaction.assetManager.get(res, Texture.class)
                    );

                    activePeas.init();

                    interaction.imageFragment.root.addActor(activePeas);
                    break;
                case "key":

                    if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                        interaction.assetManager.load(res, Texture.class);

                        interaction.assetManager.finishLoading();
                    }

                    activeKey = new ActiveKey(
                            interaction.assetManager.get(res, Texture.class)
                    );

                    activeKey.init();

                    interaction.imageFragment.root.addActor(activeKey);
                    break;
                case "hair":

                    if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                        interaction.assetManager.load(res, Texture.class);

                        interaction.assetManager.finishLoading();
                    }

                    activeHair = new ActiveHair(
                            interaction.assetManager.get(res, Texture.class)
                    );

                    activeHair.init();

                    interaction.imageFragment.root.addActor(activeHair);
                    break;
                case "arrows":

                    if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                        interaction.assetManager.load(res, Texture.class);

                        interaction.assetManager.finishLoading();
                    }

                    activeArrows = new ActiveArrows(
                            interaction.assetManager.get(res, Texture.class)
                    );

                    activeArrows.init();

                    interaction.imageFragment.root.addActor(activeArrows);
                    break;
                case "rope":

                    if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                        interaction.assetManager.load(res, Texture.class);

                        interaction.assetManager.finishLoading();
                    }

                    activeRope = new ActiveRope(
                            interaction.assetManager.get(res, Texture.class)
                    );

                    activeRope.init();

                    interaction.imageFragment.root.addActor(activeRope);
                    break;
                case "stick":

                    if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                        interaction.assetManager.load(res, Texture.class);

                        interaction.assetManager.finishLoading();
                    }

                    activeStick = new ActiveStick(
                            interaction.assetManager.get(res, Texture.class)
                    );

                    activeStick.init();

                    interaction.imageFragment.root.addActor(activeStick);
                    break;
                case "powder":

                    if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                        interaction.assetManager.load(res, Texture.class);

                        interaction.assetManager.finishLoading();
                    }

                    activePowder = new ActivePowder(
                            interaction.assetManager.get(res, Texture.class)
                    );

                    activePowder.init();

                    interaction.imageFragment.root.addActor(activePowder);
                    break;
            }
        }
    }

    public void reset() {

    }

    public abstract class ActivePuzzleItem extends FitImage {

        public ActivePuzzleItem(Texture texture) {
            super(texture);
        }

        public void init() {

            float scale = .85f;
            float width = 150;
            float height = width * (1 / scale);

            setSize(
                    width * interaction.imageFragment.backgroundScale,
                    height * interaction.imageFragment.backgroundScale
            );

            interaction.imageFragment.setPositionRelativeToBackground(this, 330, 530);
        }

    }

    public class ActivePowder extends ActivePuzzleItem {
        public ActivePowder(Texture texture) {
            super(texture);
        }
    }

    public class ActivePeas extends ActivePuzzleItem {
        public ActivePeas(Texture texture) {
            super(texture);
        }
    }

    public class ActiveRope extends ActivePuzzleItem {
        public ActiveRope(Texture texture) {
            super(texture);
        }
    }

    public class ActiveShoes extends ActivePuzzleItem {
        public ActiveShoes(Texture texture) {
            super(texture);
        }
    }

    public class ActiveStick extends ActivePuzzleItem {
        public ActiveStick(Texture texture) {
            super(texture);
        }
    }

    public class ActiveHair extends ActivePuzzleItem {
        public ActiveHair(Texture texture) {
            super(texture);
        }
    }

    public class ActiveKey extends ActivePuzzleItem {
        public ActiveKey(Texture texture) {
            super(texture);
        }
    }

    public class ActiveArrows extends ActivePuzzleItem {
        public ActiveArrows(Texture texture) {
            super(texture);
        }
    }

    public class ActiveSpoon extends ActivePuzzleItem {
        public ActiveSpoon(Texture texture) {
            super(texture);
        }
    }

    public class ActiveClocks extends ActivePuzzleItem {
        public ActiveClocks(Texture texture) {
            super(texture);
        }
    }
}
