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
import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.expansion.ExtractExpansionPartTask;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
import ua.gram.munhauzen.expansion.response.Part;
import ua.gram.munhauzen.screen.loading.fragment.ControlsFragment;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.FileWriter;
import ua.gram.munhauzen.utils.Files;
import ua.gram.munhauzen.utils.Log;

public class ExpansionDownloadManager implements Disposable {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    final ControlsFragment fragment;
    public Net.HttpRequest httpRequest;
    private boolean isCanceled;

    final Json json;

    public ExpansionDownloadManager(MunhauzenGame game, ControlsFragment fragment) {
        this.game = game;
        this.fragment = fragment;

        json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
    }

    public void start() {
        Log.i(tag, "start");

        cancel();

        dispose();

        fetchExpansionInfo();
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

                    Files.toFile(httpResponse.getResultAsStream(), output, new FileWriter.ProgressListener() {
                        @Override
                        public void onProgress(float downloaded, long elapsed, float speed) {

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

            ExternalFiles.getExpansionPartFile(game.params, part).delete();

            processAvailablePart();

        } catch (Throwable e) {
            Log.e(tag, e);

            onExtractionFailed(part);
        }
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

    public void fetchExpansionInfo() {

        if (game.gameState.purchaseState.purchases == null) {
            onConnectionCanceled();
            return;
        }

        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                fragment.progress.setText("");
                fragment.progressMessage.setText(game.t("expansion_download.started"));
            }
        });

        Purchase fullPurchase = null, part2Purchase = null, part1Purchase = null;

        for (Purchase purchase : game.gameState.purchaseState.purchases) {
            if (purchase.productId.equals(game.params.appStoreSkuFull)) {
                fullPurchase = purchase;
            }

            if (purchase.productId.equals(game.params.appStoreSkuPart2)) {
                part2Purchase = purchase;
            }

            if (purchase.productId.equals(game.params.appStoreSkuPart1)) {
                part1Purchase = purchase;
            }
        }

        String id = "free";
        if (fullPurchase != null) {
            id = fullPurchase.productId;
        } else if (part2Purchase != null) {
            id = part2Purchase.productId;
        } else if (part1Purchase != null) {
            id = part1Purchase.productId;
        }

        final String url = game.params.getExpansionUrl(id);

        Log.i(tag, "fetchExpansionInfo\nGET " + url);

        httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(url)
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

                        final float sizeMb = (float) (serverExpansionInfo.size / 1024f / 1024f);

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

        part.isExtracted = false;
        part.isExtractFailure = true;

        fragment.retryTitle.setText(game.t("expansion_download.extract_failed") + part.part);
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

        if (expansionInfo == null || isCanceled || !expansionInfo.isDownloadStarted) return;

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
        return httpRequest == null || isCanceled;
    }

    @Override
    public void dispose() {
        httpRequest = null;
        isCanceled = false;
    }

}
