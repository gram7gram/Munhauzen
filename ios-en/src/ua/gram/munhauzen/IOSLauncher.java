package ua.gram.munhauzen;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.pay.ios.apple.PurchaseManageriOSApple;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSData;
import org.robovm.apple.foundation.NSDate;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSErrorException;
import org.robovm.apple.foundation.NSFileManager;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSSearchPathDirectory;
import org.robovm.apple.foundation.NSSearchPathDomainMask;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.foundation.NSUserDefaults;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationDelegate;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIBackgroundFetchResult;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UILocalNotification;
import org.robovm.apple.uikit.UIRemoteNotification;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIUserInterfaceIdiom;
import org.robovm.apple.uikit.UIUserNotificationSettings;
import org.robovm.apple.uikit.UIUserNotificationType;
import org.robovm.apple.usernotifications.UNAuthorizationOptions;
import org.robovm.apple.usernotifications.UNNotification;
import org.robovm.apple.usernotifications.UNNotificationPresentationOptions;
import org.robovm.apple.usernotifications.UNNotificationResponse;
import org.robovm.apple.usernotifications.UNUserNotificationCenter;
import org.robovm.apple.usernotifications.UNUserNotificationCenterDelegate;
import org.robovm.objc.block.VoidBlock1;
import org.robovm.objc.block.VoidBlock2;
import org.robovm.pods.firebase.core.FIRApp;
import org.robovm.pods.firebase.database.FIRDataEventType;
import org.robovm.pods.firebase.database.FIRDataSnapshot;
import org.robovm.pods.firebase.database.FIRDatabase;
import org.robovm.pods.firebase.database.FIRDatabaseReference;
import org.robovm.pods.firebase.messaging.FIRMessaging;
import org.robovm.pods.firebase.messaging.FIRMessagingDelegate;
import org.robovm.pods.firebase.messaging.FIRMessagingRemoteMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.interfaces.OnExpansionDownloadComplete;
import ua.gram.munhauzen.translator.EnglishTranslator;
import ua.gram.munhauzen.utils.AlarmInterface;

public class IOSLauncher extends IOSApplication.Delegate implements FIRMessagingDelegate, UNUserNotificationCenterDelegate, UIApplicationDelegate {

    public static final String KEY_TIME = "key_time";
    public static final String KEY_SAVE_ICON = "key_save_icon";
    public static final String KEY_SAVE_DESCRIPTION = "key_save_description";
    public static final String KEY_NOTIFICATION1_AFTER = "KEY_NOTIFICATION1_AFTER";
    public static final String KEY_NOTIFICATION1_TITLE = "key_notification1_title";
    public static final String KEY_NOTIFICATION1_MESSAGE = "key_notification1_message";
    public static final String KEY_NOTIFICATION2_AFTER = "KEY_NOTIFICATION2_AFTER";
    public static final String KEY_NOTIFICATION2_TITLE = "key_notification2_title";
    public static final String KEY_NOTIFICATION2_MESSAGE = "key_notification2_message";
    public static final String KEY_DEVICE_TOKEN = "key_device_token";

    IOSApplicationConfiguration config;

    UNUserNotificationCenter notificationCenter;

