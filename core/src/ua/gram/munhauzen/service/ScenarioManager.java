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

        Log.i(tag, "createScenario " + scenario.cid);

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

        Scenario scenario = gameState.history.activeSave.scenario;
        if (scenario == null || !scenario.isValid()) {
            Log.e(tag, "Scenario is not valid. Resetting");
            Option start = OptionRepository.find(gameState, GameState.INITIAL_OPTION);

            gameState.history.activeSave.scenario = createScenario(start.id);
        }

        Log.i(tag, "resumeScenario " + gameState.history.activeSave.scenario.cid);

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

        for (ScenarioOption scenarioOption : scenario.options) {
            scenarioOption.update(progress, duration);
        }
    }

    public void startLoadingResources(Scenario scenario) {
        ScenarioOption option = scenario.currentOption;

        if (option == null) {
            throw new NullPointerException("Missing current option in scenario " + scenario.cid);
        }

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

    //    public void onBranchCompleted(TrackBranch trackBranch, GameBase gameBase) {
//
//        if (trackBranch == null) return;
//
//        if (gameBase == null) return;
//
//        History history = gameBase.getHistory();
//        if (history == null) return;
//
//        if (!BranchPresenter.isCreated()) return;
//
//        BranchPresenter presenter = BranchPresenter.getInstance();
//        AchievementPointService aps = presenter.getAchievementPointService();
//
//        Branch currentBranch = TreeParser.findBranchByCid(gameBase, trackBranch.id);
//        Option currentOption = OptionRepository.find(gameBase, trackBranch.option);
//
//        Log.i(tag, "onBranchCompleted " + currentBranch);
//
//        if (currentOption != null) {
//            for (String action : currentOption.getActions()) {
//
//                switch (action) {
//                    case Option.ACTION_DISABLE_ME:
//
//                        if (history.containsInOptionActionHistory(currentBranch, action)) continue;
//
//                        history.addCompletedOptionAction(currentBranch.cid, action);
//                        history.addDisabledOption(currentOption.id);
//
//                        break;
//                }
//            }
//
//            history.addPlayerEvent(currentOption.id, Category.BRANCH);
//
//            if (history.addCompletedOption(currentOption.id)) {
//                aps.checkIfOptionCompleted(currentOption.id);
//            }
//        }
//
//        history.addStep(currentBranch);
//
//        SuperBonusManager.unlock(gameBase, aps);
//    }
//
//    public void onBranchStarted(TrackBranch trackBranch, GameBase gameBase) throws ScenarioInterruptedException {
//
//        if (trackBranch == null) return;
//
//        if (gameBase == null) return;
//
//        History history = gameBase.getHistory();
//        if (history == null) return;
//
//        if (!BranchPresenter.isCreated()) return;
//
//        BranchPresenter presenter = BranchPresenter.getInstance();
//
//        Branch branch = TreeParser.findBranchByCid(gameBase, trackBranch.id);
//
//        Log.i(tag, "onBranchStarted " + branch);
//
//        Option option = OptionRepository.find(gameBase, trackBranch.option);
//        if (option != null) {
//
//            for (Item item : option.items) {
//                history.addItem(item);
//            }
//
//            for (String action : option.actions) {
//
//                switch (action) {
//                    case Option.ACTION_INCREMENT_DAY:
//
//                        if (!history.containsInOptionActionHistory(branch, action)) {
//
//                            history.incrementDay();
//
//                            history.addCompletedOptionAction(branch.cid, action);
//
//                            presenter.dispatch(Event.INCREMENT_DAY, history.getDay());
//                        }
//
//                        break;
//                    case Option.ACTION_ADVERTISEMENT:
//
//                        if (!history.containsInOptionActionHistory(branch, action)) {
//
//                            if (AdvService.isEnabled && NetworkService.isNetworkAvailable(presenter.getActivity())) {
//
//                                history.addCompletedOptionAction(branch.cid, action);
//                                presenter.dispatch(Event.ADVERTISEMENT, option.id);
//
//                                throw new ScenarioInterruptedException(action);
//                            }
//                        }
//
//                        break;
//                }
//            }
//        }
//    }
//
//    public void onFinaleReached(Scenario scenario) {
//
//        if (!BranchPresenter.isCreated()) return;
//
//        try {
//            final BranchPresenter presenter = BranchPresenter.getInstance();
//            final BranchActivity activity = presenter.getActivity();
//            final Handler handler = presenter.getHandler();
//
//            Branch branch = TreeParser.findBranchByCid(presenter.getGameBase(), scenario.currentBranch.id);
//
//            String option = scenario.currentBranch.option;
//
//            Log.i(tag, "Final branch reached " + option);
//
//            final OptionParams params;
//            switch (option) {
//                case Option.DEATH:
//
//                    GameBase.isFinaleReached = true;
//
//                    //NOTE OptionParams are ignored
//
//                    activity.redirectToDefeatActivity();
//
//                    break;
//                case Option.VICTORY:
//
//                    GameBase.isFinaleReached = true;
//
//                    params = branch.getParams();
//                    if (params != null) {
//                        if (handler != null)
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    activity.redirectToVictoryActivity(params);
//                                }
//                            }, params.getDelayX());
//                    } else {
//                        activity.redirectToVictoryActivity();
//                    }
//                    break;
//            }
//        } catch (Throwable e) {
//            Log.e(tag, e);
//        }
//
//    }
//
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
                audio.player.stop();
            }
        }

        ArrayList<Decision> availableDecisions = new ArrayList<>();
        for (Decision decision : scenario.currentOption.option.decisions) {
            if (isDecisionAvailable(decision, inventory)) {
                availableDecisions.add(decision);
            }
        }

        gameScreen.scenarioFragment = new ScenarioFragment(gameScreen);

        gameScreen.setScenarioOptionsLayer(
                gameScreen.scenarioFragment.create(availableDecisions)
        );
    }

    //
