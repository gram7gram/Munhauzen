package ua.gram.munhauzen;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
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
import java.util.Calendar;

import ru.munchausen.fingertipsandcompany.full.BuildConfig;
import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.interfaces.DownloadExpansionInteface;
import ua.gram.munhauzen.interfaces.DownloadSuccessFailureListener;
import ua.gram.munhauzen.interfaces.LoginInterface;
import ua.gram.munhauzen.interfaces.LoginListener;
import ua.gram.munhauzen.interfaces.OnExpansionDownloadComplete;
import ua.gram.munhauzen.interfaces.ReferralInterface;
import ua.gram.munhauzen.translator.RussianTranslator;

import static android.content.ContentValues.TAG;

public class AndroidLauncher extends AndroidApplication {

    private boolean needToDownload;
    public static boolean needToDownloadStatic=true;

    private FirebaseDatabase database;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    String link;
    String mInvitationUrl;

    public static  String USERS = "ztestusers";
    public static  String NOTIFICATION = "ztest1notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissionManager.grant(this, PermissionManager.PERMISSIONS);



        System.out.println("FCM TOKEN---->" + FirebaseInstanceId.getInstance().getToken());


        try {
            YandexMetrica.activate(getApplicationContext(),
                    YandexMetricaConfig.newConfigBuilder("7b7577d5-a605-465d-8fc7-f4d802d016a9").build());
            YandexMetrica.enableActivityAutoTracking(getApplication());
        } catch (Throwable ignore) {

        }

        FirebaseMessaging.getInstance().subscribeToTopic("updates");
        FirebaseMessaging.getInstance().subscribeToTopic("android-all");
        FirebaseMessaging.getInstance().subscribeToTopic("android-ru");



        //Firebase task 2 addition


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        //Firebase task 2 addition ends


        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        config.useAccelerometer = false;
        config.useCompass = false;
        config.hideStatusBar = true;
        config.useWakelock = true;

        PlatformParams params = new PlatformParams();
        params.device.type = Device.Type.android;
        params.isTablet = isTablet(this);
        params.applicationId = BuildConfig.APPLICATION_ID;
        params.storageDirectory = ".Munchausen/ru.munchausen.fingertipsandcompany.any";
        params.versionCode = BuildConfig.VERSION_CODE;
        params.versionName = BuildConfig.VERSION_NAME;
        params.locale = "ru";
        params.appStoreSkuFull = "full_munchausen_audiobook_ru";
        params.appStoreSkuPart1 = "part_1_munchausen_audiobook_ru";
        params.appStoreSkuPart2 = "part_2_munchausen_audiobook_ru";
        params.appStoreSku1Chapter = "chapter_1_munchausen_audiobook_ru";
        params.appStoreSku3Chapter = "chapter_3_munchausen_audiobook_ru";
        params.appStoreSku5Chapter = "chapter_5_munchausen_audiobook_ru";
        params.appStoreSku10Chapter = "chapter_10_munchausen_audiobook_ru";
        params.appStoreSkuThanks = "thanks_munchausen_audiobook_ru";
        params.appStoreSkuFullThanks = "all_munchausen_audiobook_ru";
        params.iap = new PurchaseManagerGoogleBilling(this);
        params.translator = new RussianTranslator();
        params.memoryUsage = new AndroidMemoryUsage();
        params.appStore = new GooglePlay(params);

        params.trailerLink = "https://www.youtube.com/watch?v=eCAohaztPlQ&t";
        params.tutorialLink = "https://youtu.be/6K__lu7QuLk";
        params.fbLink = "https://www.facebook.com/thebaronmunchausen/photos/a.120269342878170/159930395578731/?type=3&xts%5B0%5D=68.ARDwKU7TTxCgmsz5T6W9Eu2xtm-Hv7chauuJWy_S5CIe9J7J3Rsl8CVsg1jHTERjW2pp3rM-6a5Iup5zR2SmytSr8v5lpnpl3RkXyxE1C6w-x79XqWzx4TRvwZtE_zhEJeIj7lIsceHQe-kBM88Dzz1Z8eLC-r7Z7NbVx7fZdizOFiEobj-hUDluar480Bim6q9hZi3gKs3ul4QUx8vVHZ2IKCoGc_5Vs6qucx07PsvoxnL67qDzxvMjdD7WyCKQaTHwxmrY6delninvJXNdG1lYpE9dP_YrVWO8ES7ZKMo5itape1BZGdURY8ha5jULztsps6zPpMF9725fXyq20AU&tn=-R";
        params.instaLink = "https://www.instagram.com/p/CBdHK7BnYHK/?utm_source=ig_web_copy_link";
        params.twLink = "https://twitter.com/Finger_Tips_C/status/1272495967951097856?s=20";
        params.vkLink = "https://vk.com/wall374290107_9";
        params.statueLink = "https://youtu.be/nxOFXw5Efzo";

