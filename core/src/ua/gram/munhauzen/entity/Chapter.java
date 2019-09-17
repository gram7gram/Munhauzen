package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Chapter extends Entity {

    @JsonProperty
    public int number;
    @JsonProperty
    public String icon;
    @JsonProperty
    public String chapterAudio;
    @JsonProperty
    public String description;

}
