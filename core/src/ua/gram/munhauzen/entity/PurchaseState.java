package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class PurchaseState implements JsonEntry {

    @JsonProperty
    public boolean isPro;
    @JsonProperty
    public ArrayList<Purchase> purchases;
    @JsonProperty
    public ArrayList<Product> products;
}
