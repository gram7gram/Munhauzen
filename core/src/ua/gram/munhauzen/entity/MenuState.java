package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Stack;

public class MenuState implements JsonEntry {
    @JsonProperty
    public int openCount;
    @JsonProperty
    public boolean isGreetingViewed;
    @JsonProperty
    public boolean isTutorialViewed;
    @JsonProperty
    public boolean isShareViewed;
    @JsonProperty
    public boolean showThankYouBanner;
    @JsonProperty
    public boolean isContinueEnabled;
    @JsonProperty
    public boolean isFirstMenuAfterGameStart = true;
    @JsonProperty
    public Stack<String> achievementsToDisplay;
}
