package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameVersionResponse;
import ua.gram.munhauzen.screen.VersionScreen;
import ua.gram.munhauzen.utils.Log;

public class GameVersionService implements Disposable {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    Net.HttpRequest httpRequest;
    Timer.Task onComplete;

    final Json json;

    public GameVersionService(MunhauzenGame game) {
        this.game = game;

        json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
    }

    public void start(Timer.Task onComplete) {
        Log.i(tag, "start");

        this.onComplete = onComplete;

        fetchInfo();
    }

    public void fetchInfo() {
        Log.i(tag, "fetchInfo");

        final HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(game.params.getGameVersionUrl())
                .timeout(10000)
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                try {

                    String response = httpResponse.getResultAsString();

                    int code = httpResponse.getStatus().getStatusCode();

                    Log.e(tag, "fetchInfo:\n" + code + ": " + response);

                    if (code != HttpStatus.SC_OK) {
                        throw new GdxRuntimeException("Bad request");
                    }

                    GameVersionResponse gameVersion = json.fromJson(GameVersionResponse.class, response);
                    if (gameVersion == null) {
                        onBadResponse();
                        return;
                    }

                    if (gameVersion.versionCode > game.params.versionCode
                            && !game.gameState.preferences.ignoredAppUpdates.contains(gameVersion.versionCode)) {
                        game.navigator.navigateTo(new VersionScreen(game, gameVersion));
                    } else {
                        onComplete();
                    }


                } catch (Throwable e) {
                    failed(e);
                }

            }

            @Override
            public void failed(Throwable t) {
                Log.e(tag, t);

                onConnectionFailed();
            }

            @Override
            public void cancelled() {

                onConnectionCanceled();

            }
        });
    }

    private void onBadResponse() {
        Log.e(tag, "onBadResponse");

        onComplete();

    }

    private void onConnectionFailed() {
        Log.e(tag, "onConnectionFailed");

        onComplete();
    }

    private void onConnectionCanceled() {
        Log.e(tag, "onConnectionCanceled");

        onComplete();

    }

    private void onComplete() {
        Timer.instance().postTask(onComplete);
    }

    @Override
    public void dispose() {
        httpRequest = null;
    }

}
