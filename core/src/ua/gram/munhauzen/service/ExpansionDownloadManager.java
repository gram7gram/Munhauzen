package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;

import java.io.InputStream;
import java.util.Locale;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.expansion.ExtractExpansionPartTask;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
import ua.gram.munhauzen.expansion.response.Part;
import ua.gram.munhauzen.screen.loading.fragment.ControlsFragment;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.FileWriter;
import ua.gram.munhauzen.utils.Files;
import ua.gram.munhauzen.utils.Log;

public class ExpansionDownloadManager {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    final ControlsFragment fragment;
    public Net.HttpRequest httpRequest;
    private boolean isCanceled;
    public ExpansionResponse expansionToDownload;
    public String expansionPart;

    final Json json;

    public ExpansionDownloadManager(MunhauzenGame game, ControlsFragment fragment) {
        this.game = game;
        this.fragment = fragment;

        json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
    }

    public void start() {
        Log.i(tag, "start");

        isCanceled = false;

        dispose();

        fetchExpansionToDownload();

        fetchExpansionInfo();
    }

    public void fetchExpansionToDownload() {
        try {

            expansionPart = game.getExpansionPart();

            FileHandle file = Files.getExpansionConfigFile(game.params, expansionPart);

            String content = file.readString("UTF-8");

            Log.i(tag, "fetchExpansionToDownload:\n" + content);

            expansionToDownload = game.databaseManager.loadExpansionInfo(content);

        } catch (Throwable e) {
            Log.e(tag, e);

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    onConnectionFailed();
                }
            });
        }
    }

    public void fetchExpansionInfo() {

        if (game.gameState.purchaseState.purchases == null) {
            onConnectionCanceled();
            return;
        }

        if (expansionToDownload == null) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    onBadResponse();
                }
            });
            return;
        }

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                fragment.progress.setText("");
                fragment.progressMessage.setText(game.t("expansion_download.started"));
            }
        });

        try {

            if (game.gameState.expansionInfo == null) {
                game.gameState.expansionInfo = expansionToDownload;
            }

            ExpansionResponse expansionInfo = game.gameState.expansionInfo;

            if (expansionInfo.isSameExpansion(expansionToDownload)) {

                Log.i(tag, "Same expansion. Resuming download...");

                String log = "Expansion state:";

                for (Part serverPart : expansionToDownload.parts.items) {

                    serverPart.isDownloaded = false;
                    serverPart.isDownloadFailure = false;
                    serverPart.isDownloading = false;

                    serverPart.isExtracting = false;
                    serverPart.isExtracted = false;
                    serverPart.isExtractFailure = false;

                    for (Part localPart : expansionInfo.parts.items) {
                        if (localPart.url.equals(serverPart.url)) {

                            serverPart.isDownloaded = localPart.isDownloaded;
                            serverPart.isExtracted = localPart.isExtracted;

                            boolean exists = ExternalFiles.getExpansionPartFile(game.params, serverPart).exists();

                            if (exists) {
                                serverPart.isDownloaded = true;
                                serverPart.isExtracted = false;
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

                Log.e(tag, "New expansion");

                final float sizeMb = expansionToDownload.size / 1024f / 1024f;

                if (game.params.memoryUsage != null) {
                    float memory = game.params.memoryUsage.megabytesAvailable();
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

            game.gameState.expansionInfo = expansionToDownload;

            game.gameState.expansionInfo.isDownloadStarted = true;

            processAvailablePart();

        } catch (Throwable e) {
            Log.e(tag, e);

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    onConnectionFailed();
                }
            });
        }

    }

    public boolean shouldFetchExpansion() {

        if (game.gameState.purchaseState.purchases == null) {
            return false;
        }

        try {

            fetchExpansionToDownload();

            if (expansionToDownload == null) {
                return true;
            }

            if (game.gameState.expansionInfo == null) {
                game.gameState.expansionInfo = expansionToDownload;
            }

            ExpansionResponse existingExpansion = game.gameState.expansionInfo;

            if (!existingExpansion.isSameExpansion(expansionToDownload)) {
                return true;
            }

            String log = "Expansion state:";

            boolean hasIncomplete = false;

            for (Part part : expansionToDownload.parts.items) {

                part.isDownloaded = false;
                part.isDownloadFailure = false;
                part.isDownloading = false;

                part.isExtracting = false;
                part.isExtracted = false;
                part.isExtractFailure = false;

                for (Part localPart : existingExpansion.parts.items) {
                    if (localPart.url.equals(part.url)) {

                        part.isDownloaded = localPart.isDownloaded;
                        part.isExtracted = localPart.isExtracted;

                        boolean exists = ExternalFiles.getExpansionPartFile(game.params, part).exists();

                        if (exists) {
                            part.isDownloaded = true;
                            part.isExtracted = false;
                        }

                        break;
                    }
                }

                part.isCompleted = part.isDownloaded && part.isExtracted;

                if (!part.isCompleted) {
                    hasIncomplete = true;
                }

                log += "\npart " + part.part
                        + " isCompleted " + (part.isCompleted ? "+" : "-")
                        + " isDownloaded " + (part.isDownloaded ? "+" : "-")
                        + " isExtracted " + (part.isExtracted ? "+" : "-");

            }

            game.gameState.expansionInfo = expansionToDownload;

            Log.i(tag, log);

            return hasIncomplete;

        } catch (Throwable e) {
            Log.e(tag, e);

            return true;
        }
    }

    private void processAvailablePart() {

        if (isDisposed()) {
            Log.i(tag, "ignored");
            return;
        }

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

        if (isDisposed()) {
            Log.i(tag, "ignored");
            return;
        }

        String url = part.getUrl();

        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        final FileHandle output = ExternalFiles.getExpansionPartFile(game.params, part);

        part.isDownloading = true;

        httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(url)
                .timeout(10000)
                .build();

        part.retryCount += 1;

        Log.i(tag, "fetchExpansionPart part#" + part.part
                + "\nretry " + part.retryCount
                + "\n" + url);

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                cleanup();

                try {
                    int code = httpResponse.getStatus().getStatusCode();

                    Log.e(tag, "fetchExpansionPart: " + code);

                    InputStream stream = httpResponse.getResultAsStream();

                    if (code != HttpStatus.SC_OK || stream == null) {

                        if (part.retryCount < 5) {

                            float delay = 1 + (.3f * (part.retryCount - 1));

                            Timer.instance().scheduleTask(new Timer.Task() {
                                @Override
                                public void run() {
                                    fetchExpansionPart(part);
                                }
                            }, delay);

                            return;
                        } else {
                            throw new GdxRuntimeException("Bad request");
                        }
                    }

                    part.retryCount = 0;

                    Files.toFile(stream, output, new FileWriter.ProgressListener() {
                        @Override
                        public void onProgress(float downloaded, long elapsed, float speed) {
                            part.downloadedMB = downloaded;
                            part.downloadSpeed = speed;
                        }
                    });

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

                part.isDownloading = false;
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

        if (isDisposed()) {
            Log.i(tag, "ignored");
            return;
        }

        Log.i(tag, "extractPart part#" + part.part);

        try {

            part.isExtracting = true;

            validateDownloadedPart(part);

            new ExtractExpansionPartTask(game).extract(part);

            part.isExtracted = true;

        } catch (Throwable e) {
            Log.e(tag, e);

            onExtractionFailed(part);
            return;
        }

        try {
            ExternalFiles.getExpansionPartFile(game.params, part).delete();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        processAvailablePart();
    }

    public void validateDownloadedPart(Part part) {

        if (isDisposed()) {
            Log.i(tag, "ignored");
            return;
        }

        Log.i(tag, "validateDownloadedPart part#" + part.part);

        FileHandle expansionFile = ExternalFiles.getExpansionPartFile(game.params, part);
        if (!expansionFile.exists()) {
            throw new GdxRuntimeException("Expansion part#" + part.part + " was not downloaded");
        }

//        String md5 = MD5.get(expansionFile);
//
//        Log.i(tag, "downloaded part #" + part.partKey + " " + md5
//                + "\n" + "original part #" + part.partKey + " " + part.checksum);
//
//        if (!md5.equals(part.checksum)) {
//
//            discardPart(part);
//
//            throw new GdxRuntimeException("Expansion part#" + part.part + " is corrupted");
//        }
    }

    private void discardPart(Part part) {
        part.isDownloaded = false;
        part.isExtracted = false;

        ExternalFiles.getExpansionPartFile(game.params, part).delete();
    }

    private void onBadResponse() {
        Log.e(tag, "onBadResponse");

        ExpansionResponse expansionInfo = game.gameState.expansionInfo;
        expansionInfo.isDownloadStarted = false;

        fragment.retryTitle.setText(game.t("expansion_download.not_found"));
        fragment.showRetry();

        dispose();
    }

    private void onConnectionFailed() {
        Log.e(tag, "onConnectionFailed");

        ExpansionResponse expansionInfo = game.gameState.expansionInfo;
        if (expansionInfo != null)
            expansionInfo.isDownloadStarted = false;

        fragment.retryTitle.setText(game.t("expansion_download.failed"));
        fragment.showRetry();

        dispose();
    }

    private void onExtractionFailed(Part part) {
        Log.e(tag, "onExtractionFailed");

        ExpansionResponse expansionInfo = game.gameState.expansionInfo;
        if (expansionInfo != null)
            expansionInfo.isDownloadStarted = false;

        discardPart(part);

        part.isExtracting = false;
        part.isExtracted = false;
        part.isExtractFailure = true;

        fragment.retryTitle.setText(game.t("expansion_download.extract_failed"));
        fragment.showRetry();

        dispose();
    }

    private void onConnectionCanceled() {
        ExpansionResponse expansionInfo = game.gameState.expansionInfo;
        if (expansionInfo == null || expansionInfo.isCompleted) return;

        Log.e(tag, "onConnectionCanceled");

        fragment.showRetry();

        dispose();
    }

    private void onLowMemory() {
        Log.i(tag, "onLowMemory");

        ExpansionResponse expansionInfo = game.gameState.expansionInfo;
        if (expansionInfo != null)
            expansionInfo.isDownloadStarted = false;

        fragment.retryTitle.setText(game.t("expansion_download.low_memory"));
        fragment.showRetry();

        dispose();
    }

    private void onComplete() {
        ExpansionResponse expansionInfo = game.gameState.expansionInfo;

        if (expansionInfo == null) return;
        if (expansionInfo.isCompleted) return;

        Log.i(tag, "onComplete");

        expansionInfo.isCompleted = true;

        game.syncState();

        fragment.progress.setText("100%");
        fragment.progressMessage.setText(game.t("expansion_download.completed"));

        for (Part item : expansionInfo.parts.items) {
            ExternalFiles.getExpansionPartFile(game.params, item).delete();
        }

        ExternalFiles.updateNomedia(game.params);

        fragment.onExpansionDownloadComplete();
    }

    private float getProgress() {
        ExpansionResponse expansionInfo = game.gameState.expansionInfo;

        if (expansionInfo.parts.count > 0 && expansionInfo.sizeMB > 0) {

            float progress = 0;

            for (Part item : expansionInfo.parts.items) {

                if (item.isExtracted) {
                    progress += 100;
                } else if (item.isDownloaded) {
                    progress += 95;
                } else {
                    progress += 95 * (item.downloadedMB / expansionInfo.sizeMB) * expansionInfo.parts.count;
                }
            }

            progress /= expansionInfo.parts.count;

            return Math.max(0, Math.min(100, progress));
        }

        return 0;
    }

    public void updateProgress() {
        ExpansionResponse expansionInfo = game.gameState.expansionInfo;

        if (expansionInfo == null || isCanceled || !expansionInfo.isDownloadStarted) return;

        try {

            expansionInfo.progress = getProgress();

            boolean areAllCompleted = true;

            String progressText = "";
            for (Part item : expansionInfo.parts.items) {

                if (item.isDownloading) {
                    progressText = game.t("expansion_download.downloading_part")
                            .replace("__NUM__", item.part + "")
                            .replace("__TOTAL__", expansionInfo.parts.count + "")
                            .replace("__SPEED__", item.downloadSpeed == 0 ? "-" : String.format(Locale.US, "%.2f", item.downloadSpeed));
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

            fragment.progress.setText(String.format(Locale.US, "%.2f", expansionInfo.progress) + "%");
            fragment.progressMessage.setText(progressText);

            expansionInfo.isCompleted = areAllCompleted;

            if (expansionInfo.isCompleted) {
                onComplete();
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void cancel() {
        if (httpRequest != null) {
            Gdx.net.cancelHttpRequest(httpRequest);
            httpRequest = null;
        }
        isCanceled = true;
    }

    private boolean isDisposed() {
        return isCanceled;
    }

    public void dispose() {
        httpRequest = null;
    }

}
