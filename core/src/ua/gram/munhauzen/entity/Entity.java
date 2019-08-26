package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Entity implements JsonEntry {

    @JsonProperty
    public String name;

    public Entity() {
    }

    @Override
    @JsonIgnore
    public String toString() {
        return this.getClass().getSimpleName() + "#" + name;
    }
}
