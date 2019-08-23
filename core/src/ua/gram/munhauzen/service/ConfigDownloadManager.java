package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.expansion.ExportResponse;
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
    ExportResponse response;

    public ConfigDownloadManager(MunhauzenGame game, ControlsFragment fragment) {
        this.game = game;
        this.fragment = fragment;

        requestBuilder = new HttpRequestBuilder();
    }

    public void start() {

        dispose();

        httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(game.params.getGameExportUrl())
                .timeout(10000)
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                Json json = new Json(JsonWriter.OutputType.json);
                json.setIgnoreUnknownFields(true);

                String content = httpResponse.getResultAsString();

                Log.e(tag, content);

                response = json.fromJson(ExportResponse.class, content);

                downloadArchive();
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

    private void downloadArchive() {
        httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(response.url)
                .timeout(10000)
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {

            private void cleanup() {
                ExternalFiles.getGameArchiveFile().delete();
            }

            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                cleanup();

                FileHandle output = ExternalFiles.getGameArchiveFile();

                try {

                    Files.toFile(httpResponse.getResultAsStream(), output);

                    Log.i(tag, "downloaded");

                } catch (Throwable e) {

                    cleanup();

                    failed(e);

                    return;
                }

                try {

                    Log.i(tag, "extracting");

                    new ExtractGameConfigTask().extract();

                    Log.i(tag, "extracted");

                } catch (Throwable e) {
                    failed(e);

                    cleanup();

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
                }

                cleanup();
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

    private void onComplete() {
        fragment.onConfigDownloadComplete();
    }

    private void onConnectionFailed() {
        fragment.progress.setText("");
        fragment.progressMessage.setText("Download has failed");
        fragment.retryBtn.setVisible(true);
    }

    private void onConnectionCanceled() {
        fragment.progress.setText("");
        fragment.progressMessage.setText("Download was canceled");
        fragment.retryBtn.setVisible(true);
    }

    public void dispose() {

        if (httpRequest != null) {
            Gdx.net.cancelHttpRequest(httpRequest);
            httpRequest = null;
        }

        response = null;

    }
}
