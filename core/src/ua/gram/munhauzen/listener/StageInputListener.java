package ua.gram.munhauzen.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.entity.StoryInteraction;
import ua.gram.munhauzen.interaction.ContinueInteraction;
import ua.gram.munhauzen.interaction.GeneralsInteraction;
import ua.gram.munhauzen.interaction.HareInteraction;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.interaction.TimerInteraction;
import ua.gram.munhauzen.interaction.generals.fragment.GeneralsProgressBarFragment;
import ua.gram.munhauzen.interaction.hare.fragment.HareProgressBarFragment;
import ua.gram.munhauzen.interaction.picture.fragment.PictureProgressBarFragment;
import ua.gram.munhauzen.interaction.timer.fragment.TimerProgressBarFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

public class StageInputListener extends ClickListener {

    final String tag = getClass().getSimpleName();
    final GameScreen gameScreen;

    public StageInputListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        if (event.isHandled()) return;

        try {
            Log.i(tag, "ui clicked");

            StoryInteraction storyInteraction = gameScreen.getStory().currentInteraction;

            if (storyInteraction != null) {
                if (storyInteraction.interaction instanceof ContinueInteraction) {
                    ContinueInteraction continueInteraction = (ContinueInteraction) storyInteraction.interaction;

                    if (continueInteraction.root != null) {
                        if (!continueInteraction.root.isVisible()) {
                            if (!continueInteraction.isFadeIn) {
                                continueInteraction.fadeIn();
                            }
                        } else {
                            if (!continueInteraction.isFadeOut) {
                                continueInteraction.fadeOut();
                            }
                        }
                    }
                }

                if (storyInteraction.interaction instanceof HareInteraction) {
                    HareProgressBarFragment barFragment = ((HareInteraction) storyInteraction.interaction).progressBarFragment;

                    if (barFragment != null) {
                        if (!barFragment.getRoot().isVisible()) {
                            if (!barFragment.isFadeIn) {
                                barFragment.fadeIn();
                                barFragment.scheduleFadeOut();
                            }
                        } else {
                            if (!barFragment.isFadeOut) {
                                barFragment.fadeOut();
                            }
                        }
                    }
                }

                if (storyInteraction.interaction instanceof PictureInteraction) {
                    PictureProgressBarFragment barFragment = ((PictureInteraction) storyInteraction.interaction).progressBarFragment;

                    if (barFragment != null) {
                        if (!barFragment.getRoot().isVisible()) {
                            if (!barFragment.isFadeIn) {
                                barFragment.fadeIn();
                                barFragment.scheduleFadeOut();
                            }
                        } else {
                            if (!barFragment.isFadeOut) {
                                barFragment.fadeOut();
                            }
                        }
                    }
                }

                if (storyInteraction.interaction instanceof GeneralsInteraction) {
                    GeneralsProgressBarFragment barFragment = ((GeneralsInteraction) storyInteraction.interaction).progressBarFragment;

                    if (barFragment != null) {
                        if (!barFragment.getRoot().isVisible()) {
                            if (!barFragment.isFadeIn) {
                                barFragment.fadeIn();
                                barFragment.scheduleFadeOut();
                            }
                        } else {
                            if (!barFragment.isFadeOut) {
                                barFragment.fadeOut();
                            }
                        }
                    }
                }

                if (storyInteraction.interaction instanceof TimerInteraction) {
                    TimerProgressBarFragment barFragment = ((TimerInteraction) storyInteraction.interaction).progressBarFragment;

                    if (barFragment != null) {
                        if (!barFragment.getRoot().isVisible()) {
                            if (!barFragment.isFadeIn) {
                                barFragment.fadeIn();
                                barFragment.scheduleFadeOut();
                            }
                        } else {
                            if (!barFragment.isFadeOut) {
                                barFragment.fadeOut();
                            }
                        }
                    }
                }
            }

            if (storyInteraction == null || storyInteraction.interaction instanceof ContinueInteraction) {

                if (!gameScreen.progressBarFragment.getRoot().isVisible()) {
                    gameScreen.showProgressBar();
                } else {
                    gameScreen.hideProgressBar();
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }

    }
}