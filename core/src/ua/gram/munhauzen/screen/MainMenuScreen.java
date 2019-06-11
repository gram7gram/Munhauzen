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
import ua.gram.munhauzen.expansion.ExtractExpansionTask;
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
    TextButton downloadButton, useButton, expansionButton;
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

        downloadButton = game.buttonBuilder.primary("Скачать json", new ClickListener() {
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

        useButton = game.buttonBuilder.primary("Использовать json", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    new DatabaseManager().loadExternal(game.gameState);
                    progressLbl.setText("Конфиги загружены!");
                } catch (Throwable e) {
                    Log.e(tag, e);
                    progressLbl.setText("Ошибка при загрузке конфига");
                }
            }
        });

        expansionButton = game.buttonBuilder.primary("Загрузить файл расширения", new ClickListener() {
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
        container.setFillParent(true);
        container.pad(10);
        container.add(startButton).expandX().row();
        container.add(useButton).expandX().padBottom(30).row();
        container.add(downloadButton).expandX().row();
        container.add(expansionButton).expandX().row();
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

                        FileHandle output = ExternalFiles.getGameArchiveFile();

                        try {
                            long length = Long.parseLong(httpResponse.getHeader("Content-Length"));

                            InputStream is = httpResponse.getResultAsStream();

                            output.delete();

                            OutputStream os = output.write(false);

                            byte[] bytes = new byte[1024];
                            int count;
                            long read = 0;
                            while ((count = is.read(bytes, 0, bytes.length)) != -1) {
                                os.write(bytes, 0, count);
                                read += count;

                                final int progress = ((int) (((double) read / (double) length) * 100));

                                if (progress % 5 == 0) {
                                    Gdx.app.postRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (progress == 100) {
                                                progressLbl.setText("Скачивание успешно! Распаковка...");
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

                            output.delete();

                        } catch (Throwable e) {

                            output.delete();

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

    private void startExpansionDownload() {
        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        Net.HttpRequest httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(game.params.getExpansionUrl())
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                FileHandle output = ExternalFiles.getExpansionFile(game.params);

                try {
                    ExternalFiles.getExpansionImagesDir().delete();

                    ExternalFiles.getExpansionAudioDir().delete();

                    long length = Long.parseLong(httpResponse.getHeader("Content-Length"));

                    InputStream is = httpResponse.getResultAsStream();

                    output.delete();

                    OutputStream os = output.write(false);

                    byte[] bytes = new byte[1024];
                    int count;
                    long read = 0;
                    while ((count = is.read(bytes, 0, bytes.length)) != -1) {
                        os.write(bytes, 0, count);
                        read += count;

                        final int progress = ((int) (((double) read / (double) length) * 100));

                        if (progress % 5 == 0) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    if (progress == 100) {
                                        progressLbl.setText("Скачивание успешно! Распаковка...");
                                    } else {
                                        progressLbl.setText("Скачивание " + progress + "% (своб.:" + getMB() + ")");
                                    }
                                }
                            });
                        }

                    }

                    new ExtractExpansionTask(game).extract();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            progressLbl.setText("Файл расширения готов к использованию");
                        }
                    });

                    output.delete();

                } catch (Throwable e) {

                    output.delete();

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

    private String getMB() {
        return String.format("%.2f", game.params.memoryUsage.megabytesAvailable()) + "МБ";
    }
}
