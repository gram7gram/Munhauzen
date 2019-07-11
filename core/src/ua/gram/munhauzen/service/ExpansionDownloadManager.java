package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.InputStream;
import java.io.OutputStream;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.expansion.ExtractExpansionPartTask;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
import ua.gram.munhauzen.expansion.response.Part;
import ua.gram.munhauzen.fragment.mainmenu.MenuFragment;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

public class ExpansionDownloadManager {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    final MenuFragment fragment;
    ExpansionResponse expansionResponse;

    public ExpansionDownloadManager(MunhauzenGame game, MenuFragment fragment) {
        this.game = game;
        this.fragment = fragment;
    }

    public void start() {

        restoreFromFile();

        if (expansionResponse == null) {
            fetchExpansionInfo();
        } else {
            processAvailablePart();
        }
    }

    private void processAvailablePart() {
        Part nextPart = null;
        for (Part item : expansionResponse.parts.items) {
            if (item.isDownloaded && !item.isExtracted) {
                nextPart = item;
                break;
            } else if (!item.isDownloaded) {
                nextPart = item;
                break;
            }
        }

        if (nextPart != null) {
            if (nextPart.isDownloaded) {
                extractPart(nextPart);
            } else {
                fetchExpansionPart(nextPart);
            }
        } else {
            onComplete();
        }
    }

    public void fetchExpansionPart(final Part part) {
        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        final float partSize = (float) (part.size / 1024f / 1024f);

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                fragment.progressLbl.setText("Скачивание part #" + part.part + " " + String.format("%.2f", partSize) + "MB");
            }
        });

        final FileHandle output = ExternalFiles.getExpansionPartFile(part);

        Net.HttpRequest httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(part.url)
                .timeout(60000)
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                cleanup();

                try {

                    InputStream is = httpResponse.getResultAsStream();

                    OutputStream os = output.write(false);

                    byte[] bytes = new byte[1024];
                    int count;
                    while ((count = is.read(bytes, 0, bytes.length)) != -1) {
                        os.write(bytes, 0, count);
                    }

                    part.isDownloaded = true;

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            onProgressChanged();
                        }
                    });

                } catch (Throwable e) {

                    cleanup();

                    failed(e);
                }

                extractPart(part);
            }

            @Override
            public void failed(Throwable t) {
                Log.e(tag, t);

                part.isDownloaded = false;

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        fragment.progressLbl.setText("Скачивание part #" + part.part + " неудачно");
                        cancel();
                    }
                });
            }

            private void cleanup() {
                Log.i(tag, "Clean up part #" + part.part);

                output.delete();
            }

            @Override
            public void cancelled() {

            }
        });
    }

    public void extractPart(final Part part) {
        try {

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    fragment.progressLbl.setText("Распаковка part #" + part.part);
                }
            });

            new ExtractExpansionPartTask(game).extract(part);

            part.isExtracted = true;

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    onProgressChanged();
                }
            });

            ExternalFiles.getExpansionPartFile(part).delete();

            processAvailablePart();

        } catch (Throwable e) {

            Log.e(tag, e);

            part.isExtracted = false;
            part.isDownloaded = false;

            ExternalFiles.getExpansionPartFile(part).delete();

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    onProgressChanged();
                }
            });

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    fragment.progressLbl.setText("Распаковка part #" + part.part + " неудачна");
                    cancel();
                }
            });
        }
    }

    public void fetchExpansionInfo() {
        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                fragment.expansionLbl.setText("");
                fragment.progressLbl.setText("Получение информации...");
            }
        });

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

                        try {
                            Json json = new Json(JsonWriter.OutputType.json);
                            json.setIgnoreDeprecated(true);

                            expansionResponse = json.fromJson(ExpansionResponse.class, httpResponse.getResultAsString());
                            final float sizeMb = (float) (expansionResponse.size / 1024f / 1024f);

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    onProgressChanged();
                                }
                            });

                            float memory = game.params.memoryUsage.megabytesAvailable();
                            if (memory > 0) {
                                if (sizeMb > memory) {
                                    Gdx.app.postRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            onLowMemory();
                                        }
                                    });
                                    return;
                                }
                            }

                            processAvailablePart();

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
                                fragment.progressLbl.setText("Информация о файле расширения не доступна");
                                cancel();
                            }
                        });
                    }

                    @Override
                    public void cancelled() {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                cancel();
                            }
                        });
                    }
                });
            }
        });
    }

    private void onLowMemory() {
        cancel();

        fragment.progressLbl.setText("Недостаточно памяти для файла расширения");
    }

    private void onComplete() {
        cancel();

        fragment.progressLbl.setText("Файл расширения готов к использованию");

        for (Part item : expansionResponse.parts.items) {
            ExternalFiles.getExpansionPartFile(item).delete();
        }

        ExternalFiles.updateNomedia();
    }

    public void cancel() {
        fragment.expansionButton.setDisabled(false);
        fragment.expansionInfoLbl.setText("");
    }

    private void restoreFromFile() {

        FileHandle file = ExternalFiles.getExpansionInfoFile(game.params);
        if (!file.exists()) return;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreDeprecated(true);

        ExpansionResponse result = json.fromJson(ExpansionResponse.class, file.read());

        if (result.version == game.params.versionCode) {
            expansionResponse = result;

            onProgressChanged();
        }

    }

    private void onProgressChanged() {
        if (expansionResponse == null) return;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreDeprecated(true);

        ExternalFiles.getExpansionInfoFile(game.params)
                .writeString(json.toJson(expansionResponse), false);

        final float sizeMb = (float) (expansionResponse.size / 1024f / 1024f);

        fragment.expansionLbl.setText("Файл расширения v" + expansionResponse.version
                + " " + String.format("%.2f", sizeMb) + "MB [" + getProgress() + "%]");

        String progress = "";
        for (Part item : expansionResponse.parts.items) {
            progress += "#" + item.part + " [";
            progress += item.isDownloaded ? "------" : "      ";
            progress += item.isExtracted ? "------" : "      ";
            progress += "]\n";
        }

        fragment.expansionInfoLbl.setText(progress);
    }


    private int getProgress() {
        if (expansionResponse == null) return 0;

        float downloadedCount = 0, extractedCount = 0;

        for (Part item : expansionResponse.parts.items) {
            if (item.isDownloaded) ++downloadedCount;
            if (item.isExtracted) ++extractedCount;
        }

        return (int) ((downloadedCount / expansionResponse.parts.count + extractedCount / expansionResponse.parts.count) / 2 * 100);
    }
}
