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

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.LoadingScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.service.ConfigDownloadManager;
import ua.gram.munhauzen.service.ExpansionDownloadManager;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final LoadingScreen screen;
    public FragmentRoot root;
    Image decorTop, decorBottom;
    Label footer;
    public Label progress, progressMessage, expansionInfo;
    String[] footerTranslations;
    int currentFooterTranslation = 0;
    public PrimaryButton retryBtn;

    public ControlsFragment(LoadingScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        footerTranslations = new String[]{
                "Да, дорогие друзья! Это действительно так! И кто мне не верит пусть сам отправляется туда! На Луну!",
                "Тому джентельмену. Кто сомневается в моей правдивости, я поставлю вазон коньяка и заставлю ему его выпить до дна!",
                "Да. Друзья, это так!",
                "Resources are download once and will be available offline"
        };

        retryBtn = screen.game.buttonBuilder.primary("Retry", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                retryBtn.setVisible(false);
                progress.setText("");
                progressMessage.setText("");

                startConfigDownload();
            }
        });
        retryBtn.setVisible(false);

        Label title = new Label("Please, wait until resources are downloaded...", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

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

        expansionInfo = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));
        expansionInfo.setWrap(true);
        expansionInfo.setAlignment(Align.center);

        decorTop = new Image();
        decorBottom = new Image();

        Table footerTable = new Table();
        footerTable.add(footer).width(MunhauzenGame.WORLD_WIDTH * .75f);

        Table progressTable = new Table();
        progressTable.add(expansionInfo).width(MunhauzenGame.WORLD_WIDTH / 2f).padBottom(5).row();
        progressTable.add(progress).width(MunhauzenGame.WORLD_WIDTH / 2f).padBottom(5).row();
        progressTable.add(progressMessage).width(MunhauzenGame.WORLD_WIDTH / 2f).padBottom(5).row();
        progressTable.add(retryBtn)
                .width(MunhauzenGame.WORLD_WIDTH * .3f)
                .height(MunhauzenGame.WORLD_HEIGHT * .075f)
                .padBottom(5).row();

        Table titleTable = new Table();
        titleTable.add(title).width(MunhauzenGame.WORLD_WIDTH * .75f);

        Container<Image> topContainer = new Container<>(decorTop);
        topContainer.pad(5);
        topContainer.align(Align.top);

        Container<Image> bottomContainer = new Container<>(decorBottom);
        bottomContainer.pad(5);
        bottomContainer.align(Align.bottom);

        Container<Table> footerContainer = new Container<>(footerTable);
        footerContainer.padBottom(30);
        footerContainer.align(Align.bottom);

        Container<Table> titleContainer = new Container<>(titleTable);
        titleContainer.padTop(30);
        titleContainer.align(Align.top);

        Container<Table> progressContainer = new Container<>(progressTable);
        progressContainer.pad(10);
        progressContainer.align(Align.center);

        root = new FragmentRoot();
        root.addContainer(topContainer);
        root.addContainer(bottomContainer);
        root.addContainer(titleContainer);
        root.addContainer(footerContainer);
        root.addContainer(progressContainer);

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

        startConfigDownload();
    }

    private void startConfigDownload() {

        if (screen.configDownloader != null) {
            screen.configDownloader.dispose();
        }

        screen.configDownloader = new ConfigDownloadManager(screen.game, this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                screen.configDownloader.start();
            }
        }).start();
    }

    private void startExpansionDownload() {

        if (screen.expansionDownloader != null) {
            screen.expansionDownloader.dispose();
        }

        screen.expansionDownloader = new ExpansionDownloadManager(screen.game, this);

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

        decorTop.setSize(width, height);
    }

    public void setDecorBottomBackground(Texture texture) {

        decorBottom.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH - 10;
        float scale = 1f * width / decorBottom.getDrawable().getMinWidth();
        float height = 1f * decorBottom.getDrawable().getMinHeight() * scale;

        decorBottom.setSize(width, height);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void updateFooterText() {

        ++currentFooterTranslation;

        if (currentFooterTranslation == footerTranslations.length) {
            currentFooterTranslation = 0;
        }

        footer.setText(footerTranslations[currentFooterTranslation]);
    }

    public void update() {
    }

    public void onConfigDownloadComplete() {

        Log.i(tag, "onConfigDownloadComplete");

        startExpansionDownload();
    }

    public void onExpansionDownloadComplete() {

        Log.i(tag, "onExpansionDownloadComplete");

        root.setTouchable(Touchable.disabled);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                screen.navigateTo(new MenuScreen(screen.game));
            }
        }, 1);
    }
}
