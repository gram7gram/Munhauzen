package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class ServantsState implements JsonEntry {

    @JsonProperty
    public int hirePage;
    @JsonProperty
    public final HashSet<String> viewedServants;

    public ServantsState() {
        viewedServants = new HashSet<>();
        hirePage = 1;
    }
}
