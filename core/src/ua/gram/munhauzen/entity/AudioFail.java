package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AudioFail extends Entity {

    @JsonProperty
    public String audio;
    @JsonProperty
    public String file;
    @JsonProperty
    public String locale;
    @JsonProperty
    public String description;
    @JsonProperty
    public int duration;
    @JsonProperty
    public boolean isFailOpenedOnStart;
    @JsonProperty
    public boolean isFailOpenedOnComplete;
    @JsonProperty
    public boolean isFailDaughter;

    @JsonIgnore
    public String getDescription(String locale) {

        if (locale.equals(this.locale)) {
            return description;
        }

        return name;
    }

}
