package ua.gram.munhauzen.interfaces;

public interface DownloadExpansionInteface {

    void downloadExpansionAndDeletePrev(String currentChapterName, DownloadSuccessFailureListener downloadSuccessFailureListener);

    boolean downloadGoof(String goofName, DownloadSuccessFailureListener downloadSuccessFailureListener);

    boolean downloadGallery(String imageName, DownloadSuccessFailureListener downloadSuccessFailureListener);

}
