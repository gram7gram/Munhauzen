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
import ua.gram.munhauzen.service.ConfigDownloadManager;
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
    Label footer;
    public Label progress, progressMessage, expansionInfo;
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

        footerTranslations = new String[]{
                "So listen to my story and tell me whether the world has ever seen a person more truthful than Baron Munchausen.",
                "You can see that there used to be two rows of eleven buttons each on my waistcoat, and that now only three are left.",
                "Nothing is more detestable than a traveller who does not adhere strictly to the truth in his stories.",
                "I left the Assembly without another word, unable to endure any longer the company of men who could not distinguish between lying brag and the simple truth.",
                "Let us raise our cups, dear gentlemen, friends and comrades, for this married couple, for our health and together for the comforts of society and for the unconditional veracity of each narrator!",
                "I have plenty of equally striking proofs at the service of any who are insolent enough to doubt the truth of any of my statements.",
                "Some travellers are apt to advance more than is perhaps strictly true; if any of the company entertain a doubt of my veracity",
                "That’s right, I lifted both myself and my horse up into the air, and if you think it's easy, try doing it yourself.",
                "People can doubt the truth of the stories about my real heroic deeds, which is highly insulting for a noble gentleman, who values his honor.",
                "And if there be any one who doubts the truth of what I say, he is an infidel, and I will fight him at any time and place, and with any weapon he pleases.",
                "All that I have related before is truth and truth, and if there  be any one so hardy as to deny  it, I am ready to fight him with  any weapon he pleases!",
                "My dear friends and companions, have confidence in what I say, and pay  honour to the tales of Munchausen!",
                "A traveller  has a right to relate and embellish his adventures  as he pleases, and it is very impolite to refuse  that deference and applause they deserve.",
                "If any gentleman will say he doubts the truth of this story, I will find him a gallon of lemon juice and make him drink it at one draught.",
                "All my adventures are truth and verity, and who dare to doubt their authenticity, let him tell me it face to face.",
                "You see for yourselves that this strange tale must be true, however improbable it sounds, or else how could it possibly have happened?",
                "I also would doubt it, if I hadn't seen the green iron worms and their destructive activity with my own eyes. ",
                "If the shadow of a doubt can remain on any person’s mind, I say, let him take a voyage to Moon himself, and then he will know I am a traveller of veracity.",
                "This is because I love traveling and I am always looking for adventures, and you sit at home and see nothing, except the four walls of your room.",
        };

        retryBtn = screen.game.buttonBuilder.primaryRose("Retry", new ClickListener() {
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
        progressTable.add(progressMessage).width(MunhauzenGame.WORLD_WIDTH / 2f).padBottom(10).row();
        progressTable.add(retryBtn)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .row();

        Table titleTable = new Table();
        titleTable.add(title).width(MunhauzenGame.WORLD_WIDTH * .75f);

        Label startMessage = new Label("The game will now start downloading the resources. Please, have some patience, keep the battery filled and WiFi on.", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        ));
        startMessage.setWrap(true);
        startMessage.setAlignment(Align.center);

        String quality = "Low";
        switch (screen.game.params.dpi) {
            case "hdpi":
                quality = "High";
                break;
            case "mdpi":
                quality = "Medium";
                break;
        }

        Label qualityMessage = new Label("Recommended texture quality: " + quality, new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        ));
        qualityMessage.setWrap(true);
        qualityMessage.setAlignment(Align.center);

        PrimaryButton startBtn = screen.game.buttonBuilder.primaryRose("Download", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                startContainer.setVisible(false);
                progressContainer.setVisible(true);

                startConfigDownload();
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
        progressContainer.setVisible(false);

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

        if (currentFooterTranslation == footerTranslations.length) {
            currentFooterTranslation = 0;
        }

        footer.setText(footerTranslations[currentFooterTranslation]);
    }

    public void update() {
    }

    public void onConfigDownloadComplete() {

        Log.i(tag, "onConfigDownloadComplete");

        screen.configDownloader.dispose();
        screen.configDownloader = null;

        screen.game.databaseManager.loadExternal(screen.game.gameState);

        startExpansionDownload();
    }

    public void onExpansionDownloadComplete() {

        Log.i(tag, "onExpansionDownloadComplete");

        screen.expansionDownloader.dispose();
        screen.expansionDownloader = null;

        root.setTouchable(Touchable.disabled);

        GameState.clearTimer();

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                screen.navigateTo(new MenuScreen(screen.game));
            }
        }, 1);
    }
}