        if (BuildConfig.BUILD_TYPE.equals("staging")) {
            params.release = PlatformParams.Release.TEST;
        } else if (BuildConfig.BUILD_TYPE.equals("release")) {
            params.release = PlatformParams.Release.PROD;
        } else {
            params.release = PlatformParams.Release.DEV;
        }

        if (params.release == PlatformParams.Release.PROD){
            USERS = "users";
            NOTIFICATION = "1notifications";
        }else {
            USERS = "ztestusers";
            NOTIFICATION = "ztest1notifications";
        }

        //PermissionManager.grant(this, PermissionManager.PERMISSIONS);

        FirebaseAnalytics.getInstance(this);
        getReferralLink();


        if(user == null) {
            MunhauzenGame game = new MunhauzenGame(params, new OnExpansionDownloadComplete() {
                @Override
                public void setDownloadNeeded(boolean isDownloaded) {
                    needToDownload = isDownloaded;
                    needToDownloadStatic = isDownloaded;
                }
            }, new LoginInterface() {
                @Override
                public void loginAnonymously(LoginListener loginListener) {
                    loginAnonymouslyz(loginListener);
                }
            }, new ReferralInterface() {
                @Override
                public String setReferralLink() {
                    return setReferralzz();
                }

                @Override
                public void sendReferralLink() {
                    AndroidLauncher.this.sendReferralLink();
                }

                @Override
                public void getReferral() {

                }

                @Override
                public int getRefferralCount() {
                    getReferralCount();
                    return SharedPreferencesHelper.getReferralCount(getApplicationContext());
                }

                @Override
                public void setChapter0Completed() {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference userRecord =
                            FirebaseDatabase.getInstance().getReference()
                                    .child(USERS)
                                    .child(user.getUid());

                    userRecord.child("hasCompletedChap0").setValue(1);
                }
            }, new DownloadExpansionInteface() {
                @Override
                public void downloadExpansionAndDeletePrev(String currentChapterName, DownloadSuccessFailureListener downloadSuccessFailureListener) {

                }
            });



            initialize(game, config);

        }else {
            user = mAuth.getCurrentUser();

            MunhauzenGame game = new MunhauzenGame(params, new OnExpansionDownloadComplete() {
                @Override
                public void setDownloadNeeded(boolean isDownloaded) {
                    needToDownload = isDownloaded;
                    needToDownloadStatic = isDownloaded;
                }
            }, new LoginInterface() {
                @Override
                public void loginAnonymously(LoginListener loginListener) {
                    setReferralzz();
                }
            }, new ReferralInterface() {
                @Override
                public String setReferralLink() {
                    return setReferralzz();
                }

                @Override
                public void sendReferralLink() {
                    AndroidLauncher.this.sendReferralLink();
                }

                @Override
                public void getReferral() {

                }

                @Override
                public int getRefferralCount() {
                    getReferralCount();
                    return SharedPreferencesHelper.getReferralCount(getApplicationContext());
                }

                @Override
                public void setChapter0Completed() {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference userRecord =
                            FirebaseDatabase.getInstance().getReference()
                                    .child(USERS)
                                    .child(user.getUid());

                    userRecord.child("hasCompletedChap0").setValue(1);
                }
            }, new DownloadExpansionInteface() {
                @Override
                public void downloadExpansionAndDeletePrev(String currentChapterName, DownloadSuccessFailureListener downloadSuccessFailureListener) {

                }
            });

            initialize(game, config);

        }

        //for checking should download or not


        database = FirebaseDatabase.getInstance();

        getNotificationsInfoFromFirebaseDatabase();

        //for referral count
        getReferralCount();
        //referral count ends


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


