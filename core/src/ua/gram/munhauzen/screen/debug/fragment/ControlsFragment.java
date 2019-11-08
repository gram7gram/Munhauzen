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
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.FailsState;
import ua.gram.munhauzen.entity.GalleryState;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.History;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.interaction.InteractionFactory;
import ua.gram.munhauzen.interaction.cannons.CannonsScenario;
import ua.gram.munhauzen.interaction.generals.GeneralsScenario;
import ua.gram.munhauzen.interaction.hare.HareScenario;
import ua.gram.munhauzen.interaction.picture.PictureScenario;
import ua.gram.munhauzen.interaction.servants.hire.HireScenario;
import ua.gram.munhauzen.interaction.timer.TimerScenario;
import ua.gram.munhauzen.interaction.timer2.Timer2Scenario;
import ua.gram.munhauzen.interaction.wauwau.WauScenario;
import ua.gram.munhauzen.screen.DebugScreen;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.LoadingScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    private final DebugScreen screen;
    public FragmentRoot root;
    ScrollPane scroll;
    TextButton startButton;
    Label upButton;
    Table inventoryContainer, scenarioContainer, interactionContainer;
    VerticalGroup group;
    String currentSource = "scenario_1";

    public ControlsFragment(DebugScreen screen) {
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

        startButton = game.buttonBuilder.danger("Начать", new ClickListener() {
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

        PrimaryButton menuButton = game.buttonBuilder.primary("Меню", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.stopCurrentSfx();

                    screen.game.sfxService.onBackToMenuClicked();

                    screen.navigateTo(new MenuScreen(game));

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label openGalleryLbl = new Label("[+] Открыть всю галерею", new Label.LabelStyle(
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
                        if (!image.isHiddenFromGallery) {
                            game.achievementService.onImageViewed(image);
                        }
                    }

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label openFailsLbl = new Label("[+] Открыть все фейлы", new Label.LabelStyle(
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
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label openScenarioLbl = new Label("[+] Вкл перемотку вперед", new Label.LabelStyle(
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
                        game.gameState.history.visitedStories.add(s.name);
                    }
                    for (CannonsScenario s : game.databaseManager.loadCannonsScenario()) {
                        game.gameState.history.visitedStories.add(s.name);
                    }
                    for (GeneralsScenario s : game.databaseManager.loadGeneralsScenario()) {
                        game.gameState.history.visitedStories.add(s.name);
                    }
                    for (HareScenario s : game.databaseManager.loadHareScenario()) {
                        game.gameState.history.visitedStories.add(s.name);
                    }
                    for (PictureScenario s : game.databaseManager.loadPictureScenario()) {
                        game.gameState.history.visitedStories.add(s.name);
                    }
                    for (HireScenario s : game.databaseManager.loadServantsHireScenario()) {
                        game.gameState.history.visitedStories.add(s.name);
                    }
                    for (Timer2Scenario s : game.databaseManager.loadTimer2Scenario()) {
                        game.gameState.history.visitedStories.add(s.name);
                    }
                    for (TimerScenario s : game.databaseManager.loadTimerScenario()) {
                        game.gameState.history.visitedStories.add(s.name);
                    }
                    for (WauScenario s : game.databaseManager.loadWauwauScenario()) {
                        game.gameState.history.visitedStories.add(s.name);
                    }
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label removeHistoryLbl = new Label("[x] Очистить историю", new Label.LabelStyle(
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

                    for (String save : new String[]{"1", "2", "3", "4"}) {
                        ExternalFiles.getSaveFile(game.params, save).delete();
                    }

                    ExternalFiles.getHistoryFile(game.params).delete();
                    ExternalFiles.getMenuStateFile(game.params).delete();
                    ExternalFiles.getGalleryStateFile(game.params).delete();
                    ExternalFiles.getFailsStateFile(game.params).delete();

                    game.gameState.history = new History();
                    game.gameState.setActiveSave(new Save());

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label removeAllLbl = new Label("[x] Удалить ВСЕ", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        removeAllLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                removeAllLbl.setVisible(false);

                GameState.clearTimer(tag);

                if (Gdx.files.external(game.params.storageDirectory).exists()) {
                    Gdx.files.external(game.params.storageDirectory).deleteDirectory();
                }

                Gdx.app.exit();
            }
        });

        final Label expLbl = new Label("[+] Скачать обновления", new Label.LabelStyle(
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

        container.add(openGalleryLbl).pad(10).left().expandX();
        container.add(openFailsLbl).pad(10).left().expandX().row();

        container.add(expLbl).pad(10).left().expandX();
        container.add(openScenarioLbl).pad(10).left().expandX().row();

        container.add(removeHistoryLbl).pad(10).left().expandX();
        container.add(removeAllLbl).pad(10).left().expandX().row();

        inventoryContainer = new Table();
        inventoryContainer.padBottom(80);

        interactionContainer = new Table();
        interactionContainer.padBottom(80);

        scenarioContainer = new Table();
        scenarioContainer.padBottom(80);

        createInventoryTable();

        createScenarioTable();

        createInteractionTable();

        Table container2 = new Table();
        container2.add(inventoryContainer).top().expandX();
        container2.add(scenarioContainer).top().expandX();

        Table upContainer = new Table();
        upContainer.add(upButton).pad(10).align(Align.bottomRight).expand();

        group = new VerticalGroup();
        group.pad(10);
        group.addActor(container);
        group.addActor(interactionContainer);
        group.addActor(container2);

        scroll = new ScrollPane(group);
        scroll.setFillParent(true);
        scroll.setName(tag);

        root = new FragmentRoot();
        root.addContainer(scroll);
        root.addContainer(upContainer);
    }

    public void createInteractionTable() {

        interactionContainer.clearChildren();

        Label header = new Label("Фишки", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        interactionContainer.add(header).expandX().row();

        String[] interactions = {
                InteractionFactory.BALLOONS,
                InteractionFactory.CANNONS,
                InteractionFactory.CONTINUE,
                InteractionFactory.DATE,
                InteractionFactory.GENERAL,
                InteractionFactory.HARE,
                InteractionFactory.HORN,
                InteractionFactory.LIONS,
                InteractionFactory.PICTURE,
                InteractionFactory.PUZZLE,
                InteractionFactory.RANDOM,
                InteractionFactory.SERVANTS,
                InteractionFactory.SLAP,
                InteractionFactory.SWAMP,
                "TIMER(ACARRIAGE,20)",
                "TIMER_2(APRISONERS_EXPLODED,20)",
                InteractionFactory.WAUWAU,
        };

        for (final String name : interactions) {

            String text;

            if (name.equals(MunhauzenGame.developmentInteraction)) {
                text = "[+] " + name;
            } else {
                text = "[-] " + name;
            }

            Label label = new Label(text, new Label.LabelStyle(
                    game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                    Color.BLACK
            ));

            label.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    MunhauzenGame.developmentScenario = null;

                    if (name.equals(MunhauzenGame.developmentInteraction)) {
                        MunhauzenGame.developmentInteraction = null;
                    } else {
                        MunhauzenGame.developmentInteraction = name;
                    }

                    createInteractionTable();
                }
            });

            interactionContainer.add(label).left().expandX().padBottom(5).row();
        }
    }

    public void createInventoryTable() {

        inventoryContainer.clearChildren();

        Label header = new Label("Инвентарь", new Label.LabelStyle(
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

            if (inventory.isGlobal()) {
                name += " (global)";
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

                    createInventoryTable();

                }
            });

            inventoryContainer.add(label).left().expandX().padBottom(5).row();
        }
    }

    public void createScenarioTable() {

        scenarioContainer.clearChildren();

        Label header = new Label("Сценарий", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        scenarioContainer.add(header).colspan(3).expandX().row();

        Label part1 = new Label("Ч1", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        part1.setAlignment(Align.center);
        scenarioContainer.add(part1).pad(10).center().growX();

        Label part2 = new Label("Ч2", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        part2.setAlignment(Align.center);
        scenarioContainer.add(part2).pad(10).center().growX();

        Label part3 = new Label("Ч3", new Label.LabelStyle(
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

            String name = scenario.name;

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

                    MunhauzenGame.developmentInteraction = null;

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
        startButton.setDisabled(!ExternalFiles.getScenarioFile(game.params).exists());

        upButton.setVisible(scroll.getScrollY() > 0);
    }

    @Override
    public Actor getRoot() {
        return root;
    }
}