    private boolean needToDownload = true;


    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        System.out.println("createApplication: ");
        try {

            FIRApp.configure();
            System.out.println("didFinishLaunching: Firebase configured");
            if (Foundation.getMajorSystemVersion() >= 10) {
                notificationCenter = UNUserNotificationCenter.currentNotificationCenter();
                UNUserNotificationCenter.currentNotificationCenter().setDelegate(this);
                UNAuthorizationOptions authOptions = UNAuthorizationOptions.with(UNAuthorizationOptions.Alert, UNAuthorizationOptions.Badge, UNAuthorizationOptions.Sound);
                UNUserNotificationCenter.currentNotificationCenter().requestAuthorization(authOptions, new VoidBlock2<Boolean, NSError>() {
                    @Override
                    public void invoke(Boolean aBoolean, NSError nsError) {

                    }
                });
            } else {
                System.out.println("Registers with iOS8+");
                UIUserNotificationType userNotificationTypes = UIUserNotificationType.with(UIUserNotificationType.Alert,
                        UIUserNotificationType.Badge, UIUserNotificationType.Sound);


                UIUserNotificationSettings settings = new UIUserNotificationSettings(userNotificationTypes, null);
                UIApplication.getSharedApplication().registerUserNotificationSettings(settings);
                UIApplication.getSharedApplication().registerForRemoteNotifications();
            }


            //application.registerForRemoteNotifications();

            UIApplication.getSharedApplication().registerForRemoteNotifications();


            FIRMessaging.messaging().setDelegate(this);

            FIRMessaging.messaging().subscribeToTopic("updates");
            FIRMessaging.messaging().subscribeToTopic("ios-all");
            FIRMessaging.messaging().subscribeToTopic("ios-en");

//            showNotificaiton();
            //readStoredData();
            // startAlarm();

            String icon = NSUserDefaults.getStandardUserDefaults().getString(KEY_SAVE_ICON);
            String des = NSUserDefaults.getStandardUserDefaults().getString(KEY_SAVE_DESCRIPTION);


            System.out.println("Icon: " + NSUserDefaults.getStandardUserDefaults().getString(KEY_SAVE_ICON));
            System.out.println("Des: " + NSUserDefaults.getStandardUserDefaults().getString(KEY_SAVE_DESCRIPTION));

            loadFromFirebaseDB();

//            UNNotificationCategory newCategory = UNNotificationCategory("newCategory", )
//            let newCategory = UNNotificationCategory(identifier: "newCategory",
//                    actions: [ action ],
//            minimalActions: [ action ],
//            intentIdentifiers: [],
//            options: [])
//
//            let center = UNUserNotificationCenter.current()
//
//            center.setNotificationCategories([newCategory])


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e);
        }
        return super.didFinishLaunching(application, launchOptions);
    }


    @Override
    protected IOSApplication createApplication() {


//        FIRApp.configure();
//        System.out.println("didFinishLaunching: Firebase configured");
//
//        UNUserNotificationCenter.currentNotificationCenter().setDelegate(this);
//
//        UNAuthorizationOptions authOptions = UNAuthorizationOptions.with(UNAuthorizationOptions.Alert, UNAuthorizationOptions.Badge, UNAuthorizationOptions.Sound);
//        UNUserNotificationCenter.currentNotificationCenter().requestAuthorization(authOptions, new VoidBlock2<Boolean, NSError>() {
//            @Override
//            public void invoke(Boolean aBoolean, NSError nsError) {
//
//            }
//        });
//
//
//        UIApplication.getSharedApplication().registerForRemoteNotifications();
//        //application.registerForRemoteNotifications();
//
//
//        FIRMessaging.messaging().setDelegate(this);
//
//        FIRMessaging.messaging().subscribeToTopic("updates");
//        FIRMessaging.messaging().subscribeToTopic("ios-all");
//        FIRMessaging.messaging().subscribeToTopic("ios-en");


//        NotificationDelegate delegate = new NotificationDelegate();
//        notificationCenter.setDelegate(delegate);
//        delegate.userRequest();
//        delegate.scheduleNotification("test");


        config = new IOSApplicationConfiguration();
        config.orientationPortrait = true;
        config.orientationLandscape = false;
        config.useAccelerometer = false;
        config.useCompass = false;

        PlatformParams params = new PlatformParams();
        params.device.type = isIpad() ? Device.Type.ipad : Device.Type.ios;
        params.isTablet = params.device.type == Device.Type.ipad;
        params.scaleFactor = getDeviceScaleFactor();
        params.storageDirectory = ".Munchausen/en.munchausen.fingertipsandcompany.any";
        params.locale = "en";
        params.appStoreSkuFull = "full_munchausen_audiobook_eng";
        params.appStoreSkuPart1 = "part_1_munchausen_audiobook_eng";
        params.appStoreSkuPart2 = "part_2_munchausen_audiobook_eng";
        params.iap = new PurchaseManageriOSApple();
        params.translator = new EnglishTranslator();
        params.appStore = new AppleStore(params);
        params.release = PlatformParams.Release.PROD;

        params.tutorialLink = "https://youtu.be/xg25QCxlvXM";
        params.fbLink = "https://www.facebook.com/101729401434875/photos/a.101737761434039/147391586868656/?type=3&xts%5B0%5D=68.ARAk1b34nsmLEQ-Qy1jLGgf5M_OS4Eu2bfkwpEyLcDot-rTuQV1p9diUrSyXxTr7FnK5gVC4KP-wxRZK1Ri6Hom0bEoHHn1ECJU8sqPo_tMbqy4LQv1NHNWSvTpnBVQ4DJGkLFyArtPSoRZPc4pp8XDLMNmtr7wN2Q-w4E2m77vbOrD8CyvHVRMs_zTnZbT9qIX3xJbNv4fqabs9CLQIYnK6hMLvkWUe8u1n32gShORJs1cc_sbj9kbDOxFOghMGyBJq9DCTVWxrdyvukwxeVeMCBXdk8f2N5acc-_jUiXeMpT5EBx_GBMEGIl7h_P0mdMUaDECe_LujIqs5uHausB8&tn=-R";
        params.instaLink = "https://www.instagram.com/p/CBdGkEWnj3A/?utm_source=ig_web_copy_link";
        params.twLink = "https://twitter.com/Finger_Tips_C/status/1272495846488264709?s=20";
        params.vkLink = "https://vk.com/wall374290107_10";
        params.statueLink = "https://youtu.be/a_8EMz8gKAE";

        final NSDictionary<NSString, ?> infoDictionary = NSBundle.getMainBundle().getInfoDictionary();
        final Set<NSString> keys = infoDictionary.keySet();

        for (final NSString key : keys) {

            if (key.toString().equals("CFBundleVersion")) {
                final NSObject value = infoDictionary.get(key);
                params.versionCode = Integer.parseInt(value.toString());
            }
            if (key.toString().equals("CFBundleShortVersionString")) {
                final NSObject value = infoDictionary.get(key);
                params.versionName = value.toString();
            }
            if (key.toString().equals("CFBundleIdentifier")) {
                final NSObject value = infoDictionary.get(key);
                params.applicationId = value.toString();
            }
        }

        readNotificationJson();

        // set up when you created the libgdx project
        MunhauzenGame game = new MunhauzenGame(params, new AlarmInterface() {
            @Override
            public void startAlarm() {
                try {
                    showNotificaiton();
                } catch (Exception e) {

                }

            }
        }, new OnExpansionDownloadComplete() {
            @Override
            public void setDownloadNeeded(boolean isDownloaded) {
                needToDownload = isDownloaded;

            }
        });

        // We instantiate the iOS Adapter
        //AdapterIOS adapter = new AdapterIOS();

        //adapter.showNotification("dfsdsf","dfsfdsf");

        // We set the handler, you must create this method in your class


        return new IOSApplication(game, config);

    }

    private float getDeviceScaleFactor() {
        float displayScaleFactor;

        float scale = (float) (getIosVersion() >= 8 ? UIScreen.getMainScreen().getNativeScale() : UIScreen.getMainScreen().getScale());
        if (scale >= 2.0F) {
            if (UIDevice.getCurrentDevice().getUserInterfaceIdiom() == UIUserInterfaceIdiom.Pad) {
                displayScaleFactor = config.displayScaleLargeScreenIfRetina * scale;
            } else {
                displayScaleFactor = config.displayScaleSmallScreenIfRetina * scale;
            }
        } else if (UIDevice.getCurrentDevice().getUserInterfaceIdiom() == UIUserInterfaceIdiom.Pad) {
            displayScaleFactor = config.displayScaleLargeScreenIfNonRetina;
        } else {
            displayScaleFactor = config.displayScaleSmallScreenIfNonRetina;
        }

        if (displayScaleFactor > 2) return 1.5f;
        return 1;
    }

    boolean isIpad() {
        return UIDevice.getCurrentDevice().getModel().toLowerCase().contains("ipad");
    }

    int getIosVersion() {
        String systemVersion = UIDevice.getCurrentDevice().getSystemVersion();
        return Integer.parseInt(systemVersion.split("\\.")[0]);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }


    @Override
    public void didReceiveRegistrationToken(FIRMessaging messaging, String fcmToken) {
        System.out.println("didReceiveRegistrationToken: " + fcmToken);
    }

    @Override
    public void didReceiveMessage(FIRMessaging messaging, FIRMessagingRemoteMessage remoteMessage) {
        System.out.println("didReceiveMessage: " + messaging);
        try {
            showNotificaiton();
        } catch (NSErrorException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void didRegisterForRemoteNotifications(UIApplication application, NSData deviceToken) {
        System.out.println("DeviceToken----------------------> "+deviceToken);
        NSUserDefaults.getStandardUserDefaults().put(KEY_DEVICE_TOKEN, deviceToken);
        super.didRegisterForRemoteNotifications(application, deviceToken);
        FIRMessaging.messaging().setAPNSToken(deviceToken);

    }


    @Override
    public void didFailToRegisterForRemoteNotifications(UIApplication application, NSError error) {
        super.didFailToRegisterForRemoteNotifications(application, error);
        NSUserDefaults.getStandardUserDefaults().put(KEY_DEVICE_TOKEN, error.getLocalizedDescription());
        System.out.println("didFailToRegisterForRemoteNotifications error:" + error);
    }

    @Override
    public void willPresentNotification(UNUserNotificationCenter unUserNotificationCenter, UNNotification unNotification, VoidBlock1<UNNotificationPresentationOptions> completionHandler) {
        readNotificationJson();
        NSDictionary<?, ?> userInfo = unNotification.getRequest().getContent().getUserInfo();

        // With swizzling disabled you must let Messaging know about the message, for Analytics
        FIRMessaging.messaging().appDidReceiveMessage(userInfo);

        if (userInfo.get(gcmMessageIDKey) != null) {
            System.out.println("didReceiveRemoteNotification: MessageID:" + userInfo.get(gcmMessageIDKey));
        }
        System.out.println("didReceiveRemoteNotification: UserInfo:" + userInfo);

        // Change this to your preferred presentation option
        completionHandler.invoke(UNNotificationPresentationOptions.with(UNNotificationPresentationOptions.Alert, UNNotificationPresentationOptions.Sound));

        try {

            showNotificaiton();
        } catch (NSErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void didReceiveNotificationResponse(UNUserNotificationCenter unUserNotificationCenter, UNNotificationResponse unNotificationResponse, Runnable completionHandler) {
        NSDictionary<?, ?> userInfo = unNotificationResponse.getNotification().getRequest().getContent().getUserInfo();
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        FIRMessaging.messaging().appDidReceiveMessage(userInfo);

        if (userInfo.get(gcmMessageIDKey) != null) {
            System.out.println("didReceiveRemoteNotification: MessageID:" + userInfo.get(gcmMessageIDKey));
        }
        System.out.println("didReceiveRemoteNotification: UserInfo:" + userInfo);

        try {
            showNotificaiton();
        } catch (NSErrorException e) {
            e.printStackTrace();
        }

        completionHandler.run();
    }

    @Override
    public void openSettings(UNUserNotificationCenter unUserNotificationCenter, UNNotification unNotification) {

    }


    String gcmMessageIDKey = "gcm.message_id";

    @Override
    public void didReceiveRemoteNotification(UIApplication application, UIRemoteNotification userInfo) {
        super.didReceiveRemoteNotification(application, userInfo);
        if (userInfo.get(gcmMessageIDKey) != null) {
            System.out.println("didReceiveRemoteNotification: MessageID:" + userInfo.get(gcmMessageIDKey));
        }
        System.out.println("didReceiveRemoteNotification: UserInfo:" + userInfo);

        try {
            showNotificaiton();
        } catch (NSErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void didReceiveRemoteNotification(UIApplication application, UIRemoteNotification userInfo, VoidBlock1<UIBackgroundFetchResult> completionHandler) {
        super.didReceiveRemoteNotification(application, userInfo, completionHandler);
        if (userInfo.get(gcmMessageIDKey) != null) {
            System.out.println("didReceiveRemoteNotification: MessageID:" + userInfo.get(gcmMessageIDKey));
        }
        System.out.println("didReceiveRemoteNotification: UserInfo:" + userInfo);

        try {
            showNotificaiton();
        } catch (NSErrorException e) {
            e.printStackTrace();
        }

        completionHandler.invoke(UIBackgroundFetchResult.NewData);
    }

    private void showNotificaiton() throws NSErrorException {
        if (Foundation.getMajorSystemVersion() >= 10) {
            NotificationDelegate delegate = new NotificationDelegate();
            notificationCenter.setDelegate(delegate);
            delegate.userRequest();
            try {
                if (needToDownload){
                    delegate.scheduleDownloadNotification();
                }else {
                    notificationCenter.removeAllPendingNotificationRequests();
                    delegate.scheduleNotification();
                }
            }catch (Exception e){
                System.out.println("Show Notificaiton Error ----------------->"+e);
            }
        } else {
            UILocalNotification notification = new UILocalNotification();
            NSDate date = new NSDate().newDateByAddingTimeInterval(20);
            notification.setFireDate(date);
            notification.setAlertTitle("Hello");
            String token = NSUserDefaults.getStandardUserDefaults().getString(KEY_DEVICE_TOKEN);
            notification.setAlertBody("Body "+token);

//            if let info = userInfo {
//                notification.userInfo = info
//            }

            notification.setSoundName(UILocalNotification.getDefaultSoundName());
            UIApplication.getSharedApplication().scheduleLocalNotification(notification);



        }

    }

    private void readStoredData() {
        java.lang.String saveFile = "save-action.json";
        java.lang.String chapter = "chapters.json";

        //readSaveJsonFile();

        NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();

//        java.lang.String fileURL = "/Users/amar/Library/Developer/CoreSimulator/Devices/70E1D038-FA2F-49BD-B3FD-D58A65859153/data/Containers/Data/Application/333F1A41-9F56-4E8D-AE78-96643681ADDE/Documents/.Munchausen/en.munchausen.fingertipsandcompany.any/save-active.json";
//        /Users/amar/Library/Developer/CoreSimulator/Devices/70E1D038-FA2F-49BD-B3FD-D58A65859153/data/Containers/Bundle/Application/50513BF7-B3DE-453E-AAE0-50FED3235353/IOSLauncher.app/inventory.json
        java.lang.String fileURL = dir.getPath();
        System.out.println("File Path : " + fileURL);


        java.lang.String savedAction = dir.getPath() + "/.Munchausen/en.munchausen.fingertipsandcompany.any/save-active.json";
        String chPath = NSBundle.getMainBundle().findResourcePath("chapters", "json");

        System.out.println("Chapter: Path " + chPath);

        readJsonFile(savedAction);
        String chData = readJsonFile(chPath);
        try {
            JSONArray array = new JSONArray(chData);
            String obj = array.getJSONObject(0).getString("name");
            System.out.println("Name " + obj);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Read Error " + e);
        }


    }


    private void startAlarm() {
        //Saved
        NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
        java.lang.String savedAction = dir.getPath() + "/.Munchausen/en.munchausen.fingertipsandcompany.any/save-active.json";
        String saveJson = readJsonFile(savedAction);


        //Chapter
        String chPath = NSBundle.getMainBundle().findResourcePath("chapters", "json");
        String chapterJson = readJsonFile(chPath);

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

//            SharedPreferencesHelper.setLastVisitedIcon(this,iconPath);
//            SharedPreferencesHelper.setLastVisitedDescription(this, description);


            NSUserDefaults defaults = NSUserDefaults.getStandardUserDefaults();
            defaults.put(KEY_SAVE_ICON, iconPath);
            defaults.put(KEY_SAVE_DESCRIPTION, "Chapter " + chapterNo + ". " + description);

            System.out.println("LastChapterString--->" + lastChapter);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("StartAlarmError: " + e);
        }


//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.SECOND, 20);

//        System.out.println("SetAlarmAfterSeconds--->" + SharedPreferencesHelper.getNotification1Time(this));
//        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ALARM_SET_AFTER_SECONDS",SharedPreferencesHelper.getNotification1Time(this).toString() ).apply();

//        String format = "";
//        try {
//            SimpleDateFormat s = new SimpleDateFormat("hhmmss");
//            format = s.format(new Date());
//        }catch(Exception e){
//            e.printStackTrace();
//        }

        //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ALARM_SET_TIME", format ).apply();


        //showNotificaiton();

//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//        if (alarmManager != null) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//        }


    }


    public static void readNotificationJson() {

        //Notification
        String notificationPath = NSBundle.getMainBundle().findResourcePath("notification_texts", "json");
        String notificationJson = readJsonFile(notificationPath);


        try {
            JSONObject notificationJsonObject = new JSONObject(notificationJson);

            //for Continue notification
            JSONObject continueNotifObject = notificationJsonObject.getJSONObject("continue_notification");

            String continue_notification = continueNotifObject.getString("continue_notification_text_" + (((int) (Math.random() * 7)) + 1));

            //SharedPreferencesHelper.setKeyNotification1Message(getApplicationContext(), continue_notification);


            //for download notification
            JSONObject downloadNotifObject = notificationJsonObject.getJSONObject("download_notification");

            String download_notification = downloadNotifObject.getString("download_notification_text_" + (((int) (Math.random() * 7)) + 1));

            //SharedPreferencesHelper.setKeyNotification2Message(getApplicationContext(), download_notification);


            NSUserDefaults defaults = NSUserDefaults.getStandardUserDefaults();
            defaults.put(KEY_NOTIFICATION1_MESSAGE, continue_notification);
            defaults.put(KEY_NOTIFICATION2_MESSAGE, download_notification);


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("StartAlarmError: " + e);
        }


//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.SECOND, 20);

//        System.out.println("SetAlarmAfterSeconds--->" + SharedPreferencesHelper.getNotification1Time(this));
//        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ALARM_SET_AFTER_SECONDS",SharedPreferencesHelper.getNotification1Time(this).toString() ).apply();

//        String format = "";
//        try {
//            SimpleDateFormat s = new SimpleDateFormat("hhmmss");
//            format = s.format(new Date());
//        }catch(Exception e){
//            e.printStackTrace();
//        }

        //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ALARM_SET_TIME", format ).apply();


        //showNotificaiton();

//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//        if (alarmManager != null) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//        }


    }

    public static String readJsonFile(String fileURL) {
        try {


            NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();

//        java.lang.String fileURL = "/Users/amar/Library/Developer/CoreSimulator/Devices/70E1D038-FA2F-49BD-B3FD-D58A65859153/data/Containers/Data/Application/333F1A41-9F56-4E8D-AE78-96643681ADDE/Documents/.Munchausen/en.munchausen.fingertipsandcompany.any/save-active.json";


            File file = new File(fileURL);
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
//            System.out.println("Readed Json--->" + responce);
            return responce;

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Cached 1 " + ex);
        }

        //System.out.println("filepath--->" + getApplicationContext().getFilesDir());
        return null;

    }

    private void loadFromFirebaseDB() {
        // Write a message to the database
        FIRDatabase database = FIRDatabase.database();

        FIRDatabaseReference notificationsRef = database.reference("1notifications");

        FIRDatabaseReference notificationContinueRef = notificationsRef.child("1notification_to_continue");

        FIRDatabaseReference notificationDownloadRef = notificationsRef.child("2notification_to_download");


        notificationContinueRef.child("1en_hours_notification").observeEvent(FIRDataEventType.Value,
                new VoidBlock1<FIRDataSnapshot>() {
            @Override
            public void invoke(FIRDataSnapshot firDataSnapshot) {
                try {
                    NSNumber value = (NSNumber) firDataSnapshot.getValue();
                    NSUserDefaults.getStandardUserDefaults().put(KEY_NOTIFICATION1_AFTER, value);
                    System.out.println("Continue Hrs------------------------------>"+value);
                } catch (Exception e) {
                    System.out.println("Continue Hrs Error------------------------>"+e);
                }

            }
        });


        notificationContinueRef.child("2en_title_notification").observeEvent(FIRDataEventType.Value,
                new VoidBlock1<FIRDataSnapshot>() {
            @Override
            public void invoke(FIRDataSnapshot firDataSnapshot) {
                try {
                    NSString value = (NSString) firDataSnapshot.getValue();
                    NSUserDefaults.getStandardUserDefaults().put(KEY_NOTIFICATION1_TITLE, value);
                    System.out.println("Notification Title --------------------------> " + value);
                } catch (Exception e) {
                    System.out.println("Continue Title Error------------------------>"+e);
                }

            }
        });

        notificationContinueRef.child("3en_message_notification").observeEvent(FIRDataEventType.Value
                , new VoidBlock1<FIRDataSnapshot>() {
            @Override
            public void invoke(FIRDataSnapshot firDataSnapshot) {
                try {
                    NSString value = (NSString) firDataSnapshot.getValue();
                   // NSUserDefaults.getStandardUserDefaults().put(KEY_NOTIFICATION1_MESSAGE, value);
                    System.out.println("Notification Message ----------------------> " + value);
                } catch (Exception e) {
                    System.out.println("Continue Message Error------------------------>"+e);
                }

            }
        });


        notificationDownloadRef.child("1en_hours_notification").observeEvent(FIRDataEventType.Value,
                new VoidBlock1<FIRDataSnapshot>() {
            @Override
            public void invoke(FIRDataSnapshot firDataSnapshot) {
                try {
                    NSNumber value = (NSNumber) firDataSnapshot.getValue();
                    NSUserDefaults.getStandardUserDefaults().put(KEY_NOTIFICATION2_AFTER, value);
                    System.out.println("Download Hrs------------------------>"+value);
                } catch (Exception e) {
                    System.out.println("Download Hrs Error------------------------>"+e);
                }

            }
        });


        notificationDownloadRef.child("2en_title_notification").observeEvent(FIRDataEventType.Value,
                new VoidBlock1<FIRDataSnapshot>() {
            @Override
            public void invoke(FIRDataSnapshot firDataSnapshot) {
                try {
                    NSString value = (NSString) firDataSnapshot.getValue();
                    NSUserDefaults.getStandardUserDefaults().put(KEY_NOTIFICATION2_TITLE, value);
                    System.out.println("Notification Title -------------------------> " + value);
                } catch (Exception e) {
                    System.out.println("Download Title Error------------------------>"+e);
                }

            }
        });

        notificationDownloadRef.child("3en_message_notification").observeEvent(FIRDataEventType.Value
                , new VoidBlock1<FIRDataSnapshot>() {
            @Override
            public void invoke(FIRDataSnapshot firDataSnapshot) {
                try {
                    NSString value = (NSString) firDataSnapshot.getValue();
                   // NSUserDefaults.getStandardUserDefaults().put(KEY_NOTIFICATION2_MESSAGE, value);
                    System.out.println("Notification Message --------------------------> " + value);
                } catch (Exception e) {
                    System.out.println("Continue Message Error------------------------>"+e);
                }
            }
        });

    }

    @Override
    public void willResignActive(UIApplication application) {
        startAlarm();
        try {
            showNotificaiton();
        } catch (NSErrorException e) {
            e.printStackTrace();
        }
        System.out.println("Resign");
        super.willResignActive(application);
    }

    @Override
    public void willTerminate(UIApplication application) {
        startAlarm();
        try {
            showNotificaiton();
        } catch (NSErrorException e) {
            e.printStackTrace();
        }
        System.out.println("Terminated");
        super.willTerminate(application);
    }


    @Override
    public void didEnterBackground(UIApplication application) {
        startAlarm();
        try {
            showNotificaiton();
        } catch (NSErrorException e) {
            e.printStackTrace();
        }
        System.out.println("Background Entered");
        super.didEnterBackground(application);
    }


}


