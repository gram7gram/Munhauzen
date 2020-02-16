package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Device {

    public enum Type {
        ios, ipad, android
    }

    @JsonProperty
    public Type type;

}
