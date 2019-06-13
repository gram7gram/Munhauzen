package ua.gram.munhauzen.fragment.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.expansion.ExportResponse;
import ua.gram.munhauzen.expansion.ExtractExpansionTask;
import ua.gram.munhauzen.expansion.ExtractGameConfigTask;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class MenuFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    private final AssetManager assetManager;
    public ScrollPane root;
    TextButton downloadButton, expansionButton, startButton;
    Label progressLbl;
    Table inventoryContainer;
    VerticalGroup group;

    public MenuFragment(MunhauzenGame game) {
        this.game = game;
        assetManager = new AssetManager();
    }

    public void create() {
        startButton = game.buttonBuilder.primary("Начать", new ClickListener() {
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

        progressLbl = new Label("Свободная память: " + getMB(), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.p),
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

        expansionButton = game.buttonBuilder.primary("[Загрузить файл расширения]", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    startExpansionDownload();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        Table container = new Table();
        container.padBottom(80);
        container.add(startButton).padBottom(80).expandX().row();
        container.add(downloadButton).expandX().row();
        container.add(expansionButton).expandX().row();
        container.add(progressLbl).pad(10).expandX().row();

        createInventoryTable();

        group = new VerticalGroup();
        group.pad(10);
        group.addActor(container);
        group.addActor(inventoryContainer);

        root = new ScrollPane(group);
        root.setFillParent(true);
        root.setName(tag);
    }

    private void createInventoryTable() {

        Label header = new Label("Инвентарь", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h4),
                Color.RED
        ));
        inventoryContainer = new Table();
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
                    game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h4),
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
                            game.inventoryService.addInventory(inventory);
                        }
                    }

                    group.removeActor(inventoryContainer);

                    createInventoryTable();

                    group.addActor(inventoryContainer);

                }
            });

            inventoryContainer.add(label).left().expandX().row();
        }
    }

    public void update() {
        startButton.setDisabled(!ExternalFiles.getScenarioFile().exists());
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.clear();
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

                                    group.removeActor(inventoryContainer);

                                    createInventoryTable();

                                    group.addActor(inventoryContainer);
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

    private void startExpansionDownload() {
        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Net.HttpRequest httpRequest = requestBuilder.newRequest()
                        .method(Net.HttpMethods.GET)
                        .url(game.params.getExpansionUrl())
                        .build();

                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {

                        cleanup();

                        FileHandle output = ExternalFiles.getExpansionFile(game.params);

                        try {
                            long length = Long.parseLong(httpResponse.getHeader("Content-Length"));

                            float mb = length / 1024f / 1024f;
                            Log.i(tag, "downloading size=" + String.format("%.2f", mb) + "MB");

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    progressLbl.setText("Скачивание...");
                                }
                            });

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

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    progressLbl.setText("Распаковка...");
                                }
                            });

                            new ExtractExpansionTask(game).extract();

                            Log.i(tag, "extracted");

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    progressLbl.setText("Файл расширения готов к использованию");
                                }
                            });

                            output.delete();

                        } catch (Throwable e) {
                            Log.e(tag, e);

                            cleanup();

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    progressLbl.setText("Распаковка неудачна");
                                }
                            });
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

                    private void cleanup() {

                        ExternalFiles.getExpansionImagesDir().deleteDirectory();

                        ExternalFiles.getExpansionAudioDir().deleteDirectory();

                        ExternalFiles.getExpansionFile(game.params).delete();
                    }

                    @Override
                    public void cancelled() {

                    }
                });
            }
        });
    }

    private String getMB() {
        return String.format("%.2f", game.params.memoryUsage.megabytesAvailable()) + "МБ";
    }
}
