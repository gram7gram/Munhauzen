package ua.gram.munhauzen;

public interface FirebaseDownloader {
    void downloadChapterAudioFileFromCloud(String path, boolean cleaunup);
    void downloadChapterImageFileFromCloud(String name, boolean cleaunup);
}