    private void getReferralCount(){

        //for referral count

        try {

            DatabaseReference userRef = database.getReference(USERS).child(mAuth.getCurrentUser().getUid());

            DatabaseReference refCanRef = userRef.child("referred_candidates");

            refCanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int referralCount = (int) dataSnapshot.getChildrenCount();

                    DatabaseReference userRef = database.getReference(USERS);

                    SharedPreferencesHelper.setReferralCount(getApplicationContext(), 0);

                    for(DataSnapshot refferedCandidates: dataSnapshot.getChildren()){
                        DatabaseReference hasCompletedRef =  userRef.child(refferedCandidates.getValue(String.class)).child("hasCompletedChap0");
                        hasCompletedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    int isChap0Completed = dataSnapshot.getValue(Integer.class);
                                    int currentCount = SharedPreferencesHelper.getReferralCount(getApplicationContext());
                                    if(isChap0Completed == 1){
                                        SharedPreferencesHelper.setReferralCount(getApplicationContext(), currentCount +1);

                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }


                    //SharedPreferencesHelper.setReferralCount(getApplicationContext(), referralCount);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //referral count ends
    }

    private void startAlarm() {

        try {

            String historyJson = readHistoryJsonFile();
            String saveJson = readSaveJsonFile();

            String chapterJson = readChapterJsonFile();


            System.out.println("CHapetr---Jston---->" + chapterJson);


            try {
                JSONObject saveJsonObject = new JSONObject(saveJson);

                String lastChapter = saveJsonObject.getString("chapter");

                System.out.println("Last Visited CHapter=---->" + lastChapter);

                int index = 0;
                JSONObject selectedJsonObject = null;
                JSONArray chapters = new JSONArray(chapterJson);
                for (int j = 0; j < chapters.length(); j++) {
                    if (lastChapter.equals(chapters.getJSONObject(j).getString("name"))) {
                        JSONObject jsonObject = chapters.getJSONObject(j);

                        index = jsonObject.getInt("number");
                        selectedJsonObject = jsonObject;
                        break;
                    }

                }


                if (selectedJsonObject == null) {
                    return;
                }
                String iconPath = selectedJsonObject.getString("icon");
                String description = selectedJsonObject.getString("description");


                int chapterNo = selectedJsonObject.getInt("number");

                System.out.println("SELECTED_JSONOBJECT" + selectedJsonObject.getString("icon"));

                SharedPreferencesHelper.setLastVisitedIcon(this, iconPath);
                SharedPreferencesHelper.setLastVisitedDescription(this, "\n" +
                        "Глава " + chapterNo + ". " + description);


                System.out.println("LastChapterString--->" + lastChapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            Calendar c = Calendar.getInstance();
            c.add(Calendar.SECOND, SharedPreferencesHelper.getNotification1Time(this));


            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }
        } catch (Throwable ignore) {}
    }

    private void startDownloadAlarm(){

        //For download notification

        String iconPath="chapter/b_full_version_1.png";

        //SharedPreferencesHelper.setLastVisitedIcon(this,iconPath);

        if(needToDownload == true){
            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.SECOND, SharedPreferencesHelper.getNotification2Time(this));

            System.out.println("SetAlarm2AfterSeconds--->" + SharedPreferencesHelper.getNotification2Time(this));


            AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent1 = new Intent(this, AlertReceiver2.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
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
            String dfdlk = ".Munchausen/ru.munchausen.fingertipsandcompany.any/history.json";

            System.out.println("Filedir----->" + getApplicationContext().getFilesDir());
            System.out.println("ExtFilesdir-->" + getExternalFilesDir(""));
            System.out.println("ExternalStorageDirectory--->" + Environment.getExternalStorageDirectory());


            //File file = new File(getExternalFilesDir("").toString(), "my.json");
            File file = new File(Environment.getExternalStorageDirectory() + "/.Munchausen/ru.munchausen.fingertipsandcompany.any", "history.json");
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

            File file = new File(Environment.getExternalStorageDirectory() + "/.Munchausen/ru.munchausen.fingertipsandcompany.any", "save-active.json");
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
        if (ContextCompat.checkSelfPermission(AndroidLauncher.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
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


    public void loginAnonymouslyz(final LoginListener loginListener){
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            user = mAuth.getCurrentUser();
                            //updateUI(user);
                            //initGame();
                            loginListener.isLoggedIn(true);
                            //setReferral();

                            //add to database

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference userRecord =
                                    FirebaseDatabase.getInstance().getReference()
                                            .child(USERS)
                                            .child(user.getUid());

                            userRecord.child("last_login_time").setValue(ServerValue.TIMESTAMP);
                            userRecord.child("hasCompletedChap0").setValue(0);
                            System.out.println("Anonymous Account created with Refferrer info");

                            //ends


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(AndroidLauncher.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            loginListener.isLoggedIn(false);
                        }

                        // ...
                    }
                });
    }


    public String setReferralzz(){

        String uid = mAuth.getCurrentUser().getUid();
        link = "https://thebaronmunchausen.com/?invitedby=" + uid;

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                //.setDynamicLinkDomain("https://fingertipsandcompany.page.link")
                .setDomainUriPrefix("https://fingertipsandcompany.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("ru.munchausen.fingertipsandcompany.full")
                                .setMinimumVersion(125)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("ru.munchausen.fingertipsandcompany.full")
                                .setAppStoreId("1498389554")
                                .setMinimumVersion("1.0.1")
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            mInvitationUrl = task.getResult().getShortLink().toString();

                            System.out.println("ShortDynamicLink---->" + task.getResult().getShortLink());
                            //sendReferralLink();
                        }
                        else{
                            System.out.println("OnFailure---->Message--->" + task.getException().getMessage());
                            System.out.println("OnFailure---->Cause-->" + task.getException().getCause());
                        }
                    }
                });

        return  mInvitationUrl;
    }


    public void sendReferralLink(){
        try {
            String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            String invitationLink = mInvitationUrl.toString();
            String msg = "Давай слушать и играть в Мюнхгаузена вместе! Скачивай по моей рефферальной ссылке: "
                    + invitationLink;

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    msg);

            startActivity(Intent.createChooser(shareIntent, "Поделиться"));
        } catch (Throwable ignore) {}
    }


