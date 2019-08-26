package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

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
    public ArrayList<ChapterTranslation> translations;

    @JsonIgnore
    public String getDescription(String locale) {
        String text;
        ChapterTranslation translation = null;

        if (translations != null && translations.size() > 0) {
            for (ChapterTranslation trans : translations) {
                if (trans.locale.equals(locale)) {
                    translation = trans;
                    break;
                }
            }

            if (translation == null) {
                translation = translations.get(0);
            }

            text = translation.description;
        } else {
            text = name;
        }

        return text;
    }

}
