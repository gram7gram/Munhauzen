package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Purchase implements JsonEntry {

    @JsonProperty
    public String orderId;
    @JsonProperty
    public String productId;
}
