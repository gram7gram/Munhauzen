package ua.gram.munhauzen.screen.debug.fragment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.HashSet;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AchievementState;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.FailsState;
import ua.gram.munhauzen.entity.GalleryState;
import ua.gram.munhauzen.entity.GamePreferences;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.History;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.interaction.cannons.CannonsScenario;
import ua.gram.munhauzen.interaction.generals.GeneralsScenario;
import ua.gram.munhauzen.interaction.hare.HareScenario;
import ua.gram.munhauzen.interaction.picture.PictureScenario;
import ua.gram.munhauzen.interaction.servants.hire.HireScenario;
import ua.gram.munhauzen.interaction.timer.TimerScenario;
import ua.gram.munhauzen.interaction.timer2.Timer2Scenario;
import ua.gram.munhauzen.interaction.wauwau.WauScenario;
import ua.gram.munhauzen.screen.DebugAudioScreen;
import ua.gram.munhauzen.screen.DebugScreen;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.LoadingScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.service.PurchaseManager;
import ua.gram.munhauzen.service.StoryManager;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DebugFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    private final DebugScreen screen;
    public FragmentRoot root;
    ScrollPane scroll;
    TextButton startButton;
    Label upButton;
    Table inventoryContainer, scenarioContainer, endingsContainer, purchaseContainer;
    VerticalGroup group;
    String currentSource = "scenario_1";

    public DebugFragment(DebugScreen screen) {
        this.screen = screen;
        this.game = screen.game;
    }

    public void create() {
        upButton = new Label("UP", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.h1),
                Color.RED
        ));
        upButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                scroll.setScrollY(0);
            }
        });

        startButton = game.buttonBuilder.danger(game.t("debug_screen.start"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    screen.navigateTo(new GameScreen(game));

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        PrimaryButton menuButton = game.buttonBuilder.primary(game.t("debug_screen.menu"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.game.stopAllAudio();

                    screen.game.sfxService.onBackToMenuClicked();

                    screen.navigateTo(new MenuScreen(game));

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label openGalleryLbl = new Label("[+] " + game.t("debug_screen.open_gallery"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLUE
        ));
        openGalleryLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                openGalleryLbl.setVisible(false);

                try {
                    for (Image image : game.gameState.imageRegistry) {
                        game.achievementService.onImageViewed(image);
                    }

                    game.syncState();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label openChaptersLbl = new Label("[+] " + game.t("debug_screen.open_chapters"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLUE
        ));
        openChaptersLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                openChaptersLbl.setVisible(false);

                try {
                    for (Chapter c : game.gameState.chapterRegistry) {
                        game.achievementService.onChapterOpened(c);
                    }

                    game.syncState();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label openFailsLbl = new Label("[+] " + game.t("debug_screen.open_fails"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLUE
        ));
        openFailsLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                openFailsLbl.setVisible(false);

                try {
                    for (AudioFail a : game.gameState.audioFailRegistry) {
                        game.achievementService.onFailOpened(a);
                    }

                    game.syncState();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label openStatueLbl = new Label("[+] " + game.t("debug_screen.open_statues"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLUE
        ));
        openStatueLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                openStatueLbl.setVisible(false);

                try {
                    for (Inventory a : game.gameState.inventoryRegistry) {
                        if (a.isStatue) {
                            game.achievementService.onInventoryAdded(a);
                        }
                    }

                    game.syncState();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label skipLbl = new Label("[+] " + game.t("debug_screen.open_audio_test"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLUE
        ));
        skipLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.navigateTo(new DebugAudioScreen(game));
            }
        });

        final Label openScenarioLbl = new Label("[+] " + game.t("debug_screen.open_scenario"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLUE
        ));
        openScenarioLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                openScenarioLbl.setVisible(false);

                try {
                    for (Scenario s : game.gameState.scenarioRegistry) {
                        game.achievementService.onScenarioVisited(s);
                    }

                    for (CannonsScenario s : game.databaseManager.loadCannonsScenario()) {
                        game.gameState.addVisitedScenario(s.name);
                    }
                    for (GeneralsScenario s : game.databaseManager.loadGeneralsScenario()) {
                        game.gameState.addVisitedScenario(s.name);
                    }
                    for (HareScenario s : game.databaseManager.loadHareScenario()) {
                        game.gameState.addVisitedScenario(s.name);
                    }
                    for (PictureScenario s : game.databaseManager.loadPictureScenario()) {
                        game.gameState.addVisitedScenario(s.name);
                    }
                    for (HireScenario s : game.databaseManager.loadServantsHireScenario()) {
                        game.gameState.addVisitedScenario(s.name);
                    }
                    for (Timer2Scenario s : game.databaseManager.loadTimer2Scenario()) {
                        game.gameState.addVisitedScenario(s.name);
                    }
                    for (TimerScenario s : game.databaseManager.loadTimerScenario()) {
                        game.gameState.addVisitedScenario(s.name);
                    }
                    for (WauScenario s : game.databaseManager.loadWauwauScenario()) {
                        game.gameState.addVisitedScenario(s.name);
                    }

                    game.syncState();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label removeHistoryLbl = new Label("[x] " + game.t("debug_screen.purge_history"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        removeHistoryLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                removeHistoryLbl.setVisible(false);

                try {
                    game.gameState.menuState = new MenuState();
                    game.gameState.galleryState = new GalleryState();
                    game.gameState.failsState = new FailsState();
                    game.gameState.preferences = new GamePreferences();
                    game.gameState.history = new History();
                    game.gameState.achievementState = new AchievementState();
                    game.gameState.purchaseState = new PurchaseState();

                    for (String save : new String[]{"1", "2", "3", "4"}) {
                        ExternalFiles.getSaveFile(game.params, save).delete();
                    }

                    ExternalFiles.getHistoryFile(game.params).delete();
                    ExternalFiles.getMenuStateFile(game.params).delete();
                    ExternalFiles.getGalleryStateFile(game.params).delete();
                    ExternalFiles.getGamePreferencesFile(game.params).delete();
                    ExternalFiles.getFailsStateFile(game.params).delete();
                    ExternalFiles.getAchievementStateFile(game.params).delete();
                    ExternalFiles.getPurchaseStateFile(game.params).delete();

                    StoryManager storyManager = new StoryManager(null, screen.game.gameState);

                    Save save = new Save();

                    save.story = storyManager.getDefaultStory();

                    save.story.init();

                    game.gameState.setActiveSave(save);

                    game.purchaseManager.updatePurchaseState();

                    game.syncState();

                    screen.navigateTo(new DebugScreen(game));

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label removeAllLbl = new Label("[x] " + game.t("debug_screen.purge_all"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        removeAllLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    removeAllLbl.setVisible(false);

                    GameState.clearTimer(tag);

                    game.gameState = new GameState();

                    game.syncState();

                    if (Gdx.files.external(game.params.storageDirectory).exists()) {
                        Gdx.files.external(game.params.storageDirectory).deleteDirectory();
                    }

                    game.navigator.closeApp();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label expLbl = new Label("[+] " + game.t("debug_screen.updates"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLUE
        ));
        expLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    screen.navigateTo(new LoadingScreen(game));

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        Table container = new Table();
        container.padBottom(80);
        container.add(startButton)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .expandX().colspan(2).row();
        container.add(menuButton)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .colspan(2)
                .padBottom(50).expandX().row();

        container.add(expLbl).pad(10).left().expandX();
        container.add(skipLbl).pad(10).left().expandX().row();

        container.add(openGalleryLbl).pad(10).left().expandX();
        container.add(openFailsLbl).pad(10).left().expandX().row();

        container.add(openScenarioLbl).pad(10).left().expandX();
        container.add(openChaptersLbl).pad(10).left().expandX().row();

        container.add(openStatueLbl).pad(10).left().expandX();
        container.add().pad(10).left().expandX().row();

        container.add().colspan(2).pad(50).left().expandX().row();

        container.add(removeHistoryLbl).pad(10).left().expandX();
        container.add(removeAllLbl).pad(10).left().expandX().row();

        inventoryContainer = new Table();
        inventoryContainer.padBottom(80);

        endingsContainer = new Table();
        endingsContainer.padBottom(80);

        scenarioContainer = new Table();
        scenarioContainer.padBottom(80);

        purchaseContainer = new Table();
        purchaseContainer.padBottom(80);

        createInventoryTable();

        createScenarioTable();

        createEndingsTable();

        createPurchaseContainer();

        Table container2 = new Table();
        container2.add(inventoryContainer).pad(10).top().expandX();
        container2.add(scenarioContainer).pad(10).top().expandX();

        Table container3 = new Table();
        container3.add(endingsContainer).pad(10).top().expandX();

        Table upContainer = new Table();
        upContainer.add(upButton).pad(10).align(Align.bottomRight).expand();

        group = new VerticalGroup();
        group.pad(10);
        group.addActor(container);
        group.addActor(purchaseContainer);
        group.addActor(container3);
        group.addActor(container2);

        scroll = new ScrollPane(group);
        scroll.setFillParent(true);
        scroll.setName(tag);

        root = new FragmentRoot();
        root.addContainer(scroll);
        root.addContainer(upContainer);
    }

    private void createPurchaseContainer() {

        final PurchaseState state = game.gameState.purchaseState;

        if (state.purchases == null) {
            state.purchases = new ArrayList<>();
        }

        if (state.promocodes == null) {
            state.promocodes = new ArrayList<>();
        }

        if (state.referrals == null) {
            state.referrals = new ArrayList<>();
        }

        purchaseContainer.clearChildren();

        Label title = new Label("Purchase info", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));

        Label lbl1 = new Label("Last chapter Part_demo: " + PurchaseManager.demoEndsAtChapter, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));

        Label lbl2 = new Label("Last chapter Part_1: " + PurchaseManager.part1EndsAtChapter, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));

        Label lbl3 = new Label("Last chapter Part_2: " + PurchaseManager.maxChapter, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));

        Label lbl4 = new Label("[!] Purchased chapters: " + state.maxChapter, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));

        Label lbl5 = new Label("[!] Current expansion: " + state.currentExpansionVersion, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));

        purchaseContainer.add(title).left().expandX().padBottom(5).row();
        purchaseContainer.add(lbl1).left().expandX().padBottom(5).row();
        purchaseContainer.add(lbl2).left().expandX().padBottom(5).row();
        purchaseContainer.add(lbl3).left().expandX().padBottom(5).row();
        purchaseContainer.add(lbl4).left().expandX().padBottom(5).row();
        purchaseContainer.add(lbl5).left().expandX().padBottom(40).row();

        Label title4 = new Label("Purchases", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));

        purchaseContainer.add(title4).left().expandX().padBottom(5).row();

        if (state.purchases.isEmpty()) {

            Label lbl = new Label("None", new Label.LabelStyle(
                    game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                    Color.BLACK
            ));

            purchaseContainer.add(lbl).left().expandX().padBottom(5).row();

        } else {
            for (Purchase purchase : state.purchases) {
                Label lbl = new Label(purchase.productId, new Label.LabelStyle(
                        game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                        Color.BLACK
                ));

                purchaseContainer.add(lbl).left().expandX().padBottom(5).row();
            }
        }


        Label title3 = new Label("Promocodes", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));

        purchaseContainer.add(title3).left().expandX().padBottom(5).row();

        if (state.promocodes.isEmpty()) {

            Label lbl = new Label("None", new Label.LabelStyle(
                    game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                    Color.BLACK
            ));

            purchaseContainer.add(lbl).left().expandX().padBottom(5).row();

        } else {
            for (String promocode : state.promocodes) {
                Label lbl = new Label(promocode, new Label.LabelStyle(
                        game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                        Color.BLACK
                ));

                purchaseContainer.add(lbl).left().expandX().padBottom(5).row();
            }
        }

        Label title2 = new Label("Referrals x" + state.referralCount, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));

        purchaseContainer.add(title2).left().expandX().padBottom(5).row();

        if (state.referrals.isEmpty()) {

            Label lbl = new Label("None", new Label.LabelStyle(
                    game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                    Color.BLACK
            ));

            purchaseContainer.add(lbl).left().expandX().padBottom(5).row();

        } else {
            for (String ref : state.referrals) {
                Label lbl = new Label(ref, new Label.LabelStyle(
                        game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                        Color.BLACK
                ));

                purchaseContainer.add(lbl).left().expandX().padBottom(5).row();
            }
        }

        Label btn = new Label("[+] Add referral", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    game.referralService.setReferralCount(state.referralCount + 1);

                    game.syncState();

                    createPurchaseContainer();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        purchaseContainer.add(btn).left().expandX().padBottom(5).row();

        Label btn2 = new Label("[-] Reduce referral", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        btn2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    game.referralService.setReferralCount(state.referralCount - 1);

                    game.syncState();

                    createPurchaseContainer();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        purchaseContainer.add(btn2).left().expandX().padBottom(5).row();

    }

    public void createEndingsTable() {

        endingsContainer.clearChildren();

        Label header = new Label(game.t("debug_screen.endings"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        endingsContainer.add(header).expandX().row();


        String text;

        if (MunhauzenGame.developmentVictory) {
            text = "[+] VICTORY";
        } else {
            text = "[-] VICTORY";
        }

        Label label = new Label(text, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));

        label.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                MunhauzenGame.developmentVictory = !MunhauzenGame.developmentVictory;

                createEndingsTable();
            }
        });

        endingsContainer.add(label).left().expandX().padBottom(5).row();
    }

    public void createInventoryTable() {

        inventoryContainer.clearChildren();

        Label header = new Label(game.t("debug_screen.inventory"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        inventoryContainer.add(header).expandX().row();

        HashSet<String> allInventory = game.inventoryService.getAllInventory();

        for (final Inventory inventory : game.gameState.inventoryRegistry) {

            String name = inventory.name;
            boolean contains = allInventory.contains(name);
            if (contains) {
                name = "[+] " + name;
            } else {
                name = "[-] " + name;
            }

            Label label = new Label(name, new Label.LabelStyle(
                    game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                    Color.BLACK
            ));

            label.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    if (game.inventoryService.isInInventory(inventory)) {
                        game.inventoryService.remove(inventory);
                    } else {

                        game.achievementService.onInventoryAdded(inventory);
                    }

                    game.syncState();

                    createInventoryTable();

                }
            });

            inventoryContainer.add(label).left().expandX().padBottom(5).row();
        }
    }

    public void createScenarioTable() {

        scenarioContainer.clearChildren();

        Label header = new Label(game.t("debug_screen.scenario"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        scenarioContainer.add(header).colspan(3).expandX().row();

        Label part1 = new Label("[1]", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        part1.setAlignment(Align.center);
        scenarioContainer.add(part1).pad(10).center().growX();

        Label part2 = new Label("[2]", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        part2.setAlignment(Align.center);
        scenarioContainer.add(part2).pad(10).center().growX();

        Label part3 = new Label("[3]", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        part3.setAlignment(Align.center);
        scenarioContainer.add(part3).pad(10).center().growX();

        scenarioContainer.row();

        ArrayList<Scenario> filtered = new ArrayList<>();

        for (Scenario scenario : game.gameState.scenarioRegistry) {
            if (currentSource.equals(scenario.source)) {
                filtered.add(scenario);
            }
        }

        for (final Scenario scenario : filtered) {

            String name = scenario.name + (scenario.interaction != null ? " " + scenario.interaction : "");

            if (scenario.name.equals(MunhauzenGame.developmentScenario)) {
                name = "[+] " + name;
            } else {
                name = "[-] " + name;
            }

            Label label = new Label(name, new Label.LabelStyle(
                    game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                    Color.BLACK
            ));

            label.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    game.gameState.activeSave.story = null;

                    if (scenario.name.equals(MunhauzenGame.developmentScenario)) {
                        MunhauzenGame.developmentScenario = null;
                    } else {
                        MunhauzenGame.developmentScenario = scenario.name;
                    }

                    createScenarioTable();

                }
            });

            scenarioContainer.add(label).colspan(3).left().expandX().padBottom(5).row();
        }

        part1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                currentSource = "scenario_1";

                createScenarioTable();

            }
        });

        part2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                currentSource = "scenario_2";

                createScenarioTable();
            }
        });

        part3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                currentSource = "scenario_3";

                createScenarioTable();
            }
        });
    }

    public void update() {
        upButton.setVisible(scroll.getScrollY() > 0);
    }

    @Override
    public Actor getRoot() {
        return root;
    }
}
