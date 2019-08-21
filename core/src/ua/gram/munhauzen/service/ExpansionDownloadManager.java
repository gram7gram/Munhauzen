package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.InputStream;
import java.io.OutputStream;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.expansion.ExtractExpansionPartTask;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
import ua.gram.munhauzen.expansion.response.Part;
import ua.gram.munhauzen.screen.debug.fragment.ControlsFragment;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MD5;

public class ExpansionDownloadManager implements Disposable {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    final ControlsFragment fragment;
    ExpansionResponse localExpansionInfo, serverExpansionInfo;
    Net.HttpRequest httpRequest;

    final Json json;

    public ExpansionDownloadManager(MunhauzenGame game, ControlsFragment fragment) {
        this.game = game;
        this.fragment = fragment;

        json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
    }

    public void start() {
        Log.i(tag, "start");

        restoreFromFile();

        fetchExpansionInfo();
    }

    private void processAvailablePart() {
        Part nextPart = null;
        for (Part item : localExpansionInfo.parts.items) {
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
        Log.i(tag, "fetchExpansionPart part#" + part.part + " " + part.url);

        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        final FileHandle output = ExternalFiles.getExpansionPartFile(part);

        part.isDownloading = true;

        httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(part.url)
                .timeout(10000)
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Log.i(tag, "fetchExpansionPart success part#" + part.part);

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

                } catch (Throwable e) {

                    cleanup();

                    failed(e);
                    return;
                }

                extractPart(part);
            }

