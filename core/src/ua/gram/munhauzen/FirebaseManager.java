package ua.gram.munhauzen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.DateInteraction;
import ua.gram.munhauzen.interaction.HornInteraction;
import ua.gram.munhauzen.interaction.InteractionFactory;
import ua.gram.munhauzen.interaction.LionsInteraction;
import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.interaction.SwampInteraction;
import ua.gram.munhauzen.interaction.cannons.CannonsScenario;
import ua.gram.munhauzen.interaction.generals.GeneralsScenario;
import ua.gram.munhauzen.interaction.hare.HareScenario;
import ua.gram.munhauzen.interaction.picture.PictureScenario;
import ua.gram.munhauzen.interaction.servants.hire.HireScenario;
import ua.gram.munhauzen.interaction.timer.TimerScenario;
import ua.gram.munhauzen.interaction.timer2.Timer2Scenario;
import ua.gram.munhauzen.interaction.wauwau.WauScenario;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.utils.Log;

public class FirebaseManager {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;

    public FirebaseManager(MunhauzenGame game) {
        this.game = game;
    }

    public void downloadInteraction(String chapterName, FirebaseDownloader downloader) {
        ArrayList<Scenario> scenario = game.databaseManager.loadExternalScenario();
        ArrayList<String> scenarioWithChapter = new ArrayList<>();

        for (Scenario item : scenario) {
            if (chapterName.equals(item.chapter) && item.interaction != null) {
                downloadAudio(item.interaction, downloader);
                downloadImage(item.interaction, downloader);

                scenarioWithChapter.add(item.name);
            }
        }

        Log.i(tag, "download: " + chapterName + " " + scenarioWithChapter.toString());
    }

    private void downloadAudio(String interaction, FirebaseDownloader downloader) {
        HashSet<String> resources = new HashSet<>();
        switch (interaction) {
            case InteractionFactory.DATE:
                resources.addAll(Arrays.asList(DateInteraction.externalAudio));
                break;
            case InteractionFactory.HORN:
                resources.addAll(Arrays.asList(HornInteraction.externalAudio));
                break;
            case InteractionFactory.LIONS:
                resources.addAll(Arrays.asList(LionsInteraction.externalAudio));
                break;
            case InteractionFactory.PUZZLE:
                resources.addAll(Arrays.asList(PuzzleInteraction.externalAudio));
                break;
            case InteractionFactory.SWAMP:
                resources.addAll(Arrays.asList(SwampInteraction.externalAudio));
                break;
            case InteractionFactory.WAUWAU: {
                ArrayList<WauScenario> scenario = game.databaseManager.loadWauwauScenario();
                for (WauScenario item : scenario) {
                    if (item.audio != null) {
                        for (StoryAudio a : item.audio) {
                            resources.add(a.audio
                                    .replace("audio/", "")
                                    .replace(".aac", "")
                                    .replace(".mp3", "")
                                    .replace(".ogg", "")
                                    .replace(".wav", ""));
                        }
                    }

                }
                break;
            }
            case InteractionFactory.SERVANTS: {
                ArrayList<HireScenario> scenario = game.databaseManager.loadServantsHireScenario();
                for (HireScenario item : scenario) {
                    if (item.audio != null) {
                        for (StoryAudio a : item.audio) {
                            resources.add(a.audio
                                    .replace("audio/", "")
                                    .replace(".aac", "")
                                    .replace(".mp3", "")
                                    .replace(".ogg", "")
                                    .replace(".wav", ""));
                        }

                    }

                }
                break;
            }
            case InteractionFactory.PICTURE: {
                ArrayList<PictureScenario> scenario = game.databaseManager.loadPictureScenario();
                for (PictureScenario item : scenario) {
                    if (item.audio != null) {
                        for (StoryAudio a : item.audio) {
                            resources.add(a.audio
                                    .replace("audio/", "")
                                    .replace(".aac", "")
                                    .replace(".mp3", "")
                                    .replace(".ogg", "")
                                    .replace(".wav", ""));
                        }

                    }

                }
                break;
            }
            case InteractionFactory.HARE: {
                ArrayList<HareScenario> scenario = game.databaseManager.loadHareScenario();
                for (HareScenario item : scenario) {
                    if (item.audio != null) {
                        for (StoryAudio a : item.audio) {
                            resources.add(a.audio
                                    .replace("audio/", "")
                                    .replace(".aac", "")
                                    .replace(".mp3", "")
                                    .replace(".ogg", "")
                                    .replace(".wav", ""));

                        }
                    }

                }
                break;
            }
            case InteractionFactory.GENERAL: {
                ArrayList<GeneralsScenario> scenario = game.databaseManager.loadGeneralsScenario();
                for (GeneralsScenario item : scenario) {
                    if (item.audio != null) {
                        for (StoryAudio a : item.audio) {
                            resources.add(a.audio
                                    .replace("audio/", "")
                                    .replace(".aac", "")
                                    .replace(".mp3", "")
                                    .replace(".ogg", "")
                                    .replace(".wav", ""));

                        }
                    }

                }
                break;
            }
            case InteractionFactory.CANNONS: {
                ArrayList<CannonsScenario> scenario = game.databaseManager.loadCannonsScenario();
                for (CannonsScenario item : scenario) {
                    if (item.audio != null) {
                        for (StoryAudio a : item.audio) {
                            resources.add(a.audio
                                    .replace("audio/", "")
                                    .replace(".aac", "")
                                    .replace(".mp3", "")
                                    .replace(".ogg", "")
                                    .replace(".wav", ""));
                        }
                    }

                }
                break;
            }
        }


        if (interaction.contains(InteractionFactory.TIMER)) {
            ArrayList<TimerScenario> scenario = game.databaseManager.loadTimerScenario();
            for (TimerScenario item : scenario) {
                if (item.audio != null) {
                    for (StoryAudio a : item.audio) {
                        resources.add(a.audio
                                .replace("audio/", "")
                                .replace(".aac", "")
                                .replace(".mp3", "")
                                .replace(".ogg", "")
                                .replace(".wav", ""));
                    }
                }


            }
        }

        if (interaction.contains(InteractionFactory.TIMER_2)) {
            ArrayList<Timer2Scenario> scenario = game.databaseManager.loadTimer2Scenario();
            for (Timer2Scenario item : scenario) {
                if (item.audio != null) {
                    for (StoryAudio a : item.audio) {
                        resources.add(a.audio
                                .replace("audio/", "")
                                .replace(".aac", "")
                                .replace(".mp3", "")
                                .replace(".ogg", "")
                                .replace(".wav", ""));

                    }
                }

            }
        }

        Log.i(tag, "Downloading audio " + Arrays.toString(resources.toArray()));
        for (String res : resources) {
            Audio audio = AudioRepository.find(game.gameState, res);
            downloader.downloadChapterAudioFileFromCloud(audio.file, false);
        }
    }

