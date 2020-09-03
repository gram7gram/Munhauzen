package ua.gram.munhauzen;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import en.munchausen.fingertipsandcompany.full.BuildConfig;
import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.entity.History;
import ua.gram.munhauzen.interfaces.OnExpansionDownloadComplete;
import ua.gram.munhauzen.service.ExpansionDownloadManager;
import ua.gram.munhauzen.translator.EnglishTranslator;
import ua.gram.munhauzen.utils.ExternalFiles;

import static android.content.ContentValues.TAG;

public class AndroidLauncher extends AndroidApplication {

    private static final int READ_EXTERNAL_STORAGE = 0X01;

    private boolean needToDownload;

    public static boolean needToDownloadStatic=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        PermissionManager.grant(this, PermissionManager.PERMISSIONS);



        System.out.println("FCM TOKEN---->" + FirebaseInstanceId.getInstance().getToken());


        try {
            YandexMetrica.activate(getApplicationContext(),
                    YandexMetricaConfig.newConfigBuilder("94888cd9-5fad-46a5-ac7f-eb03504fba4d").build());
            YandexMetrica.enableActivityAutoTracking(getApplication());
        } catch (Throwable ignore) {

        }

        FirebaseMessaging.getInstance().subscribeToTopic("updates");
        FirebaseMessaging.getInstance().subscribeToTopic("android-all");
        FirebaseMessaging.getInstance().subscribeToTopic("android-en");

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        config.useAccelerometer = false;
        config.useCompass = false;
        config.hideStatusBar = true;
        config.useWakelock = true;

        PlatformParams params = new PlatformParams();
        params.device.type = Device.Type.android;
        params.isTablet = isTablet(this);
        params.applicationId = BuildConfig.APPLICATION_ID;
        params.storageDirectory = ".Munchausen/en.munchausen.fingertipsandcompany.any";
        params.versionCode = BuildConfig.VERSION_CODE;
        params.versionName = BuildConfig.VERSION_NAME;
        params.locale = "en";
        params.appStoreSkuFull = "full_munchausen_audiobook_eng";
        params.appStoreSkuPart1 = "part_1_munchausen_audiobook_eng";
        params.appStoreSkuPart2 = "part_2_munchausen_audiobook_eng";
        params.appStoreSku1Chapter = "chapter_1_munchausen_audiobook_eng";
        params.appStoreSku3Chapter = "chapter_3_munchausen_audiobook_eng";
        params.appStoreSku5Chapter = "chapter_5_munchausen_audiobook_eng";
        params.appStoreSku10Chapter = "chapter_10_munchausen_audiobook_eng";
        params.appStoreSkuThanks = "thanks_munchausen_audiobook_eng";
        params.appStoreSkuFullThanks = "all_munchausen_audiobook_eng";
        params.iap = new PurchaseManagerGoogleBilling(this);
        params.translator = new EnglishTranslator();
        params.memoryUsage = new AndroidMemoryUsage();
        params.appStore = new GooglePlay(params);

        params.tutorialLink = "https://youtu.be/xg25QCxlvXM";
        params.fbLink = "https://www.facebook.com/101729401434875/photos/a.101737761434039/147391586868656/?type=3&xts%5B0%5D=68.ARAk1b34nsmLEQ-Qy1jLGgf5M_OS4Eu2bfkwpEyLcDot-rTuQV1p9diUrSyXxTr7FnK5gVC4KP-wxRZK1Ri6Hom0bEoHHn1ECJU8sqPo_tMbqy4LQv1NHNWSvTpnBVQ4DJGkLFyArtPSoRZPc4pp8XDLMNmtr7wN2Q-w4E2m77vbOrD8CyvHVRMs_zTnZbT9qIX3xJbNv4fqabs9CLQIYnK6hMLvkWUe8u1n32gShORJs1cc_sbj9kbDOxFOghMGyBJq9DCTVWxrdyvukwxeVeMCBXdk8f2N5acc-_jUiXeMpT5EBx_GBMEGIl7h_P0mdMUaDECe_LujIqs5uHausB8&tn=-R";
        params.instaLink = "https://www.instagram.com/p/CBdGkEWnj3A/?utm_source=ig_web_copy_link";
        params.twLink = "https://twitter.com/Finger_Tips_C/status/1272495846488264709?s=20";
        params.vkLink = "https://vk.com/wall374290107_10";
        params.statueLink = "https://youtu.be/a_8EMz8gKAE";

        if (BuildConfig.BUILD_TYPE.equals("staging")) {
            params.release = PlatformParams.Release.TEST;
        } else if (BuildConfig.BUILD_TYPE.equals("release")) {
            params.release = PlatformParams.Release.PROD;
        } else {
            params.release = PlatformParams.Release.DEV;
        }


        MunhauzenGame game = new MunhauzenGame(params, new OnExpansionDownloadComplete() {
            @Override
            public void setDownloadNeeded(boolean isDownloaded) {
                needToDownload = isDownloaded;
                needToDownloadStatic = isDownloaded;
            }
        });