    public void getReferralLink(){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        //
                        // If the user isn't signed in and the pending Dynamic Link is
                        // an invitation, sign in the user anonymously, and record the
                        // referrer's UID.
                        //
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null
                                && deepLink != null
                                && deepLink.getBooleanQueryParameter("invitedby", false)) {
                            String referrerUid = deepLink.getQueryParameter("invitedby");
                            createAnonymousAccountWithReferrerInfo(referrerUid);
                        }
                    }
                });
    }

    private void createAnonymousAccountWithReferrerInfo(final String referrerUid) {
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Keep track of the referrer in the RTDB. Database calls
                        // will depend on the structure of your app's RTDB.
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference userRecord =
                                FirebaseDatabase.getInstance().getReference()
                                        .child(USERS)
                                        .child(user.getUid());
                        userRecord.child("referred_by").setValue(referrerUid);
                        userRecord.child("last_login_time").setValue(ServerValue.TIMESTAMP);
                        userRecord.child("hasCompletedChap0").setValue(0);
                        System.out.println("Anonymous Account created with Refferrer info");

                        final DatabaseReference referrer = FirebaseDatabase.getInstance().getReference()
                                .child(USERS).child(referrerUid);



                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int index = 0;

                                if(dataSnapshot.exists()){
                                    index = (int) dataSnapshot.getChildrenCount() ;
                                }else{
                                    index = 0;
                                }

                                referrer.child("referred_candidates").child("" + index).setValue(user.getUid());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };

                        referrer.child("referred_candidates").addListenerForSingleValueEvent(valueEventListener);



                    }
                });
    }

    private void getNotificationsInfoFromFirebaseDatabase(){


        DatabaseReference notificationsRef = database.getReference(NOTIFICATION);

        DatabaseReference notificationContinueRef = notificationsRef.child("1notification_to_continue");

        DatabaseReference notificationDownloadRef = notificationsRef.child("2notification_to_download");


        DatabaseReference myRef = notificationContinueRef.child("4ru_hours_notification");


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

        DatabaseReference myRef1 = notificationContinueRef.child("5ru_title_notification");

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

        DatabaseReference myRef2 = notificationContinueRef.child("6ru_message_notification");

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


        DatabaseReference myRef3 = notificationDownloadRef.child("4ru_hours_notification");

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

        DatabaseReference myRef4 = notificationDownloadRef.child("5ru_title_notification");

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

        DatabaseReference myRef5 = notificationDownloadRef.child("6ru_message_notification");

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

    }
}
