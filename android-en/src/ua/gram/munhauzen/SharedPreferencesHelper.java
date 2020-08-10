package ua.gram.munhauzen;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Pujan KC on 11/27/2019.
 */
public class SharedPreferencesHelper {

    private static final String KEY_TIME = "key_time";

    private  static SharedPreferences sharedPreferences;

    public static void setTime(Context context, String token){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(KEY_TIME, token).apply();
    }

    public static String getTime(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(KEY_TIME, "");
    }

    public static void setIcon(Context context, String icon){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("CHAPTER_ICON_VALUE", icon).apply();
    }

    public static String getIcon(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("CHAPTER_ICON_VALUE", "");
    }

    public static void setDescription(Context context, String icon){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("CHAPTER_DESCRIPTION_VALUE", icon).apply();
    }

    public static String getDescription(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("CHAPTER_DESCRIPTION_VALUE", "");
    }

}
