package ua.gram.munhauzen.screen.loading.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.LoadingScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final LoadingScreen screen;
    public FragmentRoot root;
    Image decorTop, decorBottom;
    Label footer;
    public Label progress, progressMessage, expansionInfo, subtitle;
    String[] footerTranslations;
    int currentFooterTranslation = 0;
    public PrimaryButton retryBtn;
    Container<Table> startContainer, progressContainer;
    Table topTable, bottomTable;

    public ControlsFragment(LoadingScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        footerTranslations = screen.game.t("loading.footer").split("\n");

        retryBtn = screen.game.buttonBuilder.primaryRose(screen.game.t("loading.retry_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                retryBtn.setVisible(false);
                progress.setText("");
                progressMessage.setText("");

                startExpansionDownload();
            }
        });
        retryBtn.setVisible(false);

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

        footer = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        ));
        footer.setWrap(true);
        footer.setAlignment(Align.center);

        progress = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
        progress.setWrap(true);
        progress.setAlignment(Align.center);

        progressMessage = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.BLACK
        ));
        progressMessage.setWrap(true);
        progressMessage.setAlignment(Align.center);

        decorTop = new Image();
        decorBottom = new Image();

        Table footerTable = new Table();
        footerTable.add(footer).width(MunhauzenGame.WORLD_WIDTH * .75f);

        Table progressTable = new Table();
        progressTable.add(progress).width(MunhauzenGame.WORLD_WIDTH / 2f).padBottom(5).row();
        progressTable.add(progressMessage).width(MunhauzenGame.WORLD_WIDTH / 2f).padBottom(10).row();
        progressTable.add(retryBtn)
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

        String quality;
        if ("hdpi".equals(screen.game.params.dpi)) {
            quality = screen.game.t("loading.quality_high");
        } else {
            quality = screen.game.t("loading.quality_medium");
        }

        Label qualityMessage = new Label(screen.game.t("loading.quality_message") + ": " + quality, new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        ));
        qualityMessage.setWrap(true);
        qualityMessage.setAlignment(Align.center);

        PrimaryButton startBtn = screen.game.buttonBuilder.primaryRose(screen.game.t("loading.download_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                startContainer.setVisible(false);
                progressContainer.setVisible(true);

                startExpansionDownload();
            }
        });

        Table startTable = new Table();
        startTable.add(startMessage)
                .width(MunhauzenGame.WORLD_WIDTH * .75f)
                .padBottom(10).row();
        startTable.add(qualityMessage)
                .width(MunhauzenGame.WORLD_WIDTH * .75f)
                .padBottom(10).row();
        startTable.add(startBtn)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT);

        topTable = new Table();
        topTable.add(decorTop).expand().top().pad(5);

        bottomTable = new Table();
        bottomTable.add(decorBottom).expand().bottom().pad(5);

        Container<Table> footerContainer = new Container<>(footerTable);
        footerContainer.padBottom(30);
        footerContainer.align(Align.bottom);

        Container<Table> titleContainer = new Container<>(titleTable);
        titleContainer.padTop(30);
        titleContainer.align(Align.top);

        progressContainer = new Container<>(progressTable);
        progressContainer.pad(10);
        progressContainer.padTop(MunhauzenGame.WORLD_HEIGHT * .25f);
        progressContainer.align(Align.top);
        progressContainer.setVisible(true);

        startContainer = new Container<>(startTable);
        startContainer.pad(10);
        startContainer.padTop(MunhauzenGame.WORLD_HEIGHT * .25f);
        startContainer.align(Align.top);
        startContainer.setVisible(true);

        root = new FragmentRoot();
        root.addContainer(topTable);
        root.addContainer(bottomTable);
        root.addContainer(titleContainer);
        root.addContainer(footerContainer);
        root.addContainer(progressContainer);
        root.addContainer(startContainer);

        root.setName(tag);

        setDecorBottomBackground(
                screen.assetManager.get("loading/lv_decor_1.png", Texture.class)
        );

        setDecorTopBackground(
                screen.assetManager.get("loading/lv_decor_1.png", Texture.class)
        );

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (isMounted()) {
                    updateFooterText();
                } else {
                    cancel();
                }
            }
        }, .2f, 20);
    }

    private void startExpansionDownload() {

        if (screen.expansionDownloader != null) {
            screen.expansionDownloader.dispose();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                screen.expansionDownloader.start();
            }
        }).start();
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

    public void updateFooterText() {

        ++currentFooterTranslation;

        if (currentFooterTranslation >= footerTranslations.length) {
            currentFooterTranslation = 0;
        }

        footer.setText(footerTranslations[currentFooterTranslation]);
    }

    public void update() {

    }

    public void onExpansionDownloadComplete() {

        Log.i(tag, "onExpansionDownloadComplete");

        screen.expansionDownloader.dispose();
        screen.expansionDownloader = null;

        root.setTouchable(Touchable.disabled);

        GameState.clearTimer(tag);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                screen.navigateTo(new MenuScreen(screen.game));
            }
        }, 1);
    }
}
