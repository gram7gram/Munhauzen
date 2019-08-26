package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Decision implements JsonEntry {
    @JsonProperty
    public int order;
    @JsonProperty
    public String scenario;
    @JsonProperty
    public ArrayList<String> inventoryRequired;
    @JsonProperty
    public ArrayList<String> inventoryAbsent;
}
