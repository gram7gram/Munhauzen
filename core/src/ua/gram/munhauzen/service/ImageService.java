package ua.gram.munhauzen.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;

import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.interaction.servants.hire.HireStory;
import ua.gram.munhauzen.interaction.servants.hire.HireStoryScenario;
import ua.gram.munhauzen.repository.ImageRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.transition.FadeTransition;
import ua.gram.munhauzen.screen.game.transition.NormalTransition;
import ua.gram.munhauzen.screen.game.transition.Transition;
import ua.gram.munhauzen.utils.Log;

public abstract class ImageService implements Disposable {

    protected final String tag = getClass().getSimpleName();
    protected final GameScreen gameScreen;
    public AssetManager assetManager;
    Transition transition;

    public ImageService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = createAssetManager();
    }

    public abstract AssetManager createAssetManager();

    public abstract String getResource(StoryImage item);

    public void prepareAndDisplay(StoryImage item) {

        try {

            String resource = getResource(item);
            if (resource == null) return;

            item.resource = resource;

            if (item.isPrepared && item.drawable != null) {
                onPrepared(item);
                return;
            }

            if (!assetManager.isLoaded(resource, Texture.class)) {
                assetManager.load(resource, Texture.class);

                assetManager.finishLoading();
            }

            item.isPreparing = false;
            item.isPrepared = true;
            item.prepareCompletedAt = new Date();

            item.drawable = new SpriteDrawable(new Sprite(
                    assetManager.get(resource, Texture.class)
            ));

            onPrepared(item);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void prepare(StoryImage item, Timer.Task onComplete) {
        try {
            if (item.isPrepared && item.drawable != null) {
                if (item.isLocked && !item.isActive) {
                    Timer.post(onComplete);
                }
                return;
            }

            String resource = getResource(item);
            if (resource == null) return;

            item.resource = resource;

            boolean isLoaded = assetManager.isLoaded(resource, Texture.class);

            if (!isLoaded) {
                if (!item.isPreparing) {

                    item.isPreparing = true;
                    item.prepareStartedAt = new Date();

                    assetManager.load(resource, Texture.class);
                }

            } else {

                item.isPreparing = false;
                item.isPrepared = true;
                item.prepareCompletedAt = new Date();

                item.drawable = new SpriteDrawable(new Sprite(assetManager.get(resource, Texture.class)));

                Timer.post(onComplete);
            }
        } catch (Throwable ignore) {
        }
    }

    public void onPrepared(StoryImage item) {

        try {
            if (item.isActive) return;

            Log.i(tag, "onPrepared " + item.resource);

            Story story = gameScreen.getStory();
            if (story != null) {
                for (StoryScenario scenarioOption : story.scenarios) {
                    for (StoryImage image : scenarioOption.scenario.images) {
                        image.isActive = false;
                    }
                }
            }

            saveCurrentBackground(item);

            displayImage(item);
        } catch (Throwable ignore) {
        }
    }

    protected void displayImage(final StoryImage item) {

        try {
            if (transition != null) {
                if (transition.isLocked) return;
            }

            Log.i(tag, "displayImage " + item.resource);


            gameScreen.showImageFragment();

            if (Scenario.FADE_IN.equals(item.transition)) {
                transition = new FadeTransition(gameScreen);
            } else {
                transition = new NormalTransition(gameScreen);
            }

            transition.prepare(item);

        } catch (Throwable ignore) {
        }
    }

    public void update() {

        try {

            if (assetManager == null) return;

            try {
                assetManager.update();
            } catch (Throwable ignore) {
            }

            Story story = gameScreen.getStory();
            if (story != null) {
                for (StoryScenario scenario : story.scenarios) {
                    if (scenario.startsAt < story.progress) {
                        if (scenario.scenario.images != null) {
                            for (StoryImage storyImage : scenario.scenario.images) {
                                if (storyImage.startsAt <= story.progress) {
                                    saveCurrentBackground(storyImage);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Throwable ignore) {
        }
    }

    public void saveCurrentBackground(StoryImage item) {

        if (item.image != null && !ImageRepository.LAST.equals(item.image)) {
            Image image = ImageRepository.find(gameScreen.game.gameState, item.image);

            if (gameScreen.getLastBackground() != image) {
                gameScreen.setLastBackground(image);
            }
        }
    }

    @Override
    public void dispose() {

        try {
            Story story = gameScreen.getStory();
            if (story != null) {
                dispose(story, true);
            }

            if (assetManager != null) {
                assetManager.dispose();
                assetManager = null;
            }

        } catch (Throwable ignore) {
        }
    }

    public synchronized void dispose(HireStory story) {
        if (assetManager == null) return;

        Log.i(tag, "dispose " + story.id);

        for (HireStoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.images != null) {
                for (StoryImage item : storyScenario.scenario.images) {

                    item.drawable = null;
                    item.isPrepared = false;

                    if (item.resource != null) {
                        if (assetManager.isLoaded(item.resource)) {
                            if (assetManager.getReferenceCount(item.resource) == 0) {
                                assetManager.unload(item.resource);
                                item.resource = null;
                            } else {
                                Log.e(tag, item.image + " dispose ignored");
                            }
                        }
                    }
                }
            }
        }

    }

    public synchronized void dispose(Story story, boolean force) {
        if (assetManager == null) return;

        Log.i(tag, "dispose " + story.id);

        Image last = gameScreen.getLastBackground();

        for (StoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.images != null) {
                for (StoryImage item : storyScenario.scenario.images) {

                    if (force) {
                        if (item.resource != null) {
                            if (assetManager.isLoaded(item.resource)) {
                                assetManager.unload(item.resource);
                            }
                        }

                        item.drawable = null;
                        item.isPrepared = false;
                    } else {

                        boolean isLast = (last != null && last.name.equals(item.image))
                                || item.image.equals(ImageRepository.LAST);

                        if (!isLast) {

                            item.drawable = null;
                            item.isPrepared = false;

                            if (item.resource != null) {
                                if (assetManager.isLoaded(item.resource)) {
                                    if (assetManager.getReferenceCount(item.resource) == 0) {
                                        assetManager.unload(item.resource);
                                        item.resource = null;
                                    } else {
                                        Log.e(tag, item.image + " dispose ignored");
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

    }
}
