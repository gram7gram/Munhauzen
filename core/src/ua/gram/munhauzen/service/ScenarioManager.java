package ua.gram.munhauzen.service;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Option;
import ua.gram.munhauzen.entity.OptionRepository;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.StringUtils;

public class ScenarioManager {

    private final String tag = getClass().getSimpleName();
    private final GameState gameState;

    public ScenarioManager(GameState gameState) {
        this.gameState = gameState;
    }

    public Scenario createScenario(String cid) {
        Scenario scenario = new Scenario();
        scenario.cid = StringUtils.cid();

        Option option = OptionRepository.find(gameState, cid);

        findScenario(option, scenario);

        scenario.init();

        if (!scenario.isValid()) {
            throw new IllegalArgumentException("Created scenario is not valid!");
        }

        return scenario;
    }

    public void resumeScenario() {

        Log.i(tag, "resumeScenario");

        try {

            Scenario scenario = gameState.history.activeSave.scenario;
            if (!scenario.isValid()) {
                Log.e(tag, "Scenario is not valid. Resetting");
                Option start = OptionRepository.find(gameState, GameState.INITIAL_OPTION);

                scenario = createScenario(start.id);

                gameState.history.activeSave.scenario = scenario;
            }

//                presenter.getProgressBarManager().start(scenario);


        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void findScenario(Option from, Scenario scenario) {

        Log.i(tag, "findScenario " + from + " #" + scenario.options.size);

//        BranchDecisionManager decisionManager = new BranchDecisionManager(gameBase);
//
//        Branch altBranch = decisionManager.decideCurrentBranch(from);
//        Option option = OptionRepository.find(gameBase, altBranch.getOption());
//
//        // NOTE Can be executed outside the Branch Activity
//        if (option != null && BranchPresenter.isCreated()) {
//
//            BranchPresenter presenter = BranchPresenter.getInstance();
//            RobinzonActivity activity = presenter.getActivity();
//            History history = gameBase.getHistory();
//
//            if (option.isInteraction() && !history.isInteractionCompleted(option.interaction, option.id)) {
//                InteractionStrategy strategy = InteractionFactory.createInstance(option.interaction);
//
//                if (!strategy.isSupported(activity)) {
//                    Branch nextBranch = TreeParser.findNextBranchByInventory(gameBase, altBranch);
//                    if (nextBranch != null) {
//                        findScenario(gameBase, nextBranch, scenario);
//                        return;
//                    }
//                }
//            }
//        }
//
//        if (!canStartScenario(scenario, option)) {
//
//            //NOTE Scenario is completed due to INCREMENT_DAY action in option.
//            //But new scenario will be created after.
//            TrackBranch lastBranch = scenario.last();
//            if (lastBranch != null) {
//                lastBranch.isBeforeNewScenario = true;
//            }
//            return;
//        }
//
//        TrackBranch trackBranch = new TrackBranch();
//        trackBranch.id = altBranch.cid;
//
//        scenario.branches.add(trackBranch);
//
//        if (option != null) {
//            trackBranch.duration = option.getDuration();
//            trackBranch.option = option.getId();
//            trackBranch.optionType = option.getType();
//            trackBranch.containsDayAction = option.containsDayAction();
//        }
//
//        if (canFinishScenario(altBranch, option)) return;
//
//        Option match = TreeParser.findNextBranchByInventory(gameBase, altBranch);
//
//        if (match != null) {
//
//            if (!match.isFinal() && match.isReference()) {
//                Branch reference = TreeParser.findBranchByOption(
//                        gameBase, match.getOption(), true, null);
//
//                if (reference == null) {
//                    throw new NullPointerException("Branch reference not found for " + match);
//                }
//
//                match = reference;
//            }
//
//            findScenario(match, scenario);
//        }
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
//    public void onScenarioCompleted() {
//        Log.i(tag, "Scenario completed");
//
//        if (!BranchPresenter.isCreated()) return;
//
//        final BranchPresenter presenter = BranchPresenter.getInstance();
//
//        presenter.dispatch(Event.TRACK_COMPLETED);
//    }
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
//    public boolean updateScenario(GameBase gameBase, Scenario scenario, int progress, int duration) {
//
//        boolean hasChanged = scenario.update(progress, duration);
//
//        for (TrackBranch branch : scenario.branches) {
//            Option option = OptionRepository.find(gameBase, branch.option);
//            if (option == null) continue;
//
//            if (!option.isInitialized) {
//                option.init(branch.startsAt);
//            }
//
//            option.update(progress, duration);
//        }
//
//        return hasChanged;
//    }
//
//    public void resetScenario(GameBase gameBase, Scenario scenario) {
//        if (gameBase == null) return;
//
//        for (TrackBranch branch : scenario.branches) {
//            Option option = OptionRepository.find(gameBase, branch.option);
//            if (option != null)
//                option.reset();
//        }
//    }
}