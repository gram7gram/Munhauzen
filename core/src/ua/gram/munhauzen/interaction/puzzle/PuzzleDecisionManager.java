package ua.gram.munhauzen.interaction.puzzle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.service.InventoryService;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PuzzleDecisionManager {

    public final String tag = getClass().getSimpleName();
    public final HashMap<HashSet<String>, String> combinations;
    public final PuzzleInteraction interaction;
    public final HashSet<String> items;
    public ActivePuzzleItem activeStick, activeSpoon, activeShoes, activePeas, activeKey, activeHair, activeClocks, activeArrows, activePowder, activeRope;

    public PuzzleDecisionManager(PuzzleInteraction interaction) {
        this.interaction = interaction;
        items = new HashSet<>();

        combinations = new HashMap<>();
        HashSet<String> combo1 = new HashSet<>();
        combo1.add("powder");
        combo1.add("peas");

        HashSet<String> combo2 = new HashSet<>();
        combo2.add("peas");
        combo2.add("rope");
        combo2.add("spoon");

        HashSet<String> combo3 = new HashSet<>();
        combo3.add("shoes");
        combo3.add("stick");
        combo3.add("hair");

        HashSet<String> combo4 = new HashSet<>();
        combo4.add("key");
        combo4.add("arrows");
        combo4.add("clocks");

        combinations.put(combo1, "BOMB");
        combinations.put(combo2, "FISHING_ROD");
        combinations.put(combo3, "CROW");
        combinations.put(combo4, "CLOCKS");
    }

    public void cleanup() {

        try {

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

            interaction.imageFragment.clocks.setVisible(true);
            interaction.imageFragment.spoon.setVisible(true);
            interaction.imageFragment.shoes.setVisible(true);
            interaction.imageFragment.peas.setVisible(true);
            interaction.imageFragment.key.setVisible(true);
            interaction.imageFragment.hair.setVisible(true);
            interaction.imageFragment.arrows.setVisible(true);
            interaction.imageFragment.rope.setVisible(true);
            interaction.imageFragment.stick.setVisible(true);

            interaction.gameScreen.audioService.stop();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void decide() {

        try {
            cleanup();

            for (String item : items) {
                String res = "puzzle/inter_puzzle_" + item + "_2.png";

                if (!interaction.assetManager.isLoaded(res, Texture.class)) {
                    interaction.assetManager.load(res, Texture.class);

                    interaction.assetManager.finishLoading();
                }

                Texture texture = interaction.assetManager.get(res, Texture.class);

                switch (item) {
                    case "clocks":

                        activeClocks = new ActiveClocks(texture);

                        activeClocks.init();

                        interaction.imageFragment.root.addActor(activeClocks);
                        interaction.imageFragment.clocks.setVisible(false);
                        break;
                    case "spoon":
                        activeSpoon = new ActiveSpoon(texture);

                        activeSpoon.init();

                        interaction.imageFragment.root.addActor(activeSpoon);
                        interaction.imageFragment.spoon.setVisible(false);
                        break;
                    case "shoes":
                        activeShoes = new ActiveShoes(texture);

                        activeShoes.init();

                        interaction.imageFragment.root.addActor(activeShoes);
                        interaction.imageFragment.shoes.setVisible(false);
                        break;
                    case "peas":

                        activePeas = new ActivePeas(texture);

                        activePeas.init();

                        interaction.imageFragment.root.addActor(activePeas);
                        interaction.imageFragment.peas.setVisible(false);
                        break;
                    case "key":

                        activeKey = new ActiveKey(texture);

                        activeKey.init();

                        interaction.imageFragment.root.addActor(activeKey);
                        interaction.imageFragment.key.setVisible(false);
                        break;
                    case "hair":
                        activeHair = new ActiveHair(texture);

                        activeHair.init();

                        interaction.imageFragment.root.addActor(activeHair);
                        interaction.imageFragment.hair.setVisible(false);
                        break;
                    case "arrows":
                        activeArrows = new ActiveArrows(texture);

                        activeArrows.init();

                        interaction.imageFragment.root.addActor(activeArrows);
                        interaction.imageFragment.arrows.setVisible(false);
                        break;
                    case "rope":

                        activeRope = new ActiveRope(texture);

                        activeRope.init();

                        interaction.imageFragment.root.addActor(activeRope);
                        interaction.imageFragment.rope.setVisible(false);
                        break;
                    case "stick":
                        activeStick = new ActiveStick(texture);

                        activeStick.init();

                        interaction.imageFragment.root.addActor(activeStick);
                        interaction.imageFragment.stick.setVisible(false);
                        break;
                    case "powder":

                        activePowder = new ActivePowder(texture);

                        activePowder.init();

                        interaction.imageFragment.root.addActor(activePowder);
                        interaction.imageFragment.powder.setVisible(false);
                        break;
                }
            }

            if (items.size() == 3) {
                checkCombination();
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void destroyItems() {

        try {

            InventoryService inventoryService = interaction.gameScreen.game.inventoryService;

            Inventory inventoryCrow = InventoryRepository.find(interaction.gameScreen.game.gameState, "CROW");

            String soundName;
            if (!inventoryService.isInInventory(inventoryCrow)) {

                soundName = MathUtils.random(new String[]{
                        "s15broken_1",
                        "s15broken_2",
                        "s15broken_3",
                        "s15broken_4",
                        "s15broken_5"
                });
            } else {

                soundName = MathUtils.random(new String[]{
                        "s15crow_1", "s15crow_2", "s15crow_3", "s15crow_4", "s15crow_5"
                });
            }

            final StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = soundName;

            interaction.gameScreen.audioService.prepare(storyAudio, new Timer.Task() {
                @Override
                public void run() {
                    try {
                        interaction.gameScreen.audioService.playAudio(storyAudio);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            });

        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {

                    try {
                        for (String item : items) {

                            switch (item) {
                                case "clocks":
                                    activeClocks.destroy();
                                    break;
                                case "spoon":
                                    activeSpoon.destroy();
                                    break;
                                case "shoes":
                                    activeShoes.destroy();
                                    break;
                                case "peas":
                                    activePeas.destroy();
                                    break;
                                case "key":
                                    activeKey.destroy();
                                    break;
                                case "hair":
                                    activeHair.destroy();
                                    break;
                                case "arrows":
                                    activeArrows.destroy();
                                    break;
                                case "rope":
                                    activeRope.destroy();
                                    break;
                                case "stick":
                                    activeStick.destroy();
                                    break;
                                case "powder":
                                    activePowder.destroy();
                                    break;
                            }
                        }
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }, 1);
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    reset();
                }
            }, 2);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void checkCombination() {

        Log.i(tag, "checkCombination");

        InventoryService inventoryService = interaction.gameScreen.game.inventoryService;

        Log.i(tag, "items: " + Arrays.toString(items.toArray()));

        boolean hasCombination = false;
        for (HashSet<String> strings : combinations.keySet()) {
            if (items.containsAll(strings)) {

                try {
                    hasCombination = true;

                    String item = combinations.get(strings);

                    Log.i(tag, "Has combination! " + item);

                    Inventory inventory = InventoryRepository.find(interaction.gameScreen.game.gameState, item);

                    if (!inventoryService.isInInventory(inventory)) {
                        if (inventory.isGlobal()) {
                            inventoryService.addGlobalInventory(inventory);
                        } else {
                            inventoryService.addInventory(inventory);
                        }
                    }

                } catch (Throwable e) {
                    Log.e(tag, e);

                    hasCombination = false;
                }

            }
        }

        if (!hasCombination) {

            Log.i(tag, "No combination");

            destroyItems();

            return;
        }

        for (HashSet<String> strings : combinations.keySet()) {
            if (items.containsAll(strings)) {

                String item = combinations.get(strings);

                try {
                    switch (item) {
                        case "FISHING_ROD":

                            try {
                                interaction.gameScreen.interactionService.complete();

                                Story story = interaction.gameScreen.storyManager.create("a15_1d_right");

                                interaction.gameScreen.setStory(story);

                                interaction.gameScreen.storyManager.startLoadingResources();
                            } catch (Throwable e) {
                                Log.e(tag, e);
                            }

                            break;
                        case "CLOCKS":

                            Inventory inventoryCrow = InventoryRepository.find(interaction.gameScreen.game.gameState, "CROW");

                            String soundName;
                            if (inventoryService.isInInventory(inventoryCrow)) {
                                soundName = "s15_1_a_crow";
                            } else {
                                soundName = "s15_1_a";
                            }

                            final StoryAudio storyAudio = new StoryAudio();
                            storyAudio.audio = soundName;

                            interaction.gameScreen.audioService.prepare(storyAudio, new Timer.Task() {
                                @Override
                                public void run() {
                                    try {
                                        interaction.gameScreen.audioService.playAudio(storyAudio);
                                    } catch (Throwable e) {
                                        Log.e(tag, e);
                                    }
                                }
                            });

                            break;
                    }

                } catch (Throwable e) {
                    Log.e(tag, e);
                }

            }
        }

    }

    public void reset() {
        try {
            Log.i(tag, "reset");
            items.clear();

            cleanup();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public abstract class ActivePuzzleItem extends FitImage {

        public ActivePuzzleItem(Texture texture) {
            super(texture);
        }

        public void init() {

            float scale = .85f;
            float width = 200;
            float height = width * (1 / scale);

            setSize(
                    width * interaction.imageFragment.backgroundScale,
                    height * interaction.imageFragment.backgroundScale
            );

            setOrigin(Align.bottom);

            interaction.imageFragment.setPositionRelativeToBackground(this, 310, 530);
        }

        public void destroy() {
            Random r = new Random();
            addAction(
                    Actions.sequence(
                            Actions.parallel(
                                    Actions.rotateBy(r.between(0, 3) * 15 - r.between(0, 1) * 90, .5f),
                                    Actions.alpha(0, .6f)
                            ),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    remove();
                                }
                            })
                    )
            );
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
