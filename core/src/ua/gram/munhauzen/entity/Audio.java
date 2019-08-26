package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Audio extends Entity {

    @JsonProperty
    public String file;
    @JsonProperty
    public int duration;

}
