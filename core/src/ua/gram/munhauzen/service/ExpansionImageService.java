package ua.gram.munhauzen.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.repository.ImageRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Files;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExpansionImageService extends ImageService {

    public ExpansionImageService(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public AssetManager createAssetManager() {
        return new ExpansionAssetManager(gameScreen.game);
    }

    @Override
    public String getResource(StoryImage item) {
        Image image = ImageRepository.find(gameScreen.game.gameState, item.image);

        if (ImageRepository.LAST.equals(item.image) && image == null) return null;

        if ("intro".equals(item.chapter)) {
            return Files.getIntroImage(image).path();
        }

        FileHandle file = ExternalFiles.getExpansionImage(gameScreen.game.params, image);
        if (!file.exists()) {


            /*  if(gameScreen.game.isOnlineMode()) {

             *//* gameScreen.banner.fadeOut(new Runnable() {
                    @Override
                    public void run() {*//*
                        //screen.destroyBanners();


                        gameScreen.openChapterDownloadBanner(new Runnable() {
                            @Override
                            public void run() {
                                //screen.destroyBanners();
                                System.out.println("openedChapter");
                            }
                        });

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                long time = System.currentTimeMillis();
                                while (System.currentTimeMillis() < time + 1000){}
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("");

                                        String myKong = gameScreen.game.gameState.chapterRegistry.get(0).name;
                                        String previousChapterName = GameScreen.getPreviousChapterName(gameScreen.game.gameState.chapterRegistry.get(0).name);

                                        final boolean[] isSuccess = {false,false};
                                        int i =1;

                                        //isSuccess[1] = false;

                                        while(isSuccess[0] != true && isSuccess[1] == false) {

                                            if (i == 1) {
                                                MunhauzenGame.downloadExpansionInteface.downloadExpansionAndDeletePrev(previousChapterName, new DownloadSuccessFailureListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                        isSuccess[0] = true;
                                                        //screen.navigateTo(new GameScreen(screen.game));
                                                    }

                                                    @Override
                                                    public void onFailure() {
                                                        isSuccess[1] = true;
                                                        gameScreen.openNoInternetBanner(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                //screen.destroyBanners();


                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            i++;


                                        }



                                        if(isSuccess[0]) {
                                            gameScreen.navigateTo(new GameScreen(gameScreen.game));
                                        }

                                    }
                                });
                            }
                        }).start();

                   *//* }
                });*//*


            }*/

            return null;
        }

        return file.path();
    }

}