    private void downloadImage(String interaction, FirebaseDownloader downloader) {
        HashSet<String> resources = new HashSet<>();
        switch (interaction) {
            case InteractionFactory.WAUWAU: {
                ArrayList<WauScenario> scenario = game.databaseManager.loadWauwauScenario();
                for (WauScenario item : scenario) {
                    if (item.images != null) {
                        for (StoryImage a : item.images) {
                            if (a.image.contains("images/")) {
                                resources.add(a.image
                                        .replace("images/", "")
                                        .replace(".jpg", ""));
                            }
                        }
                    }

                }
                break;
            }
            case InteractionFactory.SERVANTS: {
                ArrayList<HireScenario> scenario = game.databaseManager.loadServantsHireScenario();
                for (HireScenario item : scenario) {
                    if (item.images != null) {
                        for (StoryImage a : item.images) {
                            if (a.image.contains("images/")) {
                                resources.add(a.image
                                        .replace("images/", "")
                                        .replace(".jpg", ""));
                            }
                        }
                    }

                }
                break;
            }
            case InteractionFactory.PICTURE: {
                ArrayList<PictureScenario> scenario = game.databaseManager.loadPictureScenario();
                for (PictureScenario item : scenario) {
                    if (item.images != null) {
                        for (StoryImage a : item.images) {
                            if (a.image.contains("images/")) {
                                resources.add(a.image
                                        .replace("images/", "")
                                        .replace(".jpg", ""));
                            }
                        }
                    }

                }
                break;
            }
            case InteractionFactory.GENERAL: {
                ArrayList<GeneralsScenario> scenario = game.databaseManager.loadGeneralsScenario();
                for (GeneralsScenario item : scenario) {
                    if (item.images != null) {
                        for (StoryImage a : item.images) {
                            if (a.image.contains("images/")) {
                                resources.add(a.image
                                        .replace("images/", "")
                                        .replace(".jpg", ""));
                            }
                        }
                    }

                }
                break;
            }
            case InteractionFactory.CANNONS: {
                ArrayList<CannonsScenario> scenario = game.databaseManager.loadCannonsScenario();
                for (CannonsScenario item : scenario) {
                    if (item.images != null) {
                        for (StoryImage a : item.images) {
                            if (a.image.contains("images/")) {
                                resources.add(a.image
                                        .replace("images/", "")
                                        .replace(".jpg", ""));
                            }
                        }
                    }
                }
                break;
            }
        }


        if (interaction.contains(InteractionFactory.TIMER)) {
            ArrayList<TimerScenario> scenario = game.databaseManager.loadTimerScenario();
            for (TimerScenario item : scenario) {
                if (item.images != null) {
                    for (StoryImage a : item.images) {
                        if (a.image.contains("images/")) {
                            resources.add(a.image
                                    .replace("images/", "")
                                    .replace(".jpg", ""));
                        }
                    }
                }

            }
        }
        if (interaction.contains(InteractionFactory.TIMER_2)) {
            ArrayList<Timer2Scenario> scenario = game.databaseManager.loadTimer2Scenario();
            for (Timer2Scenario item : scenario) {
                if (item.images != null) {
                    for (StoryImage a : item.images) {
                        if (a.image.contains("images/")) {
                            resources.add(a.image
                                    .replace("images/", "")
                                    .replace(".jpg", ""));
                        }
                    }
                }

            }
        }

        Log.i(tag, "Downloading images " + Arrays.toString(resources.toArray()));
        for (String res : resources) {
            downloader.downloadChapterImageFileFromCloud(res, false);
        }
    }
}
