package ua.gram.munhauzen.screen.debug.fragment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.expansion.ExportResponse;
import ua.gram.munhauzen.expansion.ExtractGameConfigTask;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.service.ExpansionDownloadManager;
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
    public FragmentRoot root;
    ScrollPane scroll;
    TextButton downloadButton;
    public TextButton expansionButton;
    TextButton startButton;
    public Label progressLbl, expansionLbl, expansionInfoLbl;
    Label upButton;
    Table inventoryContainer, scenarioContainer;
    VerticalGroup group;
    ExpansionDownloadManager downloader;
    String currentSource = "scenario_1";

    public ControlsFragment(MunhauzenGame game) {
        this.game = game;
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

        startButton = game.buttonBuilder.primary("Start", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    game.setScreen(new GameScreen(game));
                    dispose();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        PrimaryButton menuButton = game.buttonBuilder.primary("Menu", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    game.setScreen(new MenuScreen(game));
                    dispose();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        final Label removeCacheLbl = new Label("[Очистить кеш]", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.RED
        ));
        removeCacheLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                ExternalFiles.getExpansionInfoFile(game.params).delete();
                removeCacheLbl.setVisible(false);
            }
        });
        removeCacheLbl.setVisible(
                ExternalFiles.getExpansionInfoFile(game.params).exists()
        );

        progressLbl = new Label("", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLUE
        ));

        expansionLbl = new Label("", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLUE
        ));

        expansionInfoLbl = new Label("", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLUE
        ));

        downloadButton = game.buttonBuilder.primary("[Скачать json]", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    startDownload();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        expansionButton = game.buttonBuilder.primary("[Скачать файл расш.]", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                expansionButton.setDisabled(true);

                try {
                    downloader = new ExpansionDownloadManager(game, ControlsFragment.this);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            downloader.start();
                        }
                    }).start();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        Table container = new Table();
        container.padBottom(80);
        container.add(startButton)
                .width(MunhauzenGame.WORLD_WIDTH * .5f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f)
                .expandX().row();
        container.add(menuButton)
                .width(MunhauzenGame.WORLD_WIDTH * .5f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f)
                .padBottom(80).expandX().row();
        container.add(downloadButton)
                .width(MunhauzenGame.WORLD_WIDTH * .5f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f)
                .expandX().row();
        container.add(expansionButton)
                .width(MunhauzenGame.WORLD_WIDTH * .5f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f)
                .expandX().row();
        container.add(removeCacheLbl).pad(10).expandX().row();

        container.add(expansionLbl).expandX().row();
        container.add(expansionInfoLbl).expandX().row();
        container.add(progressLbl).expandX().row();

        inventoryContainer = new Table();
        inventoryContainer.padBottom(80);

        scenarioContainer = new Table();
        scenarioContainer.padBottom(80);

        createInventoryTable();

        createScenarioTable();

        Table container2 = new Table();
        container2.add(inventoryContainer).top().expandX();
        container2.add(scenarioContainer).top().expandX();

        Table container3 = new Table();
        container3.add(upButton).pad(10).align(Align.bottomRight).expand();

        group = new VerticalGroup();
        group.pad(10);
        group.addActor(container);
        group.addActor(container2);

        scroll = new ScrollPane(group);
        scroll.setFillParent(true);
        scroll.setName(tag);

        root = new FragmentRoot();
        root.addContainer(scroll);
        root.addContainer(container3);
    }

    private void createInventoryTable() {

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

                        if (inventory.isGlobal()) {
                            game.inventoryService.addGlobalInventory(inventory);
                        } else {
                            game.inventoryService.addSaveInventory(inventory);
                        }
                    }

                    createInventoryTable();

                }
            });

            inventoryContainer.add(label).left().expandX().row();
        }
    }

    private void createScenarioTable() {

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

                    if (scenario.name.equals(MunhauzenGame.developmentScenario)) {
                        MunhauzenGame.developmentScenario = null;
                    } else {
                        MunhauzenGame.developmentScenario = scenario.name;
                    }

                    createScenarioTable();

                }
            });

            scenarioContainer.add(label).colspan(3).left().expandX().row();
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
        startButton.setDisabled(!ExternalFiles.getScenarioFile().exists());

        upButton.setVisible(scroll.getScrollY() > 0);

        if (downloader != null) {
            downloader.updateProgress();
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    private void startDownload() {
        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        Net.HttpRequest httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(game.params.getGameExportUrl())
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                Json json = new Json(JsonWriter.OutputType.json);
                json.setIgnoreUnknownFields(true);

                ExportResponse response = json.fromJson(ExportResponse.class, httpResponse.getResultAsString());

                Net.HttpRequest fileRequest = requestBuilder.newRequest()
                        .method(Net.HttpMethods.GET)
                        .url(response.url)
                        .build();

                Gdx.net.sendHttpRequest(fileRequest, new Net.HttpResponseListener() {

                    private void cleanup() {
                        ExternalFiles.getGameArchiveFile().delete();
                    }

                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {

                        cleanup();

                        FileHandle output = ExternalFiles.getGameArchiveFile();

                        try {
                            long length = Long.parseLong(httpResponse.getHeader("Content-Length"));

                            float mb = length / 1024f / 1024f;
                            Log.i(tag, "downloading size=" + String.format("%.2f", mb) + "MB");

                            InputStream is = httpResponse.getResultAsStream();

                            OutputStream os = output.write(false);

                            byte[] bytes = new byte[1024];
                            int count;
                            while ((count = is.read(bytes, 0, bytes.length)) != -1) {
                                os.write(bytes, 0, count);
                            }

                            Log.i(tag, "downloaded");

                        } catch (Throwable e) {

                            cleanup();

                            failed(e);

                            return;
                        }

                        try {

                            Log.i(tag, "extracting");

                            new ExtractGameConfigTask().extract();

                            Log.i(tag, "extracted");

                            game.databaseManager.loadExternal(game.gameState);

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    progressLbl.setText("Конфиги готовы к использованию");

                                    createInventoryTable();

                                    createScenarioTable();
                                }
                            });

                            output.delete();

                        } catch (Throwable e) {
                            Log.e(tag, e);

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    progressLbl.setText("Невозможно использовать конфиги");
                                }
                            });

                            cleanup();

                        }
                    }

                    @Override
                    public void failed(Throwable t) {
                        Log.e(tag, t);

                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                progressLbl.setText("Скачивание неудачно");
                            }
                        });
                    }

                    @Override
                    public void cancelled() {

                    }
                });
            }

            @Override
            public void failed(Throwable t) {
                Log.e(tag, t);

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        progressLbl.setText("Скачивание неудачно");
                    }
                });
            }

            @Override
            public void cancelled() {

            }
        });

    }

    @Override
    public void dispose() {
        super.dispose();

        if (downloader != null) {
            downloader.dispose();
            downloader = null;
        }
    }
}
