package ua.gram.munhauzen;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Pujan KC on 11/27/2019.
 */
public class SharedPreferencesHelper {

    private static final String KEY_TIME = "key_time";
    private static final String KEY_SAVE_ICON = "key_save_icon";
    private static final String KEY_SAVE_DESCRIPTION = "key_save_description";
    private static final String KEY_NOTIFICATION_AFTER = "key_notification_after";
    private static final String KEY_NOTIFICATION1_TITLE = "key_notification1_title";
    private static final String KEY_NOTIFICATION1_MESSAGE = "key_notification1_message";

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

    public static void setLastVisitedIcon(Context context, String icon){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(KEY_SAVE_ICON, icon).apply();
    }

    public static String getLastVisitedIcon(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(KEY_SAVE_ICON, "");
    }

    public static void setLastVisitedDescription(Context context, String icon){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(KEY_SAVE_DESCRIPTION, icon).apply();
    }

    public static String getLastVisitedDescription(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(KEY_SAVE_DESCRIPTION, "");
    }

    public static void setNotification1Time(Context context, Integer time){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(KEY_NOTIFICATION_AFTER, time).apply();
    }

    public static Integer getNotification1Time(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(KEY_NOTIFICATION_AFTER, 24);
    }

    public static void setKeyNotification1Title(Context context, String title){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(KEY_NOTIFICATION1_TITLE, title).apply();
    }

    public static String getKeyNotification1Title(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(KEY_NOTIFICATION1_TITLE, "Get Back to Muncahusen");
    }

    public static void setKeyNotification1Message(Context context, String message){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(KEY_NOTIFICATION1_MESSAGE, message).apply();
    }

    public static String getKeyNotification1Message(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(KEY_NOTIFICATION1_MESSAGE, "Continue your adventure at ");
    }

}
