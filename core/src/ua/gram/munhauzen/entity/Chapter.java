package ua.gram.munhauzen.entity;

import java.util.ArrayList;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Chapter extends Entity {

    public int number;
    public String icon;
    public String chapterAudio;
    public ArrayList<ChapterTranslation> translations;

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
