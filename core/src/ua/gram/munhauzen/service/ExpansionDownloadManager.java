package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
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

        fetchExpansionInfo();
    }

    private void processAvailablePart() {
        Part nextPart = null;
        for (Part item : game.gameState.expansionInfo.parts.items) {

            if (item.isCompleted) continue;

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

        final FileHandle output = ExternalFiles.getExpansionPartFile(game.params, part);

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
                    int code = httpResponse.getStatus().getStatusCode();

                    Log.e(tag, "fetchExpansionPart: " + code);

                    if (code != HttpStatus.SC_OK) {
                        throw new GdxRuntimeException("Bad request");
                    }

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

            ExternalFiles.getExpansionPartFile(game.params, part).delete();

            processAvailablePart();

        } catch (Throwable e) {
            Log.e(tag, e);

            onExtractionFailed(part);
        }
    }

    public void validateDownloadedPart(Part part) {
        Log.i(tag, "validateDownloadedPart part#" + part.part);

        FileHandle expansionFile = ExternalFiles.getExpansionPartFile(game.params, part);
        if (!expansionFile.exists()) {
            throw new GdxRuntimeException("Expansion part#" + part.part + " was not downloaded");
        }

        String md5 = MD5.get(expansionFile);

        Log.i(tag, "downloaded part #" + part.part + " " + md5
                + "\n" + "original part #" + part.part + " " + part.checksum);

        if (!MunhauzenGame.CAN_SKIP_EXPANSION_VALIDATION) {
            if (!md5.equals(part.checksum)) {

                discardPart(part);

                throw new GdxRuntimeException("Expansion part#" + part.part + " is corrupted");
            }
        }

    }

    private void discardPart(Part part) {
        part.isDownloaded = false;
        part.isExtracted = false;

        ExternalFiles.getExpansionPartFile(game.params, part).delete();
    }

    public void fetchExpansionInfo() {
        Log.i(tag, "fetchExpansionInfo");

        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                fragment.progress.setText("");
                fragment.progressMessage.setText(game.t("expansion_download.started"));
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

                try {

                    String response = httpResponse.getResultAsString();

                    int code = httpResponse.getStatus().getStatusCode();

                    Log.e(tag, "fetchExpansionInfo:\n" + code + ": " + response);

                    if (code != HttpStatus.SC_OK) {
                        throw new GdxRuntimeException("Bad request");
                    }

                    ExpansionResponse serverExpansionInfo = game.databaseManager.loadExpansionInfo(response);
                    if (serverExpansionInfo == null) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onBadResponse();
                            }
                        });
                        return;
                    }

                    ExpansionResponse expansionInfo = game.gameState.expansionInfo;

                    if (expansionInfo != null && expansionInfo.isSameExpansion(serverExpansionInfo)) {

                        Log.i(tag, "Same expansion. Resuming download...");

                        String log = "Expansion state:";

                        for (Part serverPart : serverExpansionInfo.parts.items) {

                            serverPart.isDownloaded = false;
                            serverPart.isDownloadFailure = false;
                            serverPart.isDownloading = false;

                            serverPart.isExtracting = false;
                            serverPart.isExtracted = false;
                            serverPart.isExtractFailure = false;

                            for (Part localPart : expansionInfo.parts.items) {
                                if (localPart.part == serverPart.part && localPart.checksum.equals(serverPart.checksum)) {

                                    serverPart.isDownloaded = localPart.isDownloaded;
                                    serverPart.isExtracted = localPart.isExtracted;

                                    if (serverPart.isDownloaded && !serverPart.isExtracted) {
                                        if (!ExternalFiles.getExpansionPartFile(game.params, serverPart).exists()) {
                                            serverPart.isDownloaded = false;
                                        }
                                    }

                                    break;
                                }
                            }

                            serverPart.isCompleted = serverPart.isDownloaded && serverPart.isExtracted;

                            log += "\npart " + serverPart.part
                                    + " isCompleted " + (serverPart.isCompleted ? "+" : "-")
                                    + " isDownloaded " + (serverPart.isDownloaded ? "+" : "-")
                                    + " isExtracted " + (serverPart.isExtracted ? "+" : "-");

                        }

                        Log.i(tag, log);

                    } else {

                        Log.e(tag, "New expansion. Removing previous expansion and starting download...");

                        if (MunhauzenGame.CAN_REMOVE_PREVIOUS_EXPANSION)
                            ExternalFiles.getExpansionDir(game.params).deleteDirectory();

                        final float sizeMb = (float) (serverExpansionInfo.size / 1024f / 1024f);

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
                    }

                    game.gameState.expansionInfo = serverExpansionInfo;

                    game.gameState.expansionInfo.isDownloadStarted = true;

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

    private void onBadResponse() {
        Log.e(tag, "onBadResponse");

        ExpansionResponse expansionInfo = game.gameState.expansionInfo;
        expansionInfo.isDownloadStarted = false;

        fragment.progress.setText(expansionInfo.progress + "%");
        fragment.progressMessage.setText(game.t("expansion_download.not_found"));
        fragment.retryBtn.setVisible(true);

        fragment.screen.expansionDownloader.dispose();
        fragment.screen.expansionDownloader = null;

    }

    private void onConnectionFailed() {
        Log.e(tag, "onConnectionFailed");

        ExpansionResponse expansionInfo = game.gameState.expansionInfo;
        if (expansionInfo != null)
            expansionInfo.isDownloadStarted = false;

        fragment.progress.setText("");
        fragment.progressMessage.setText(game.t("expansion_download.failed"));
        fragment.retryBtn.setVisible(true);

        fragment.screen.expansionDownloader.dispose();
        fragment.screen.expansionDownloader = null;
    }

    private void onExtractionFailed(Part part) {
        Log.e(tag, "onExtractionFailed");

        ExpansionResponse expansionInfo = game.gameState.expansionInfo;
        if (expansionInfo != null)
            expansionInfo.isDownloadStarted = false;

        discardPart(part);

        part.isExtracted = false;
        part.isExtractFailure = true;

        fragment.progress.setText("");
        fragment.progressMessage.setText(game.t("expansion_download.extract_failed") + part.part);
        fragment.retryBtn.setVisible(true);

        if (fragment.screen.expansionDownloader != null) {
            fragment.screen.expansionDownloader.dispose();
            fragment.screen.expansionDownloader = null;
        }
    }

    private void onConnectionCanceled() {
        ExpansionResponse expansionInfo = game.gameState.expansionInfo;
        if (expansionInfo == null || expansionInfo.isCompleted) return;

        Log.e(tag, "onConnectionCanceled");

        fragment.progress.setText("");
        fragment.progressMessage.setText(game.t("expansion_download.canceled"));
        fragment.retryBtn.setVisible(true);

        if (fragment.screen.expansionDownloader != null) {
            fragment.screen.expansionDownloader.dispose();
            fragment.screen.expansionDownloader = null;
        }
    }

    private void onLowMemory() {
        Log.i(tag, "onLowMemory");

        ExpansionResponse expansionInfo = game.gameState.expansionInfo;
        if (expansionInfo != null)
            expansionInfo.isDownloadStarted = false;

        fragment.progressMessage.setText(game.t("expansion_download.low_memory"));
        fragment.retryBtn.setVisible(true);

        fragment.screen.expansionDownloader.dispose();
        fragment.screen.expansionDownloader = null;
    }

    private void onComplete() {
        ExpansionResponse expansionInfo = game.gameState.expansionInfo;

        if (expansionInfo == null) return;
        if (expansionInfo.isCompleted) return;

        Log.i(tag, "onComplete");

        expansionInfo.isCompleted = true;

        game.databaseManager.persistSync(game.gameState);

        fragment.progress.setText("100%");
        fragment.progressMessage.setText(game.t("expansion_download.completed"));

        for (Part item : expansionInfo.parts.items) {
            ExternalFiles.getExpansionPartFile(game.params, item).delete();
        }

        ExternalFiles.updateNomedia(game.params);

        fragment.onExpansionDownloadComplete();
    }

    private int getProgress() {
        ExpansionResponse expansionInfo = game.gameState.expansionInfo;

        if (expansionInfo == null) return 0;

        float downloadedCount = 0, extractedCount = 0;

        for (Part item : expansionInfo.parts.items) {
            if (item.isDownloaded) ++downloadedCount;
            if (item.isExtracted) ++extractedCount;
        }

        return (int) ((downloadedCount / expansionInfo.parts.count + extractedCount / expansionInfo.parts.count) / 2 * 100);
    }

    public void updateProgress() {
        ExpansionResponse expansionInfo = game.gameState.expansionInfo;

        if (expansionInfo == null) return;

        if (!expansionInfo.isDownloadStarted) return;

        try {

            expansionInfo.progress = getProgress();

            boolean areAllCompleted = true;

            String progressText = "";
            for (Part item : expansionInfo.parts.items) {

                if (item.isDownloading) {
                    progressText = game.t("expansion_download.downloading_part")
                            .replace("__NUM__", item.part + "")
                            .replace("__TOTAL__", expansionInfo.parts.count + "");
                }

                if (item.isExtracting) {
                    progressText = game.t("expansion_download.extracting_part")
                            .replace("__NUM__", item.part + "")
                            .replace("__TOTAL__", expansionInfo.parts.count + "");
                }

                if (item.isDownloadFailure) {
                    progressText = game.t("expansion_download.downloading_part_failed")
                            .replace("__NUM__", item.part + "");
                }

                if (item.isExtractFailure) {
                    progressText = game.t("expansion_download.extracting_part_failed")
                            .replace("__NUM__", item.part + "");
                }

                item.isCompleted = item.isDownloaded && item.isExtracted;
                if (!item.isCompleted) {
                    areAllCompleted = false;
                }

            }

            fragment.progress.setText(Math.max(1, expansionInfo.progress) + "%");
            fragment.progressMessage.setText(progressText);

            float sizeMb = (float) (expansionInfo.size / 1024f / 1024f);

            fragment.expansionInfo.setText("v" + expansionInfo.version + " " + String.format("%.2f", sizeMb) + "MB");

            expansionInfo.isCompleted = areAllCompleted;

            if (expansionInfo.isCompleted) {
                onComplete();
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void dispose() {
        httpRequest = null;
    }

}
