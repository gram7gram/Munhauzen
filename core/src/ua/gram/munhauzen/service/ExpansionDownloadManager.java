package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.expansion.ExtractExpansionPartTask;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
import ua.gram.munhauzen.expansion.response.Part;
import ua.gram.munhauzen.screen.loading.fragment.ControlsFragment;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Files;
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

                cleanup();

                try {

                    Files.toFile(httpResponse.getResultAsStream(), output);

                    part.isDownloaded = true;

                    Log.i(tag, "fetchExpansionPart success part#" + part.part);

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
                        onConnectionFailed();
                    }
                });
            }

            private void cleanup() {
                output.delete();
            }

            @Override
            public void cancelled() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        onConnectionCanceled();
                    }
                });
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

        Log.i(tag, "downloaded part #" + part.part + " " + md5
                + "\n" + "original part #" + part.part + " " + part.checksum);

        if (!md5.equals(part.checksum)) {

            discardPart(part);

            throw new GdxRuntimeException("Expansion part#" + part.part + " is corrupted");
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
                fragment.progress.setText("");
                fragment.progressMessage.setText("Fetching info...");
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
                        onConnectionFailed();
                    }
                });
            }

            @Override
            public void cancelled() {

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        onConnectionCanceled();
                    }
                });

            }
        });
    }

    private void onConnectionFailed() {
        fragment.progressMessage.setText("Unable to fetch resources");
    }

    private void onConnectionCanceled() {
        fragment.progressMessage.setText("Download was canceled");
    }

    private void onLowMemory(float memory) {
        Log.i(tag, "onLowMemory");

        fragment.progressMessage.setText("Not enough memory. Please, free some space for the game");
    }

    private void onComplete() {
        Log.i(tag, "onComplete");

        fragment.progress.setText("100%");
        fragment.progressMessage.setText("Resources are loaded!");

        if (localExpansionInfo != null) {
            for (Part item : localExpansionInfo.parts.items) {
                ExternalFiles.getExpansionPartFile(item).delete();
            }
        }

        ExternalFiles.updateNomedia();

        localExpansionInfo = null;

        fragment.onExpansionDownloadComplete();
    }

    private void restoreFromFile() {

        FileHandle file = ExternalFiles.getExpansionInfoFile(game.params);
        if (!file.exists()) return;

        ExpansionResponse result = json.fromJson(ExpansionResponse.class, file.read());

        if (result != null && result.version == game.params.versionCode) {
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

//            final float sizeMb = (float) (localExpansionInfo.size / 1024f / 1024f);

            int progress = getProgress();

            String progressText = "";
            Color progressColor = Color.BLACK;
            for (Part item : localExpansionInfo.parts.items) {

                if (item.isDownloading) {
                    progressText = "Downloading part #" + item.part + "...";
                }

                if (item.isExtracting) {
                    progressText = "Extracting part #" + item.part + "...";
                }

                if (item.isDownloadFailure) {
                    progressText = "Downloading part #" + item.part + " failed";
                    progressColor = Color.RED;
                }

                if (item.isExtractFailure) {
                    progressText = "Extracting part#" + item.part + " failed";
                    progressColor = Color.RED;
                }
            }

            fragment.progress.setText(progress + "%");
            fragment.progress.setColor(progressColor);
            fragment.progressMessage.setText(progressText);
            fragment.progressMessage.setColor(progressColor);

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

        serverExpansionInfo = null;
        localExpansionInfo = null;
    }

}
