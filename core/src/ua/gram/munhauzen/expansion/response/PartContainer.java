package ua.gram.munhauzen.expansion.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.JsonEntry;

public class PartContainer implements JsonEntry {

    @JsonProperty
    public int count;

    @JsonProperty
    public ArrayList<Part> items;
}
