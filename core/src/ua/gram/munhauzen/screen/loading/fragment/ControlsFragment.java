package ua.gram.munhauzen.screen.loading.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.Locale;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
import ua.gram.munhauzen.screen.LoadingScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.service.ExpansionDownloadManager;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final LoadingScreen screen;
    public FragmentRoot root;
    Image decorTop, decorBottom;
    public Label progress, progressMessage, subtitle, retryTitle;
    Container<Table> startContainer, progressContainer, retryContainer;
    Table topTable, bottomTable;
    PrimaryButton menuBtn, completeBtn, cancelBtn;
    Thread downloadThread;

    public ControlsFragment(LoadingScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        PrimaryButton startBtn = screen.game.buttonBuilder.primaryRose(screen.game.t("loading.download_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                progress.setText("");
                progressMessage.setText("");

                showDownload();

                startExpansionDownload();
            }
        });

        PrimaryButton retryBtn = screen.game.buttonBuilder.danger(screen.game.t("loading.retry_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                progress.setText("");
                progressMessage.setText("");

                showDownload();

                startExpansionDownload();
            }
        });

        menuBtn = screen.game.buttonBuilder.danger(screen.game.t("loading.menu_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.navigateTo(new MenuScreen(screen.game));
            }
        });

        PrimaryButton purchasesBtn = screen.game.buttonBuilder.danger(screen.game.t("loading.purchases_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.navigateTo(new PurchaseScreen(screen.game));
            }
        });

        cancelBtn = screen.game.buttonBuilder.danger(screen.game.t("loading.cancel_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                stopDownloadThread();

                if (screen.expansionDownloader != null) {
                    screen.expansionDownloader.cancel();
                }

                retryTitle.setText(screen.game.t("loading.retry_title"));
                showRetry();
            }
        });

        completeBtn = screen.game.buttonBuilder.danger(screen.game.t("loading.completed_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.navigateTo(new MenuScreen(screen.game));
            }
        });
        completeBtn.setVisible(false);

        Label title = new Label(screen.game.t("loading.title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

        subtitle = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));
        subtitle.setWrap(true);
        subtitle.setAlignment(Align.center);

        progress = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
        progress.setWrap(true);
        progress.setAlignment(Align.center);

        progressMessage = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        ));
        progressMessage.setWrap(true);
        progressMessage.setAlignment(Align.center);

        decorTop = new Image();
        decorBottom = new Image();

        Table progressTable = new Table();
        progressTable.add(progress).width(MunhauzenGame.WORLD_WIDTH / 2f).padBottom(5).row();
        progressTable.add(progressMessage).width(MunhauzenGame.WORLD_WIDTH / 2f).padBottom(10).row();
        progressTable.add(cancelBtn)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .row();
        progressTable.add(completeBtn)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .row();

        Table titleTable = new Table();
        titleTable.add(title).width(MunhauzenGame.WORLD_WIDTH * .75f).padBottom(10).row();
        titleTable.add(subtitle).width(MunhauzenGame.WORLD_WIDTH * .75f).row();

        Label startMessage = new Label(screen.game.t("loading.message"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        ));
        startMessage.setWrap(true);
        startMessage.setAlignment(Align.center);

        retryTitle = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
        retryTitle.setWrap(true);
        retryTitle.setAlignment(Align.center);

        Table startTable = new Table();
        startTable.add(startMessage)
                .width(MunhauzenGame.WORLD_WIDTH * .9f)
                .padBottom(10).row();
        startTable.add(startBtn)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT);

        Table retryTable = new Table();
        retryTable.add(retryTitle)
                .width(MunhauzenGame.WORLD_WIDTH * .9f)
                .padBottom(10).row();
        retryTable.add(retryBtn)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .padBottom(10).row();
        retryTable.add(purchasesBtn)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .padBottom(10).row();
        retryTable.add(menuBtn)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .padBottom(10).row();

        topTable = new Table();
        topTable.add(decorTop).expand().top().pad(5);

        bottomTable = new Table();
        bottomTable.add(decorBottom).expand().bottom().pad(5);

        Container<Table> titleContainer = new Container<>(titleTable);
        titleContainer.padTop(30);
        titleContainer.align(Align.top);

        progressContainer = new Container<>(progressTable);
        progressContainer.pad(10);
        progressContainer.padTop(MunhauzenGame.WORLD_HEIGHT * .25f);
        progressContainer.align(Align.top);

        startContainer = new Container<>(startTable);
        startContainer.pad(10);
        startContainer.align(Align.center);

        retryContainer = new Container<>(retryTable);
        retryContainer.pad(10);
        retryContainer.align(Align.center);

        root = new FragmentRoot();
        root.addContainer(topTable);
        root.addContainer(bottomTable);
        root.addContainer(titleContainer);
        root.addContainer(progressContainer);
        root.addContainer(startContainer);
        root.addContainer(retryContainer);

        root.setName(tag);

        setDecorBottomBackground(
                screen.assetManager.get("loading/lv_decor_1.png", Texture.class)
        );

        setDecorTopBackground(
                screen.assetManager.get("loading/lv_decor_1.png", Texture.class)
        );

        showIntro();
    }

    private void showIntro() {
        startContainer.setVisible(true);
        progressContainer.setVisible(false);
        retryContainer.setVisible(false);
    }

    private void showDownload() {
        startContainer.setVisible(false);
        progressContainer.setVisible(true);
        retryContainer.setVisible(false);
    }

    public void showRetry() {
        startContainer.setVisible(false);
        progressContainer.setVisible(false);
        retryContainer.setVisible(true);
    }

    private void startExpansionDownload() {

        screen.game.sfxService.onLoadingVisited();

        if (screen.expansionDownloader != null) {
            screen.expansionDownloader.dispose();
        }

        downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    screen.expansionDownloader = new ExpansionDownloadManager(screen.game, ControlsFragment.this);
                    screen.expansionDownloader.start();
                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        downloadThread.start();
    }

    public void setDecorTopBackground(Texture texture) {

        Sprite sprite = new Sprite(texture);
        sprite.flip(false, true);

        decorTop.setDrawable(new SpriteDrawable(sprite));

        float width = MunhauzenGame.WORLD_WIDTH - 10;
        float scale = 1f * width / decorTop.getDrawable().getMinWidth();
        float height = 1f * decorTop.getDrawable().getMinHeight() * scale;

        topTable.getCell(decorTop).size(width, height);
    }

    public void setDecorBottomBackground(Texture texture) {

        decorBottom.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH - 10;
        float scale = 1f * width / decorBottom.getDrawable().getMinWidth();
        float height = 1f * decorBottom.getDrawable().getMinHeight() * scale;

        bottomTable.getCell(decorBottom).size(width, height);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {
        try {

            if (screen.expansionDownloader != null) {

                ExpansionResponse expansionInfo = screen.expansionDownloader.expansionToDownload;
                String part = screen.expansionDownloader.expansionPart;

                if (expansionInfo == null || part == null) {
                    subtitle.setText("");
                    menuBtn.setVisible(false);
                    return;
                }

                menuBtn.setVisible(expansionInfo.isCompleted);

                if (expansionInfo.version > 0) {

                    float sizeMb = expansionInfo.size / 1024f / 1024f;

                    subtitle.setText(part
                            + " " + expansionInfo.dpi
                            + " " + String.format(Locale.US, "%.2f", sizeMb) + "MB"
                            + " v" + expansionInfo.version);
                }
            }

        } catch (Throwable ignore) {
        }

    }

    public void onExpansionDownloadComplete() {

        Log.i(tag, "onExpansionDownloadComplete");

        if (screen.expansionDownloader != null) {
            screen.expansionDownloader.dispose();
            screen.expansionDownloader = null;
        }

        completeBtn.setVisible(true);
        cancelBtn.setVisible(false);

        GameState.clearTimer(tag);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                screen.navigateTo(new MenuScreen(screen.game));
            }
        }, 1);
    }

    private void stopDownloadThread() {
        try {
            if (downloadThread != null) {
                downloadThread.interrupt();
                downloadThread = null;
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        stopDownloadThread();
    }
}
