package ua.gram.munhauzen.interaction.puzzle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ua.gram.munhauzen.entity.Inventory;
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
    StoryAudio bombAudio, crowAudio, clockAudio, rodAudio, destroyAudio;
    Timer.Task bombTask, crowTask, clockTask, rodTask1, rodTask2, destroyTask;

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

            InventoryService inventoryService = interaction.gameScreen.game.inventoryService;

            Inventory inventoryCrow = InventoryRepository.find(interaction.gameScreen.game.gameState, "CROW");
            Inventory inventoryBomb = InventoryRepository.find(interaction.gameScreen.game.gameState, "BOMB");
            Inventory inventoryClock = InventoryRepository.find(interaction.gameScreen.game.gameState, "CLOCKS");
            Inventory inventoryRod = InventoryRepository.find(interaction.gameScreen.game.gameState, "FISHING_ROD");

            boolean hasBomb = inventoryService.isInInventory(inventoryBomb);
            boolean hasRod = inventoryService.isInInventory(inventoryRod);
            boolean hasClock = inventoryService.isInInventory(inventoryClock);
            boolean hasCrow = inventoryService.isInInventory(inventoryCrow);

            interaction.imageFragment.peas.setVisible(!(hasBomb && hasRod));

            interaction.imageFragment.shoes.setVisible(!hasCrow);
            interaction.imageFragment.hair.setVisible(!hasCrow);
            interaction.imageFragment.stick.setVisible(!hasCrow);

            interaction.imageFragment.clocks.setVisible(!hasClock);
            interaction.imageFragment.key.setVisible(!hasClock);
            interaction.imageFragment.arrows.setVisible(!hasClock);

            interaction.imageFragment.spoon.setVisible(!hasRod);
            interaction.imageFragment.rope.setVisible(!hasRod);

            interaction.imageFragment.powder.setVisible(!hasBomb);

            interaction.gameScreen.audioService.stop(tag);

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
                        activeClocks.setZIndex(0);

                        interaction.imageFragment.resultGroup.addActor(activeClocks);
                        interaction.imageFragment.clocks.setVisible(false);
                        break;
                    case "spoon":
                        activeSpoon = new ActiveSpoon(texture);

                        activeSpoon.init();
                        activeSpoon.setZIndex(1);

                        interaction.imageFragment.resultGroup.addActor(activeSpoon);
                        interaction.imageFragment.spoon.setVisible(false);
                        break;
                    case "shoes":
                        activeShoes = new ActiveShoes(texture);

                        activeShoes.init();
                        activeShoes.setZIndex(2);

                        interaction.imageFragment.resultGroup.addActor(activeShoes);
                        interaction.imageFragment.shoes.setVisible(false);
                        break;
                    case "peas":

                        activePeas = new ActivePeas(texture);

                        activePeas.init();
                        activePeas.setZIndex(3);

                        interaction.imageFragment.resultGroup.addActor(activePeas);
                        interaction.imageFragment.peas.setVisible(false);
                        break;
                    case "key":

                        activeKey = new ActiveKey(texture);

                        activeKey.init();
                        activeKey.setZIndex(4);

                        interaction.imageFragment.resultGroup.addActor(activeKey);
                        interaction.imageFragment.key.setVisible(false);
                        break;
                    case "hair":
                        activeHair = new ActiveHair(texture);

                        activeHair.init();
                        activeHair.setZIndex(5);

                        interaction.imageFragment.resultGroup.addActor(activeHair);
                        interaction.imageFragment.hair.setVisible(false);
                        break;
                    case "arrows":
                        activeArrows = new ActiveArrows(texture);

                        activeArrows.init();
                        activeArrows.setZIndex(6);

                        interaction.imageFragment.resultGroup.addActor(activeArrows);
                        interaction.imageFragment.arrows.setVisible(false);
                        break;
                    case "rope":

                        activeRope = new ActiveRope(texture);

                        activeRope.init();
                        activeRope.setZIndex(7);

                        interaction.imageFragment.resultGroup.addActor(activeRope);
                        interaction.imageFragment.rope.setVisible(false);
                        break;
                    case "stick":
                        activeStick = new ActiveStick(texture);

                        activeStick.init();
                        activeStick.setZIndex(8);

                        interaction.imageFragment.resultGroup.addActor(activeStick);
                        interaction.imageFragment.stick.setVisible(false);
                        break;
                    case "powder":

                        activePowder = new ActivePowder(texture);

                        activePowder.init();
                        activePowder.setZIndex(9);

                        interaction.imageFragment.resultGroup.addActor(activePowder);
                        interaction.imageFragment.powder.setVisible(false);
                        break;
                }
            }

            if (items.size() == 2) {
                checkCombination(false);
            } else if (items.size() == 3) {
                checkCombination(true);
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void destroyItems() {

        InventoryService inventoryService = interaction.gameScreen.game.inventoryService;

        try {
            Inventory inventoryCrow = InventoryRepository.find(interaction.gameScreen.game.gameState, "CROW");

            String soundName;
            if (!inventoryService.isInInventory(inventoryCrow)) {

                soundName = MathUtils.random(new String[]{
                        "s15broken_1", "s15broken_2", "s15broken_3", "s15broken_4", "s15broken_5"
                });
            } else {

                soundName = MathUtils.random(new String[]{
                        "s15crow_1", "s15crow_2", "s15crow_3", "s15crow_4", "s15crow_5"
                });
            }

            destroyAudio = new StoryAudio();
            destroyAudio.audio = soundName;

            interaction.gameScreen.audioService.prepareAndPlay(destroyAudio);

            destroyTask = Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    reset();
                }
            }, destroyAudio.duration / 1000f);

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

            interaction.gameScreen.onCriticalError(e);
        }

    }

    public void checkCombination(boolean canDestroy) {

        Log.i(tag, "checkCombination");

        interaction.imageFragment.root.setTouchable(Touchable.disabled);

        InventoryService inventoryService = interaction.gameScreen.game.inventoryService;

        Log.i(tag, "items: " + Arrays.toString(items.toArray()));

        boolean hasCombination = false, hasClock = false, hasRod = false, hasCrow = false, hasBomb = false;

        for (HashSet<String> strings : combinations.keySet()) {
            if (items.containsAll(strings)) {

                try {
                    hasCombination = true;

                    String item = combinations.get(strings);

                    Log.i(tag, "Has combination! " + item);

                    Inventory inventory = InventoryRepository.find(interaction.gameScreen.game.gameState, item);

                    if (!inventoryService.isInInventory(inventory)) {

                        if (!hasClock)
                            hasClock = item.equals("CLOCKS");

                        if (!hasRod)
                            hasRod = item.equals("FISHING_ROD");

                        if (!hasCrow)
                            hasCrow = item.equals("CROW");

                        if (!hasBomb)
                            hasBomb = item.equals("BOMB");

                        if (inventory.isGlobal()) {
                            inventoryService.addGlobalInventory(inventory);
                        } else {
                            inventoryService.addSaveInventory(inventory);
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

            if (canDestroy) {
                destroyItems();
            } else {
                interaction.imageFragment.root.setTouchable(Touchable.enabled);
            }

            return;
        }

        if (hasCrow) {

            onCrowCombination(new Timer.Task() {
                @Override
                public void run() {
                    reset();
                }
            });

        } else if (hasBomb) {

            onBombCombination(new Timer.Task() {
                @Override
                public void run() {
                    reset();
                }
            });

        } else if (hasClock) {

            onClockCombination(new Timer.Task() {
                @Override
                public void run() {
                    reset();
                }
            });

        } else if (hasRod) {

            onRodCombination();

        } else {
            animateCombinationAndReset();
        }
    }

    private void onRodCombination() {
        Log.i(tag, "onRodCombination");

        animateCombination();

        try {
            rodAudio = new StoryAudio();
            rodAudio.audio = "s15_1_d";

            interaction.gameScreen.audioService.prepareAndPlay(rodAudio);

            interaction.assetManager.load("puzzle/pbear_fin.jpg", Texture.class);

            rodTask1 = Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    try {

                        interaction.imageFragment.sourceGroup.addAction(
                                Actions.sequence(
                                        Actions.alpha(0, .3f),
                                        Actions.visible(false)
                                )
                        );
                        interaction.imageFragment.resultGroup.addAction(
                                Actions.sequence(
                                        Actions.alpha(0, .3f),
                                        Actions.visible(false)
                                )
                        );
                        interaction.imageFragment.resetButton.addAction(
                                Actions.sequence(
                                        Actions.alpha(0, .3f),
                                        Actions.visible(false)
                                )
                        );

                        interaction.imageFragment.setBackground(
                                interaction.assetManager.get("puzzle/pbear_fin.jpg", Texture.class),
                                "puzzle/pbear_fin.jpg"
                        );

                    } catch (Throwable e) {
                        Log.e(tag, e);

                        interaction.gameScreen.onCriticalError(e);
                    }
                }
            }, 10);

            rodTask2 = Timer.instance().scheduleTask(new Timer.Task() {
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
            }, rodAudio.duration / 1000f);

            interaction.assetManager.finishLoading();
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    private void onClockCombination(Timer.Task onComplete) {

        InventoryService inventoryService = interaction.gameScreen.game.inventoryService;

        animateCombination();

        try {
            Log.i(tag, "onClockCombination");

            Inventory inventoryCrow = InventoryRepository.find(interaction.gameScreen.game.gameState, "CROW");

            String soundName;
            if (inventoryService.isInInventory(inventoryCrow)) {
                soundName = "s15_1_a_crow";
            } else {
                soundName = "s15_1_a";
            }

            clockAudio = new StoryAudio();
            clockAudio.audio = soundName;

            interaction.gameScreen.audioService.prepareAndPlay(clockAudio);

            clockTask = Timer.instance().scheduleTask(onComplete, clockAudio.duration / 1000f);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    private void onCrowCombination(Timer.Task onComplete) {

        animateCombination();

        try {
            Log.i(tag, "onCrowCombination");

            crowAudio = new StoryAudio();
            crowAudio.audio = "s15_1_b";

            interaction.gameScreen.audioService.prepareAndPlay(crowAudio);

            crowTask = Timer.instance().scheduleTask(onComplete, crowAudio.duration / 1000f);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    private void onBombCombination(Timer.Task onComplete) {

        InventoryService inventoryService = interaction.gameScreen.game.inventoryService;

        animateCombination();

        try {
            Log.i(tag, "onBombCombination");

            Inventory inventoryCrow = InventoryRepository.find(interaction.gameScreen.game.gameState, "CROW");

            String soundName;
            if (inventoryService.isInInventory(inventoryCrow)) {
                soundName = "s15_1_с_crow";
            } else {
                soundName = "s15_1_с";
            }

            bombAudio = new StoryAudio();
            bombAudio.audio = soundName;

            interaction.gameScreen.audioService.prepareAndPlay(bombAudio);

            bombTask = Timer.instance().scheduleTask(onComplete, bombAudio.duration / 1000f);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void reset() {
        try {
            Log.i(tag, "reset");

            interaction.imageFragment.root.setTouchable(Touchable.enabled);

            items.clear();

            dispose();

            cleanup();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void animateCombinationAndReset() {
        Log.i(tag, "animateCombination");

        interaction.imageFragment.resultGroup.setOrigin(
                interaction.imageFragment.resultGroup.getWidth() / 2f,
                interaction.imageFragment.resultGroup.getHeight() / 2f
        );
        interaction.imageFragment.resultGroup.addAction(Actions.sequence(
                Actions.moveBy(0, 40, .3f),
                Actions.moveBy(0, -40, .3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        reset();
                    }
                })
        ));
    }

    private void animateCombination() {
        Log.i(tag, "animateCombination");

        interaction.imageFragment.resultGroup.setOrigin(
                interaction.imageFragment.resultGroup.getWidth() / 2f,
                interaction.imageFragment.resultGroup.getHeight() / 2f
        );
        interaction.imageFragment.resultGroup.addAction(Actions.sequence(
                Actions.moveBy(0, 40, .3f),
                Actions.moveBy(0, -40, .3f)
        ));
    }

    public void update() {

        if (bombAudio != null) {
            interaction.gameScreen.audioService.updateVolume(bombAudio);
        }

        if (crowAudio != null) {
            interaction.gameScreen.audioService.updateVolume(crowAudio);
        }

        if (rodAudio != null) {
            interaction.gameScreen.audioService.updateVolume(rodAudio);
        }

        if (destroyAudio != null) {
            interaction.gameScreen.audioService.updateVolume(destroyAudio);
        }
    }

    public void dispose() {
        Log.i(tag, "dispose");

        if (destroyTask != null) {
            destroyTask.cancel();
            destroyTask = null;
        }
        if (bombTask != null) {
            bombTask.cancel();
            bombTask = null;
        }

        if (crowTask != null) {
            crowTask.cancel();
            crowTask = null;
        }

        if (clockTask != null) {
            clockTask.cancel();
            clockTask = null;
        }

        if (rodTask1 != null) {
            rodTask1.cancel();
            rodTask1 = null;
        }

        if (rodTask2 != null) {
            rodTask2.cancel();
            rodTask2 = null;
        }

        if (destroyAudio != null) {
            interaction.gameScreen.audioService.stop(destroyAudio);
            destroyAudio = null;
        }

        if (bombAudio != null) {
            interaction.gameScreen.audioService.stop(bombAudio);
            rodAudio = null;
        }

        if (crowAudio != null) {
            interaction.gameScreen.audioService.stop(crowAudio);
            rodAudio = null;
        }

        if (rodAudio != null) {
            interaction.gameScreen.audioService.stop(rodAudio);
            rodAudio = null;
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
                    width * interaction.imageFragment.backgroundImage.backgroundScale,
                    height * interaction.imageFragment.backgroundImage.backgroundScale
            );

            setOrigin(Align.bottom);

            interaction.imageFragment.setPositionRelativeToBackground(this, 310, 630);
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