            @Override
            public void failed(Throwable t) {
                Log.e(tag, t);

                part.isDownloaded = false;
                part.isDownloadFailure = true;

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        cancel();
                    }
                });
            }

            private void cleanup() {
                output.delete();
            }

            @Override
            public void cancelled() {

            }
        });
    }

    public void extractPart(final Part part) {
        Log.i(tag, "extractPart part#" + part.part);

        try {

            part.isExtracting = true;

            validateDownloadedPart(part);

            new ExtractExpansionPartTask(game).extract(part);

            part.isExtracted = true;

            ExternalFiles.getExpansionPartFile(part).delete();

            processAvailablePart();

        } catch (Throwable e) {

            Log.e(tag, e);

            discardPart(part);

            part.isExtracted = false;
            part.isExtractFailure = true;
        }
    }

    public void validateDownloadedPart(Part part) {
        Log.i(tag, "validateDownloadedPart part#" + part.part);

        FileHandle expansionFile = ExternalFiles.getExpansionPartFile(part);

        String md5 = MD5.get(expansionFile);

        Log.i(tag, "downloaded part #" + part.part + " " + md5);
        Log.i(tag, "original part #" + part.part + " " + part.checksum);

        if (!md5.equals(part.checksum)) {

            discardPart(part);

            throw new GdxRuntimeException("Expansion corrupted");
        }

    }

    private void discardPart(Part part) {
        part.isDownloaded = false;
        part.isExtracted = false;

        ExternalFiles.getExpansionPartFile(part).delete();
    }

    public void fetchExpansionInfo() {
        Log.i(tag, "fetchExpansionInfo");

        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                fragment.expansionLbl.setText("");
                fragment.expansionInfoLbl.setText("");
                fragment.progressLbl.setText("Получение информации...");
            }
        });

        httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(game.params.getExpansionUrl())
                .timeout(10000)
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Log.i(tag, "fetchExpansionInfo success");

                try {

                    String response = httpResponse.getResultAsString();

                    Log.e(tag, response);

                    serverExpansionInfo = json.fromJson(ExpansionResponse.class, response);
                    if (localExpansionInfo != null) {

                        for (Part serverPart : serverExpansionInfo.parts.items) {
                            for (Part localPart : localExpansionInfo.parts.items) {
                                if (localPart.part == serverPart.part && localPart.checksum.equals(serverPart.checksum)) {

                                    serverPart.isDownloaded = localPart.isDownloaded;
                                    serverPart.isExtracted = localPart.isExtracted;

                                    if (serverPart.isDownloaded && !serverPart.isExtracted) {
                                        if (!ExternalFiles.getExpansionPartFile(serverPart).exists()) {
                                            serverPart.isDownloaded = false;
                                        }
                                    }

                                    break;
                                }

                            }
                        }

                    } else {
                        ExternalFiles.getExpansionDir().deleteDirectory();

                        final float sizeMb = (float) (serverExpansionInfo.size / 1024f / 1024f);

                        final float memory = game.params.memoryUsage.megabytesAvailable();
                        if (memory > 0) {
                            if (sizeMb > memory) {
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        onLowMemory(memory);
                                    }
                                });
                                return;
                            }
                        }
                    }

                    localExpansionInfo = serverExpansionInfo;

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
            }
        });
    }

    private void onLowMemory(float memory) {
        Log.i(tag, "onLowMemory");

        fragment.progressLbl.setText("Недостаточно памяти для файла расширения (доступно " + memory + "mb)");
        cancel();
    }

    private void onComplete() {
        Log.i(tag, "onComplete");
        cancel();

        fragment.expansionLbl.setText("");
        fragment.expansionInfoLbl.setText("");
        fragment.progressLbl.setText("Файл расширения готов к использованию");

        if (localExpansionInfo != null) {
            for (Part item : localExpansionInfo.parts.items) {
                ExternalFiles.getExpansionPartFile(item).delete();
            }
        }

        ExternalFiles.updateNomedia();

        localExpansionInfo = null;
    }

    public void cancel() {
        fragment.expansionInfoLbl.setText("");
        localExpansionInfo = null;
    }

    private void restoreFromFile() {

        FileHandle file = ExternalFiles.getExpansionInfoFile(game.params);
        if (!file.exists()) return;

        ExpansionResponse result = json.fromJson(ExpansionResponse.class, file.read());

        if (result.version == game.params.versionCode) {
            localExpansionInfo = result;
        }

    }

    private int getProgress() {
        if (localExpansionInfo == null) return 0;

        float downloadedCount = 0, extractedCount = 0;

        for (Part item : localExpansionInfo.parts.items) {
            if (item.isDownloaded) ++downloadedCount;
            if (item.isExtracted) ++extractedCount;
        }

        return (int) ((downloadedCount / localExpansionInfo.parts.count + extractedCount / localExpansionInfo.parts.count) / 2 * 100);
    }

    public void updateProgress() {
        if (localExpansionInfo == null) return;

        try {

            final float sizeMb = (float) (localExpansionInfo.size / 1024f / 1024f);

            int progress = getProgress();

            fragment.expansionLbl.setText("Файл расширения v" + localExpansionInfo.version
                    + " " + String.format("%.2f", sizeMb) + "MB [" + progress + "%]");

            String text = "";
            String progressText = "";
            for (Part item : localExpansionInfo.parts.items) {

                if (item.isDownloading) {
                    progressText = "Скачивание part #" + item.part + "...";
                }

                if (item.isExtracting) {
                    progressText = "Распаковка part #" + item.part + "...";
                }

                if (item.isDownloadFailure) {
                    progressText = "Скачивание part #" + item.part + " неудачно";
                }

                if (item.isExtractFailure) {
                    progressText = "Распаковка part #" + item.part + " неудачна";
                }

                text += "#" + item.part + " [";

                if (item.isDownloaded) {
                    text += "------";
                } else if (item.isDownloading) {
                    text += "-     ";
                } else {
                    text += "      ";
                }

                if (item.isExtracted) {
                    text += "------";
                } else if (item.isExtracting) {
                    text += "-     ";
                } else {
                    text += "      ";
                }

                text += "]\n";
            }

            fragment.expansionInfoLbl.setText(text);
            fragment.progressLbl.setText(progressText);

            ExternalFiles.getExpansionInfoFile(game.params)
                    .writeString(json.toJson(localExpansionInfo), false);

            if (progress == 100) {
                onComplete();
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void dispose() {
        if (httpRequest != null) {
            Gdx.net.cancelHttpRequest(httpRequest);
            httpRequest = null;
        }

    }

}
