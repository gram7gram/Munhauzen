package ua.gram.munhauzen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FontProvider implements Disposable {

    final MunhauzenGame game;
    final String tag = getClass().getSimpleName();

    final String alphabet = "\u0000\"'1234567890-=+?!@#$%&*(){}[].,:;/_><…"
            + "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz"
            + "АаБбВвГгДдЕеЭэЖжЗзИиЙйЫыКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчЩщШшЮюЯяЬьЪъ"
            + "АаБбВвГгДдЕеЄЄЖжЗзИиЙйІіЇїКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчЩщШшЮюЯяЬь";

    public static final String
            CalligraphModern2 = "CalligraphModern2.ttf",
            BuxtonSketch = "BuxtonSketch.ttf",
            FleischmannGotich = "FleischmannGotich.ttf",
            DroidSansMono = "DroidSansMono.ttf";

    public static final int
            hd = 164,
            h1 = 80,
            h2 = 60,
            h3 = 50,
            h4 = 40,
            h5 = 32,
            p = 24,
            small = 16;

    private HashMap<String, HashMap<Integer, BitmapFont>> map;
    private HashMap<String, HashMap<Integer, BitmapFont>> mapHd;

    public FontProvider(MunhauzenGame game) {
        this.game = game;
    }

    public BitmapFont getFont(String font, Integer size) {
        if (!map.containsKey(font)) return null;
        if (!map.get(font).containsKey(size)) return null;
        return map.get(font).get(size);
    }

    public BitmapFont getHdFont(String font, Integer size) {
        if (!mapHd.containsKey(font)) return null;
        if (!mapHd.get(font).containsKey(size)) return null;
        return mapHd.get(font).get(size);
    }

    public BitmapFont getFont(Integer size) {
        return getFont(CalligraphModern2, size);
    }

    public void load() {

        Log.i(tag, "load");

        String[] fonts = new String[]{CalligraphModern2, DroidSansMono, BuxtonSketch};
        int[] sizes = new int[]{small, p, h1, h2, h3, h4, h5};

        map = new HashMap<>(fonts.length);

        for (String font : fonts) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(font));

            HashMap<Integer, BitmapFont> variants = new HashMap<>(sizes.length);

            for (int size : sizes) {
                FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                parameter.characters = alphabet;
                parameter.size = (int) (size * game.params.scaleFactor);

                BitmapFont bitmapFont = generator.generateFont(parameter);

                variants.put(size, bitmapFont);
            }

            generator.dispose();

            map.put(font, variants);
        }
    }

    public void loadHd() {

        Log.i(tag, "loadHd");

        String[] fonts = new String[]{FleischmannGotich, CalligraphModern2};
        int[] sizes = new int[]{hd};

        mapHd = new HashMap<>(fonts.length);

        for (String font : fonts) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(font));

            HashMap<Integer, BitmapFont> variants = new HashMap<>(sizes.length);

            for (int size : sizes) {
                FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                parameter.characters = alphabet;
                parameter.size = size;

                BitmapFont bitmapFont = generator.generateFont(parameter);

                variants.put(size, bitmapFont);
            }

            generator.dispose();

            mapHd.put(font, variants);
        }
    }

    @Override
    public void dispose() {

        Log.i(tag, "dispose");

        for (String font : mapHd.keySet()) {
            HashMap<Integer, BitmapFont> variants = mapHd.get(font);
            for (Integer size : variants.keySet()) {
                variants.get(size).dispose();
            }
            variants.clear();
        }
        mapHd.clear();

        for (String font : map.keySet()) {
            HashMap<Integer, BitmapFont> variants = map.get(font);
            for (Integer size : variants.keySet()) {
                variants.get(size).dispose();
            }
            variants.clear();
        }
        map.clear();
    }
}
