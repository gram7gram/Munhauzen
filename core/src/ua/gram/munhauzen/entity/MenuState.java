package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuState implements JsonEntry {
    @JsonProperty
    public int openCount;
    @JsonProperty
    public boolean isGreetingViewed;
    @JsonProperty
    public boolean isShareViewed;
    @JsonProperty
    public boolean showThankYouBanner;
    @JsonProperty
    public boolean isContinueEnabled;
    @JsonProperty
    public boolean isGalleryBannerViewed;
    @JsonProperty
    public boolean isGoofsBannerViewed;
    @JsonProperty
    public boolean isLegalViewed;
    @JsonProperty
    public boolean isFirstMenuAfterGameStart = true;
}
