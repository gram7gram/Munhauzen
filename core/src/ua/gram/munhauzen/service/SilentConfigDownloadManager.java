package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.expansion.ExtractGameConfigTask;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.FileWriter;
import ua.gram.munhauzen.utils.Files;
import ua.gram.munhauzen.utils.Log;

public class SilentConfigDownloadManager {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    final HttpRequestBuilder requestBuilder;
    Net.HttpRequest httpRequest;

    public SilentConfigDownloadManager(MunhauzenGame game) {
        this.game = game;

        requestBuilder = new HttpRequestBuilder();
    }

    public void start() {

        dispose();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                onConnectionStarted();
            }
        });

        String url = game.params.getGameExportUrl();

        httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(url)
                .timeout(10000)
                .build();

        Log.e(tag, "GET " + url);

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {

            private void cleanup() {
                ExternalFiles.getGameArchiveFile(game.params).delete();
            }

            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                try {

                    cleanup();

                    int code = httpResponse.getStatus().getStatusCode();

                    Log.e(tag, "getGameExportUrl: " + code);

                    if (code != HttpStatus.SC_OK) {
                        throw new GdxRuntimeException("Bad request");
                    }

                    FileHandle output = ExternalFiles.getGameArchiveFile(game.params);

                    Files.toFile(httpResponse.getResultAsStream(), output, new FileWriter.ProgressListener() {
                        @Override
                        public void onProgress(float downloaded, long elapsed, float speed) {

                        }
                    });

                    Log.i(tag, "downloaded");

                } catch (Throwable e) {
                    failed(e);
                    return;
                }

                try {

                    new ExtractGameConfigTask(game).extract();

                    Log.i(tag, "extracted");

                } catch (Throwable e) {
                    failed(e);
                    return;
                }

                try {

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            onComplete();
                        }
                    });

                } catch (Throwable e) {
                    failed(e);
                    return;
                }

                cleanup();
            }

            @Override
            public void failed(Throwable t) {
                Log.e(tag, t);

                cleanup();

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

    private void onConnectionStarted() {
        Log.i(tag, "onConnectionStarted");
    }

    private void onComplete() {

        game.databaseManager.loadExternal(game.gameState);

    }

    private void onConnectionFailed() {
        Log.e(tag, "onConnectionFailed");

    }

    private void onConnectionCanceled() {
        Log.e(tag, "onConnectionCanceled");

    }

    public void dispose() {

        httpRequest = null;

    }
}