        initialize(game, config);


        //for checking should download or not


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("en_hours_notification");

        DatabaseReference notificationsRef = database.getReference("1notifications");

        DatabaseReference notificationContinueRef = notificationsRef.child("1notification_to_continue");

        DatabaseReference notificationDownloadRef = notificationsRef.child("2notification_to_download");


        DatabaseReference myRef = notificationContinueRef.child("1en_hours_notification");


        // Read  Notification time from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    Integer value = dataSnapshot.getValue(Integer.class);
                    Log.d(TAG, "Value is: " + value);
                    System.out.println("Value---->" + value);
                    SharedPreferencesHelper.setNotification1Time(getApplicationContext(), value);
                    System.out.println("NotificationValue--->"+ SharedPreferencesHelper.getNotification1Time(getApplicationContext()) );
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());


            }
        });

        // Read Notification title from the database

        DatabaseReference myRef1 = notificationContinueRef.child("2en_title_notification");

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    System.out.println("Value---->" + value);
                    SharedPreferencesHelper.setKeyNotification1Title(getApplicationContext(), value);
                    System.out.println("NotificationTitle--->"+ SharedPreferencesHelper.getKeyNotification1Title(getApplicationContext()) );
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());


            }
        });

        // Read Notification message from the database

        DatabaseReference myRef2 = notificationContinueRef.child("3en_message_notification");

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    System.out.println("Value---->" + value);
                    //SharedPreferencesHelper.setKeyNotification1Message(getApplicationContext(), value);
                    System.out.println("NotificationMessage--->"+ SharedPreferencesHelper.getKeyNotification1Message(getApplicationContext()) );
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());


            }
        });

        //FOr Download notification


        DatabaseReference myRef3 = notificationDownloadRef.child("1en_hours_notification");

        // Read  Notification time from the database
        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    Integer value = dataSnapshot.getValue(Integer.class);
                    Log.d(TAG, "Value is: " + value);
                    System.out.println("Value---->" + value);
                    SharedPreferencesHelper.setNotification2Time(getApplicationContext(), value);
                    System.out.println("NotificationValue--->"+ SharedPreferencesHelper.getNotification2Time(getApplicationContext()) );
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());


            }
        });

        // Read Notification title from the database

        DatabaseReference myRef4 = notificationDownloadRef.child("2en_title_notification");

        myRef4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    System.out.println("Value---->" + value);
                    SharedPreferencesHelper.setKeyNotification2Title(getApplicationContext(), value);
                    System.out.println("NotificationTitle--->"+ SharedPreferencesHelper.getKeyNotification2Title(getApplicationContext()) );
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());


            }
        });

        // Read Notification message from the database

        DatabaseReference myRef5 = notificationDownloadRef.child("3en_message_notification");

        myRef5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    System.out.println("Value---->" + value);
                    //SharedPreferencesHelper.setKeyNotification2Message(getApplicationContext(), value);
                    System.out.println("NotificationMessage--->"+ SharedPreferencesHelper.getKeyNotification2Message(getApplicationContext()) );
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());


            }
        });

        //for download notification ends


        //for setting random values for notificaitons messages

        String notificationJson = readNotificationJsonFile();

        try{
            JSONObject notificationJsonObject = new JSONObject(notificationJson);

            //for Continue notification
            JSONObject continueNotifObject = notificationJsonObject.getJSONObject("continue_notification");

            String continue_notification = continueNotifObject.getString("continue_notification_text_" + (((int) (Math.random() * 7)) + 1));

            SharedPreferencesHelper.setKeyNotification1Message(getApplicationContext(), continue_notification);


            //for download notification
            JSONObject downloadNotifObject = notificationJsonObject.getJSONObject("download_notification");

            String download_notification = downloadNotifObject.getString("download_notification_text_" + (((int) (Math.random() * 7)) + 1));

            SharedPreferencesHelper.setKeyNotification2Message(getApplicationContext(), download_notification);

        }catch (Exception e){
            e.printStackTrace();
        }

        //for setting random values for notificaitons messages ends
    }


    private void startAlarm() {

        String historyJson = readHistoryJsonFile();
        String saveJson = readSaveJsonFile();

        String chapterJson = readChapterJsonFile();


        System.out.println("CHapetr---Jston---->" + chapterJson);


        try {
            JSONObject saveJsonObject = new JSONObject(saveJson);

            String lastChapter = saveJsonObject.getString("chapter");

            System.out.println("Last Visited CHapter=---->" + lastChapter);

            int index=0;
            JSONObject selectedJsonObject=null;
            JSONArray chapters = new JSONArray(chapterJson);
                for(int j=0;j<chapters.length();j++){
                    if(lastChapter.equals(chapters.getJSONObject(j).getString("name"))){
                        JSONObject jsonObject = chapters.getJSONObject(j);

                            index = jsonObject.getInt("number");
                            selectedJsonObject=jsonObject;
                            break;
                    }

                }


            if (selectedJsonObject == null){
                return;
            }
            String iconPath=selectedJsonObject.getString("icon");
            String description=selectedJsonObject.getString("description");

            int chapterNo = selectedJsonObject.getInt("number");


            System.out.println("SELECTED_JSONOBJECT"+selectedJsonObject.getString("icon"));

            SharedPreferencesHelper.setLastVisitedIcon(this,iconPath);
            SharedPreferencesHelper.setLastVisitedDescription(this, "Chapter " + chapterNo + ". " + description);





            System.out.println("LastChapterString--->" + lastChapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, SharedPreferencesHelper.getNotification1Time(this));

        System.out.println("SetAlarmAfterSeconds--->" + SharedPreferencesHelper.getNotification1Time(this));
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ALARM_SET_AFTER_SECONDS",SharedPreferencesHelper.getNotification1Time(this).toString() ).apply();

        String format = "";
        try {
            SimpleDateFormat s = new SimpleDateFormat("hhmmss");
            format = s.format(new Date());
        }catch(Exception e){
            e.printStackTrace();
        }

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ALARM_SET_TIME", format ).apply();



        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }




    }


    private void startDownloadAlarm(){

        //For download notification

           //String iconPath="chapter/b_full_version_1.png";

            //SharedPreferencesHelper.setLastVisitedIcon(this,iconPath);

            if(needToDownload == true){
                Calendar c1 = Calendar.getInstance();
                c1.add(Calendar.SECOND, SharedPreferencesHelper.getNotification2Time(this));

                System.out.println("SetAlarm2AfterSeconds--->" + SharedPreferencesHelper.getNotification2Time(this));


                AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent1 = new Intent(this, AlertReceiver2.class);
                //intent1.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 2, intent1, 0);
                if (alarmManager1 != null) {
                    alarmManager1.setExact(AlarmManager.RTC_WAKEUP, c1.getTimeInMillis(), pendingIntent1);
                }
            }

        //For download notification ends


    }

    private String readNotificationJsonFile() {
        String json = null;
        try {
            InputStream is = getAssets().open("notification_texts.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    private String readChapterJsonFile() {
        String json = null;
        try {
            InputStream is = getAssets().open("chapters.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }



    private String readHistoryJsonFile() {
        try {
            String dfdlk = ".Munchausen/en.munchausen.fingertipsandcompany.any/history.json";

            System.out.println("Filedir----->" + getApplicationContext().getFilesDir());
            System.out.println("ExtFilesdir-->" + getExternalFilesDir(""));
            System.out.println("ExternalStorageDirectory--->" + Environment.getExternalStorageDirectory());


            //File file = new File(getExternalFilesDir("").toString(), "my.json");
            File file = new File(Environment.getExternalStorageDirectory() + "/.Munchausen/en.munchausen.fingertipsandcompany.any", "history.json");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
// This responce will have Json Format String
            String responce = stringBuilder.toString();
            System.out.println("Readed Json--->" + responce);
            return responce;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //System.out.println("filepath--->" + getApplicationContext().getFilesDir());
        return null;

    }


    private String readSaveJsonFile() {
        try {

            File file = new File(Environment.getExternalStorageDirectory() + "/.Munchausen/en.munchausen.fingertipsandcompany.any", "save-active.json");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
// This responce will have Json Format String
            String responce = stringBuilder.toString();
            System.out.println("Readed Json--->" + responce);
            return responce;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //System.out.println("filepath--->" + getApplicationContext().getFilesDir());
        return null;

    }

    private String readExpansionJsonFile() {
        try {
            PlatformParams platformParams = new PlatformParams();
            String dfdlk = ".Munchausen/en.munchausen.fingertipsandcompany.any/" + platformParams.expansionVersion + "-expansion.json";



            //File file = new File(getExternalFilesDir("").toString(), "my.json");
            File file = new File(Environment.getExternalStorageDirectory() + "/.Munchausen/en.munchausen.fingertipsandcompany.any", platformParams.expansionVersion + "-expansion.json");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
// This responce will have Json Format String
            String responce = stringBuilder.toString();
            System.out.println("Readed Expansion Json--->" + responce);
            return responce;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //System.out.println("filepath--->" + getApplicationContext().getFilesDir());
        return null;

    }


    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        super.startActivity(intent, options);
    }

    @Override
    protected void onDestroy() {
        if (ContextCompat.checkSelfPermission(AndroidLauncher.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            if(!needToDownload == true) {
                startAlarm();
            }else if(needToDownload) {
                startDownloadAlarm();
            }
        }
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        if (ContextCompat.checkSelfPermission(AndroidLauncher.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            if(!needToDownload == true) {
                startAlarm();
            }else if(needToDownload) {
                    startDownloadAlarm();
            }
        }
        super.onStop();
    }

}
