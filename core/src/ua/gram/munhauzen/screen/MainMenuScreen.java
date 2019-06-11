package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.InputStream;
import java.io.OutputStream;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.expansion.ExportResponse;
import ua.gram.munhauzen.expansion.ExtractGameConfigTask;
import ua.gram.munhauzen.service.DatabaseManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class MainMenuScreen implements Screen {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    private Texture background;
    private MunhauzenStage ui;
    TextButton downloadButton, useButton;
    Label progressLbl;

    public MainMenuScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        ui = new MunhauzenStage(game);

        background = game.assetManager.get("a0.jpg", Texture.class);

        TextButton startButton = game.buttonBuilder.primary("Начать", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        progressLbl = new Label("", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.p),
                Color.BLUE
        ));
        progressLbl.setVisible(false);

        downloadButton = game.buttonBuilder.primary("Скачать json", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                startDownload();
            }
        });

        useButton = game.buttonBuilder.primary("Загрузить json", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                new DatabaseManager().loadExternal(game.gameState);

                progressLbl.setVisible(true);
                progressLbl.setText("Конфиги загружены!");
            }
        });

        Table container = new Table();
        container.setFillParent(true);
        container.pad(10);
        container.add(startButton).width(400).expandX().row();
        container.add(downloadButton).width(400).expandX().row();
        container.add(useButton).width(400).expandX().row();
        container.add(progressLbl).pad(10).expandX();

        ui.addActor(container);

        Gdx.input.setInputProcessor(ui);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        game.batch.begin();
        game.batch.disableBlending();

        game.batch.draw(background,
                0, 0, //position
                MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT //width
        );

        game.batch.enableBlending();
        game.batch.end();

        ui.act(delta);
        ui.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        MunhauzenGame.pauseGame();
    }

    @Override
    public void resume() {
        MunhauzenGame.resumeGame();
    }

    @Override
    public void dispose() {
        if (ui != null) {
            ui.dispose();
        }
    }

    private void startDownload() {
        downloadButton.setDisabled(true);
        progressLbl.setVisible(true);

        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        Net.HttpRequest httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(game.params.gameConfigPath)
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                Json json = new Json(JsonWriter.OutputType.json);
                json.setIgnoreUnknownFields(true);

                ExportResponse response = json.fromJson(ExportResponse.class, httpResponse.getResultAsString());

                Net.HttpRequest httpRequest = requestBuilder.newRequest()
                        .method(Net.HttpMethods.GET)
                        .url(response.url)
                        .build();

                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {

                        long length = Long.parseLong(httpResponse.getHeader("Content-Length"));

                        // We're going to download the file to external storage, create the streams
                        InputStream is = httpResponse.getResultAsStream();
                        FileHandle output = ExternalFiles.getGameArchiveFile();

                        OutputStream os = output.write(false);

                        byte[] bytes = new byte[1024];
                        int count;
                        long read = 0;
                        try {
                            // Keep reading bytes and storing them until there are no more.
                            while ((count = is.read(bytes, 0, bytes.length)) != -1) {
                                os.write(bytes, 0, count);
                                read += count;

                                // Update the UI with the download progress
                                final int progress = ((int) (((double) read / (double) length) * 100));

                                if (progress % 5 == 0) {

                                    Log.i(tag, "progress=" + progress);

                                    // Since we are downloading on a background thread, post a runnable to touch ui
                                    Gdx.app.postRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (progress == 100) {
                                                downloadButton.setDisabled(false);
                                                progressLbl.setText("Скачивание успешно!");
                                            } else {
                                                progressLbl.setText("Скачивание " + progress + "%");
                                            }
                                        }
                                    });
                                }
                            }

                            new ExtractGameConfigTask().extract();

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    progressLbl.setText("Конфиги готовы к использованию");
                                }
                            });

                        } catch (Throwable e) {
                            failed(e);
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
                        progressLbl.setText("Экспорт неудачен");
                    }
                });
            }

            @Override
            public void cancelled() {

            }
        });

    }
}
