package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Set;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Option;
import ua.gram.munhauzen.entity.OptionAudio;
import ua.gram.munhauzen.entity.OptionImage;
import ua.gram.munhauzen.entity.OptionRepository;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.ScenarioOption;
import ua.gram.munhauzen.fragment.ScenarioFragment;
import ua.gram.munhauzen.history.Save;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.StringUtils;

public class ScenarioManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final GameState gameState;

    public ScenarioManager(GameScreen gameScreen, GameState gameState) {
        this.gameScreen = gameScreen;
        this.gameState = gameState;
    }

    public Scenario createScenario(String optionId) {

        resetScenario();

        Scenario scenario = new Scenario();
        scenario.cid = StringUtils.cid();

        Option option = OptionRepository.find(gameState, optionId);

        findScenario(option, scenario);

        scenario.init();

        String log = "createScenario " + scenario.cid + " x" + scenario.options.size;

        for (ScenarioOption scenarioOption : scenario.options) {
            log += scenarioOption.startsAt + "-" + scenarioOption.finishesAt + " " + scenarioOption.option.id + "\r\n";

            for (OptionAudio audio : scenarioOption.option.audio) {
                log += " - audio " + audio.id + " " + audio.startsAt + "-" + audio.finishesAt + "\r\n";
            }

            for (OptionImage image : scenarioOption.option.images) {
                log += " - image " + image.id + " " + image.startsAt + "-" + image.finishesAt + "\r\n";
            }
        }

        Log.i(tag, log);

        if (!scenario.isValid()) {
            throw new IllegalArgumentException("Created scenario is not valid!");
        }

        return scenario;
    }

    public void resumeScenario() {

        Scenario scenario = gameScreen.getScenario();
        if (scenario == null || !scenario.isValid()) {
            Log.e(tag, "Scenario is not valid. Resetting");

            Option start = OptionRepository.find(gameState, Option.BEGIN);

            gameScreen.setScenario(createScenario(start.id));
        }

        Log.i(tag, "resumeScenario " + gameScreen.getScenario().cid);

    }

    private void findScenario(Option from, Scenario scenario) {

        Log.i(tag, "findScenario " + from.id + " #" + scenario.options.size);

        Set<String> inventory = gameState.history.activeSave.getUniqueInventory();

        ScenarioOption scenarioOption = new ScenarioOption();
        scenarioOption.option = from;
        scenarioOption.duration = 0;

        scenario.options.add(scenarioOption);

        if ("GOTO".equals(from.action)) {
            for (Decision decision : from.decisions) {
                if (isDecisionAvailable(decision, inventory)) {

                    Option next = OptionRepository.find(gameState, decision.option);

                    findScenario(next, scenario);
                }
            }
        }
    }

    public void updateScenario(float progress, int duration) {

        Scenario scenario = gameState.history.activeSave.scenario;

        scenario.update(progress, duration);
    }

    public void startLoadingResources(Scenario scenario) {
        ScenarioOption option = scenario.currentOption;
        if (option == null) return;

        final OptionAudio optionAudio = option.currentAudio;
        if (optionAudio != null) {
            gameScreen.audioService.prepare(optionAudio, new Timer.Task() {
                @Override
                public void run() {
                    gameScreen.audioService.onPrepared(optionAudio);
                }
            });

            if (optionAudio.next != null) {
                gameScreen.audioService.prepare(optionAudio.next, new Timer.Task() {
                    @Override
                    public void run() {

                    }
                });
            }
        }

        final OptionImage optionImage = option.currentImage;
        if (optionImage != null) {
            gameScreen.imageService.prepare(optionImage, new Timer.Task() {
                @Override
                public void run() {
                    gameScreen.imageService.onPrepared(optionImage);
                }
            });

            if (optionImage.next != null) {
                gameScreen.imageService.prepare(optionImage.next, new Timer.Task() {
                    @Override
                    public void run() {

                    }
                });
            }
        }

    }

    public void onScenarioCompleted() {

        Scenario scenario = gameState.history.activeSave.scenario;
        Log.i(tag, "onScenarioCompleted " + scenario.cid);

        for (ScenarioOption scenarioOption : scenario.options) {

            String newInventory = scenarioOption.option.inventoryAdd;
            if (newInventory != null) {
                gameState.history.activeSave.inventory.add(newInventory);
            }

            String newGlobalInventory = scenarioOption.option.inventoryGlobalAdd;
            if (newGlobalInventory != null) {
                gameState.history.globalInventory.add(newGlobalInventory);
            }
        }

        Set<String> inventory = gameState.history.activeSave.getUniqueInventory();

        for (OptionAudio audio : scenario.currentOption.option.audio) {
            if (audio.player != null) {
                audio.player.pause();
            }
        }

        ArrayList<Decision> availableDecisions = new ArrayList<>();
        for (Decision decision : scenario.currentOption.option.decisions) {
            if (isDecisionAvailable(decision, inventory)) {
                availableDecisions.add(decision);
            }
        }

        if (gameScreen.scenarioFragment != null) {
            gameScreen.scenarioFragment.dispose();
        }
        gameScreen.scenarioFragment = new ScenarioFragment(gameScreen);

        gameScreen.setScenarioOptionsLayer(
                gameScreen.scenarioFragment.create(availableDecisions)
        );
    }

    public void resetScenario() {

        Save save = gameState.history.activeSave;

        Scenario scenario = save.scenario;
        if (scenario == null) return;

        Log.i(tag, "resetScenario " + scenario.cid);

        if (!save.scenarioStack.contains(scenario)) {
            save.scenarioStack.push(scenario);
        }

        for (ScenarioOption scenarioOption : scenario.options) {

            for (OptionAudio audio : scenarioOption.option.audio) {
                String resource = audio.getResource();
                if (gameScreen.assetManager.isLoaded(resource, Music.class)) {
                    if (gameScreen.assetManager.getReferenceCount(resource) == 0) {
                        gameScreen.assetManager.unload(resource);
                    }
                }
            }

            for (OptionImage image : scenarioOption.option.images) {
                String resource = image.getResource();
                if (gameScreen.assetManager.isLoaded(resource, Texture.class)) {
                    if (gameScreen.assetManager.getReferenceCount(resource) == 0) {
                        gameScreen.assetManager.unload(resource);
                    }
                }
            }

            scenarioOption.reset();
        }

        gameScreen.assetManager.finishLoading();

        scenario.reset();
    }

    private boolean isDecisionAvailable(Decision decision, Set<String> inventory) {
        boolean hasRequired = true;
        if (decision.inventoryRequired != null) {
            for (String item : decision.inventoryRequired) {
                if (!inventory.contains(item)) {
                    hasRequired = false;
                    break;
                }
            }
        }

        boolean hasAbsent = false;
        if (decision.inventoryAbsent != null) {
            for (String item : decision.inventoryAbsent) {
                if (inventory.contains(item)) {
                    hasAbsent = true;
                    break;
                }
            }
        }

        return hasRequired && !hasAbsent;
    }
}