package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.expansion.ExtractGameConfigTask;
import ua.gram.munhauzen.screen.loading.fragment.ControlsFragment;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Files;
import ua.gram.munhauzen.utils.Log;

public class ConfigDownloadManager {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    final HttpRequestBuilder requestBuilder;
    final ControlsFragment fragment;
    Net.HttpRequest httpRequest;

    public ConfigDownloadManager(MunhauzenGame game, ControlsFragment fragment) {
        this.game = game;
        this.fragment = fragment;

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

        httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(game.params.getGameExportUrl())
                .timeout(10000)
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {

            private void cleanup() {
                ExternalFiles.getGameArchiveFile().delete();
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

                    FileHandle output = ExternalFiles.getGameArchiveFile();

                    Files.toFile(httpResponse.getResultAsStream(), output);

                    Log.i(tag, "downloaded");

                } catch (Throwable e) {
                    failed(e);
                    return;
                }

                try {

                    new ExtractGameConfigTask().extract();

                    Log.i(tag, "extracted");

                } catch (Throwable e) {
                    failed(e);
                    return;
                }

                try {

                    game.databaseManager.loadExternal(game.gameState);

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

        fragment.progress.setText("");
        fragment.progressMessage.setText("Fetching game info...");
    }

    private void onComplete() {
        fragment.onConfigDownloadComplete();
    }

    private void onConnectionFailed() {
        Log.e(tag, "onConnectionFailed");

        fragment.progress.setText("");
        fragment.progressMessage.setText("Download has failed");
        fragment.retryBtn.setVisible(true);

        if (fragment.screen.configDownloader != null) {
            fragment.screen.configDownloader.dispose();
            fragment.screen.configDownloader = null;
        }
    }

    private void onConnectionCanceled() {
        Log.e(tag, "onConnectionCanceled");

        fragment.progress.setText("");
        fragment.progressMessage.setText("Download was canceled");
        fragment.retryBtn.setVisible(true);

        if (fragment.screen.configDownloader != null) {
            fragment.screen.configDownloader.dispose();
            fragment.screen.configDownloader = null;
        }
    }

    public void dispose() {

        if (httpRequest != null) {
            Gdx.net.cancelHttpRequest(httpRequest);
            httpRequest = null;
        }

    }
}