//    public void onInteractionReached(Scenario scenario) {
//
//        Log.i(tag, "onInteractionReached");
//
//        if (!BranchPresenter.isCreated()) return;
//
//        final BranchPresenter presenter = BranchPresenter.getInstance();
//
//        presenter.getBackgroundAudioService().destroy();
//
//        presenter.getInteractionService().execute(scenario);
//    }
//
//    public void onNewScenarioReached(final Scenario scenario) {
//
//        Log.i(tag, "onNewScenarioReached");
//
//        if (!BranchPresenter.isCreated()) return;
//
//        final BranchPresenter presenter = BranchPresenter.getInstance();
//        final GameBase gameBase = presenter.getGameBase();
//
//        presenter.getBackgroundAudioService().destroy();
//
//        presenter.onHistoryAvailable(new Presenter.OnHistoryAvailable() {
//            @Override
//            public void onSuccess(History history) {
//
//                Branch currentBranch = TreeParser.findBranchByCid(gameBase, scenario.currentBranch.id);
//                Branch nextBranch = TreeParser.findNextBranchByInventory(gameBase, currentBranch);
//                if (nextBranch == null) {
//                    throw new NullPointerException("Next branch was not found for " + currentBranch);
//                }
//
//                history.getActiveSave().clickedBranches.add(nextBranch.cid);
//
//                Scenario nextScenario = createScenario(gameBase, nextBranch.cid);
//
//                history.setScenario(nextScenario);
//
//                GameplayService.resumeGameAndStartScenario();
//            }
//
//            @Override
//            public void onError() {
//                presenter.getActivity().redirectToDefeatActivity();
//            }
//        });
//
//    }
//
//    public boolean canFinishScenario(Branch branch, Option option) {
//
//        boolean isFinalBranch = branch.isFinal() || branch.isClick();
//        boolean isInteraction = option != null && option.isInteraction();
//
//        return isFinalBranch || isInteraction;
//    }
//
//    public boolean canStartScenario(Scenario scenario, Option option) {
//        if (scenario.branches.size() == 0) return true;
//
//        for (TrackBranch branch : scenario.branches) {
//            if (branch.containsDayAction) {
//                return false;
//            }
//        }
//
//        return option == null || !option.containsDayAction();
//    }
//
//
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

    public void postUpdate(Scenario scenario) {

        for (ScenarioOption option : scenario.options) {
            if (option != scenario.currentOption) {
                for (OptionAudio audio : option.option.audio) {
                    Music player = audio.player;
                    if (player != null) {
                        if (player.isPlaying()) {
                            player.stop();
                        }
                    }
                }
            }
        }

        if (scenario.currentOption != null) {
            if (scenario.currentOption.currentAudio != null) {
                Music player = scenario.currentOption.currentAudio.player;
                if (player != null) {
                    if (GameState.isPaused) {
                        player.pause();
                    } else if (!scenario.isCompleted && !player.isPlaying()) {
                        player.play();
                    }
                }
            }
        }
    }
}