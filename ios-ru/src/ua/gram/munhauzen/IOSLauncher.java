package ua.gram.munhauzen;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.pay.ios.apple.PurchaseManageriOSApple;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSData;
import org.robovm.apple.foundation.NSDate;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSErrorException;
import org.robovm.apple.foundation.NSFileManager;
import org.robovm.apple.foundation.NSMutableArray;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSSearchPathDirectory;
import org.robovm.apple.foundation.NSSearchPathDomainMask;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.foundation.NSURLComponents;
import org.robovm.apple.foundation.NSURLQueryItem;
import org.robovm.apple.foundation.NSUserDefaults;
import org.robovm.apple.uikit.UIActivityViewController;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationDelegate;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIApplicationOpenURLOptions;
import org.robovm.apple.uikit.UIBackgroundFetchResult;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UILocalNotification;
import org.robovm.apple.uikit.UIRemoteNotification;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIUserInterfaceIdiom;
import org.robovm.apple.uikit.UIUserNotificationSettings;
import org.robovm.apple.uikit.UIUserNotificationType;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.usernotifications.UNAuthorizationOptions;
import org.robovm.apple.usernotifications.UNNotification;
import org.robovm.apple.usernotifications.UNNotificationPresentationOptions;
import org.robovm.apple.usernotifications.UNNotificationResponse;
import org.robovm.apple.usernotifications.UNUserNotificationCenter;
import org.robovm.apple.usernotifications.UNUserNotificationCenterDelegate;
import org.robovm.objc.block.VoidBlock1;
import org.robovm.objc.block.VoidBlock2;
import org.robovm.objc.block.VoidBlock3;
import org.robovm.pods.firebase.auth.FIRAuth;
import org.robovm.pods.firebase.auth.FIRAuthDataResult;
import org.robovm.pods.firebase.auth.FIRUser;
import org.robovm.pods.firebase.core.FIRApp;
import org.robovm.pods.firebase.database.FIRDataEventType;
import org.robovm.pods.firebase.database.FIRDataSnapshot;
import org.robovm.pods.firebase.database.FIRDatabase;
import org.robovm.pods.firebase.database.FIRDatabaseReference;
import org.robovm.pods.firebase.database.FIRServerValue;
import org.robovm.pods.firebase.dynamiclinks.FIRDynamicLink;
import org.robovm.pods.firebase.dynamiclinks.FIRDynamicLinkAndroidParameters;
import org.robovm.pods.firebase.dynamiclinks.FIRDynamicLinkComponents;
import org.robovm.pods.firebase.dynamiclinks.FIRDynamicLinkIOSParameters;
import org.robovm.pods.firebase.dynamiclinks.FIRDynamicLinks;
import org.robovm.pods.firebase.messaging.FIRMessaging;
import org.robovm.pods.firebase.messaging.FIRMessagingDelegate;
import org.robovm.pods.firebase.messaging.FIRMessagingRemoteMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Set;

import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.interfaces.LoginInterface;
import ua.gram.munhauzen.interfaces.LoginListener;
import ua.gram.munhauzen.interfaces.OnExpansionDownloadComplete;
import ua.gram.munhauzen.interfaces.ReferralInterface;
import ua.gram.munhauzen.translator.RussianTranslator;
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
    public static final String KEY_REFERRAL_COUNT = "key_referral_count";
    public static final String MUNHAUSEN_URL = "https://thebaronmunchausen.com";
//    public static final String MUNHAUSEN_URL = "https://fingertipsandcompany.page.link/?invitedby=";
    public static final String INVITE_LINK = MUNHAUSEN_URL+"/?invitedby=";
    public static final String BUNDLE_ID = "ru.munchausen.fingertipsandcompany.full";
    public static final int CHAPTER0_COMPLETED = 1;
    public static final int CHAPTER0_INCOMPLETE = 0;


    public static String USERS = "ztestusers";
    public static String NOTIFICATION = "ztest1notifications";

    public class FIREBASE_PATHS{
        //        public static fina
        //        l  String USERS = "users";
//        public static final  String NOTIFICATION = "1notifications";

        public static final  String LAST_LOGIN_TIME = "last_login_time";
        public static final  String HAS_COMPLETED_CHAP_0 = "hasCompletedChap0";
        public static final  String REFERRED_CANDIDATES = "referred_candidates";
        public static final  String REFERRED_BY = "referred_by";
    }

    private IOSApplicationConfiguration config;

    private UNUserNotificationCenter notificationCenter;

    private boolean needToDownload = false;
    public static boolean needToDownloadStatic = false;
    private String mInvitationURL = "";

    private FIRAuth mAuth;


    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        System.out.println("createApplication: ");



        try {

            FIRApp.configure();
            System.out.println("didFinishLaunching: Firebase configured");
            NSUserDefaults.getStandardUserDefaults().put(KEY_REFERRAL_COUNT,0);
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
            FIRMessaging.messaging().subscribeToTopic("ios-ru");

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

    private AlarmInterface mAlarmInterface = new AlarmInterface() {
        @Override
        public void startAlarm() {
            try {
                showNotificaiton();
            }catch (Exception e){
                System.out.println("Applicatio Exception---------------->"+e);
            }
        }
    };

    private OnExpansionDownloadComplete mExpansionDownloadInterface = new OnExpansionDownloadComplete() {
        @Override
        public void setDownloadNeeded(boolean isDownloaded) {
            needToDownload = isDownloaded;
            needToDownloadStatic = isDownloaded;
        }
    };

    /**
     * Creates ananomous user
     * @param loginListener login interface from core
     */
    private void loginAnonymouslyz(final LoginListener loginListener){
        mAuth.signInAnonymously(new VoidBlock2<FIRAuthDataResult, NSError>() {
            @Override
            public void invoke(FIRAuthDataResult firAuthDataResult, NSError nsError) {
                FIRUser user = firAuthDataResult.getUser();
                if (user == null){
                    loginListener.isLoggedIn(false);
                    System.out.println("LoginFailed------------------------------>"+nsError);
                    return;
                }
                System.out.println("UserID------------------------------>"+mAuth.getCurrentUser().getUid());
                loginListener.isLoggedIn(true);
                FIRDatabaseReference userRecord = FIRDatabase.database().reference()
                        .child(USERS).child(user.getUid());
                userRecord.child(FIREBASE_PATHS.LAST_LOGIN_TIME).setValue(FIRServerValue.timestamp());
                userRecord.child(FIREBASE_PATHS.HAS_COMPLETED_CHAP_0).setValue(NSNumber.valueOf(CHAPTER0_INCOMPLETE));
                setReferralzz();
            }
        });


    }

    /**
     * Generates a short referal Link
     * @return referal link
     */
    private String setReferralzz(){
        String uid = mAuth.getCurrentUser().getUid();
        String link = INVITE_LINK+uid;
        FIRDynamicLinkComponents referalLink = new FIRDynamicLinkComponents(new NSURL(link) ,"https://fingertipsandcompany.page.link");

        FIRDynamicLinkIOSParameters iOSParameters = new FIRDynamicLinkIOSParameters(BUNDLE_ID);
        iOSParameters.setMinimumAppVersion("1.0.1");
        iOSParameters.setAppStoreID("1498389554");
        referalLink.setIOSParameters(iOSParameters);

        FIRDynamicLinkAndroidParameters androidParameters = new FIRDynamicLinkAndroidParameters(BUNDLE_ID);
        androidParameters.setMinimumVersion(125);
        referalLink.setAndroidParameters(androidParameters);


        referalLink.shorten(new VoidBlock3<NSURL, NSArray<NSString>, NSError>() {
            @Override
            public void invoke(NSURL shortURL, NSArray<NSString> nsStrings, NSError nsError) {
                if (nsError != null){
                    System.out.println("Referal Link Shorter Error-------------------->"+nsError);
                    return;
                }
                mInvitationURL = shortURL.getAbsoluteString();
                System.out.println("Generated Referral Link ");

            }
        });

        return mInvitationURL;

    }

    /**
     * Sends invitation link through mail
     */
    private void sendInvitationLink(){
        String invitationLink = mInvitationURL;
        String msg = "Let's play Munchausen together! Use my referrer link: "
                + invitationLink;

        System.out.println(msg);

        NSMutableArray<NSString> array = new NSMutableArray<NSString>();
        array.add(msg);

        UIActivityViewController activityViewController = new UIActivityViewController(array,null);
        UIViewController currentViewController = UIApplication.getSharedApplication().getKeyWindow().getRootViewController();
        currentViewController.presentViewController(activityViewController,true,null);


//        if (!MFMailComposeViewController.canSendMail()){
//            System.out.println("Device can't send email");
//            return;
//        }
//
//        MFMailComposeViewController mailer = new MFMailComposeViewController();
//        mailer.setMailComposeDelegate(new MFMailComposeViewControllerDelegate() {
//            @Override
//            public void didFinish(MFMailComposeViewController mfMailComposeViewController, MFMailComposeResult mfMailComposeResult, NSError nsError) {
//
//            }
//        });
//
//        mailer.setSubject("Invitation");
//        mailer.setMessageBody(msg, true);
//        mailer.presentViewController(mailer, true, null);

    }

    /**
     * increase referral count if referral candidates have completed intro chapter
     */
    private void getReferralCount(){
        try{
            FIRDatabaseReference refCanRef = FIRDatabase.database()
                    .reference(USERS)
                    .child(mAuth.getCurrentUser().getUid())
                    .child(FIREBASE_PATHS.REFERRED_CANDIDATES);

            refCanRef.observeEvent(FIRDataEventType.Value,
                    new VoidBlock1<FIRDataSnapshot>() {
                        @Override
                        public void invoke(FIRDataSnapshot firDataSnapshot) {
                            FIRDatabaseReference userRef = FIRDatabase.database().reference()
                                    .child(USERS);
                            //Initially clear referral count
                            NSUserDefaults.getStandardUserDefaults().put(KEY_REFERRAL_COUNT,0);

                            int array = (int) firDataSnapshot.getChildrenCount();
                            int num = (int) firDataSnapshot.getChildrenCount();

                            System.out.println("Referral count ------------------>"+num);

                            for (FIRDataSnapshot referredCandidates: firDataSnapshot.getChildren().getAllObjects()){
                                String userID = referredCandidates.getValue().toString();
                                FIRDatabaseReference hasCompetedRef = userRef
                                        .child(userID)
                                        .child(FIREBASE_PATHS.HAS_COMPLETED_CHAP_0);
                                hasCompetedRef.observeSingleEvent(FIRDataEventType.Value,
                                        new VoidBlock1<FIRDataSnapshot>() {
                                            @Override
                                            public void invoke(FIRDataSnapshot firDataSnapshot) {
                                                try{
                                                    NSNumber nsNumber = (NSNumber) firDataSnapshot.getValue();
                                                    int isChap0Completed = nsNumber.intValue();
                                                    int currentCount = NSUserDefaults.getStandardUserDefaults().getInt(KEY_REFERRAL_COUNT);
                                                    System.out.println("CurrentCount------------------------>"+currentCount);
                                                    if (isChap0Completed == CHAPTER0_COMPLETED){
                                                        NSUserDefaults.getStandardUserDefaults().put(KEY_REFERRAL_COUNT, currentCount+1);
                                                    }
                                                }catch (Exception e){
                                                    System.out.println("Get Chap0 completed Error ----------------->"+e);
                                                }
                                            }
                                        });
                            }

                        }
                    });

        }catch (Exception e){
            System.out.println("getReferalCount Error ------------------>"+e);
        }
    }

    private LoginInterface mLoginInterface = new LoginInterface() {
        @Override
        public void loginAnonymously(LoginListener loginListener) {
            loginAnonymouslyz(loginListener);
        }
    };

    private LoginInterface mSetReferalInterface = new LoginInterface() {
        @Override
        public void loginAnonymously(LoginListener loginListener) {
            setReferralzz();
        }
    };

    private ReferralInterface mReferalInterface = new ReferralInterface() {
        @Override
        public String setReferralLink() {
            return setReferralzz();
        }

        @Override
        public void sendReferralLink() {
            sendInvitationLink();
        }

        @Override
        public void getReferral() {

        }

        @Override
        public int getRefferralCount() {
            getReferralCount();
            int count = NSUserDefaults.getStandardUserDefaults().getInt(KEY_REFERRAL_COUNT);
            System.out.println("CurrentCount Updated------------------------>"+count);
            return count;
        }

        @Override
        public void setChapter0Completed() {
            FIRUser user = mAuth.getCurrentUser();
            FIRDatabaseReference userRecord =
                    FIRDatabase.database().reference()
                            .child(USERS)
                            .child(user.getUid());

            userRecord.child(FIREBASE_PATHS.HAS_COMPLETED_CHAP_0)
                    .setValue(NSNumber.valueOf(CHAPTER0_COMPLETED));
        }
    };

    private boolean handleDynamicLink(FIRDynamicLink dynamicLink){
        if (dynamicLink == null){
            return false;
        }
        NSURL deepLink = dynamicLink.getUrl();
        if (deepLink == null){
            return false;
        }

        System.out.println("DynaimcLink------------------->"+deepLink);
        NSArray<NSURLQueryItem> queryItems = new NSURLComponents(deepLink,true)
                .getQueryItems();

        NSArray<NSURLQueryItem> filteredList = new NSArray<NSURLQueryItem>();

        for (NSURLQueryItem item :  queryItems){
            if (item.getName().equals("invitedby")){
                filteredList.add(item);
            }
        }

        final String invitedBy = filteredList.first().getValue();

        final FIRUser user = FIRAuth.auth().getCurrentUser();

        if (user == null && invitedBy!=null){
            FIRAuth.auth().signInAnonymously(new VoidBlock2<FIRAuthDataResult, NSError>() {
                @Override
                public void invoke(FIRAuthDataResult firAuthDataResult, NSError nsError) {
                    FIRDatabaseReference users =
                            FIRDatabase.database().reference()
                                    .child(USERS);

                    FIRDatabaseReference userRecord = users.child(user.getUid());
                    userRecord.child(FIREBASE_PATHS.REFERRED_BY).setValue(new NSString(invitedBy));
                    userRecord.child(FIREBASE_PATHS.LAST_LOGIN_TIME).setValue(FIRServerValue.timestamp());
                    userRecord.child(FIREBASE_PATHS.HAS_COMPLETED_CHAP_0).setValue(NSNumber.valueOf(CHAPTER0_INCOMPLETE));

                    final FIRDatabaseReference referrer = users.child(invitedBy);
                    referrer.child(FIREBASE_PATHS.REFERRED_CANDIDATES)
                            .observeSingleEvent(FIRDataEventType.Value, new VoidBlock1<FIRDataSnapshot>() {
                                @Override
                                public void invoke(FIRDataSnapshot dataSnapshot) {
                                    int index = 0;

                                    if(dataSnapshot.exists()){
                                        index = (int) dataSnapshot.getChildrenCount();
                                    }

                                    referrer.child(FIREBASE_PATHS.REFERRED_CANDIDATES)
                                            .child(String.valueOf(index))
                                            .setValue(new NSString(user.getUid()));
                                }
                            });


                }
            });
        }
        return true;

    }

    @Override
    public boolean openURL(UIApplication app, NSURL url, UIApplicationOpenURLOptions options) {
        boolean isDynamicLink = FIRDynamicLinks.dynamicLinks()
                .shouldHandleDynamicLinkFromCustomSchemeURL(url);
        if (isDynamicLink){
            FIRDynamicLink firDynamicLink = FIRDynamicLinks.dynamicLinks().dynamicLinkFromCustomSchemeURL(url);
            return  handleDynamicLink(firDynamicLink);
        }
        return false;
    }

    @Override
    protected IOSApplication createApplication() {
        config = new IOSApplicationConfiguration();
        config.orientationPortrait = true;
        config.orientationLandscape = false;
        config.useAccelerometer = false;
        config.useCompass = false;

        PlatformParams params = new PlatformParams();
        params.device.type = isIpad() ? Device.Type.ipad : Device.Type.ios;
        params.isTablet = params.device.type == Device.Type.ipad;
        params.scaleFactor = getDeviceScaleFactor();
        params.storageDirectory = ".Munchausen/ru.munchausen.fingertipsandcompany.any";
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
        params.iap = new PurchaseManageriOSApple();
        params.translator = new RussianTranslator();
        params.appStore = new AppleStore(params);
        params.release = PlatformParams.Release.PROD;

        params.trailerLink = "https://www.youtube.com/watch?v=eCAohaztPlQ&t";
        params.tutorialLink = "https://youtu.be/6K__lu7QuLk";
        params.fbLink = "https://www.facebook.com/thebaronmunchausen/photos/a.120269342878170/159930395578731/?type=3&xts%5B0%5D=68.ARDwKU7TTxCgmsz5T6W9Eu2xtm-Hv7chauuJWy_S5CIe9J7J3Rsl8CVsg1jHTERjW2pp3rM-6a5Iup5zR2SmytSr8v5lpnpl3RkXyxE1C6w-x79XqWzx4TRvwZtE_zhEJeIj7lIsceHQe-kBM88Dzz1Z8eLC-r7Z7NbVx7fZdizOFiEobj-hUDluar480Bim6q9hZi3gKs3ul4QUx8vVHZ2IKCoGc_5Vs6qucx07PsvoxnL67qDzxvMjdD7WyCKQaTHwxmrY6delninvJXNdG1lYpE9dP_YrVWO8ES7ZKMo5itape1BZGdURY8ha5jULztsps6zPpMF9725fXyq20AU&tn=-R";
        params.instaLink = "https://www.instagram.com/p/CBdHK7BnYHK/?utm_source=ig_web_copy_link";
        params.twLink = "https://twitter.com/Finger_Tips_C/status/1272495967951097856?s=20";
        params.vkLink = "https://vk.com/wall374290107_9";
        params.statueLink = "https://youtu.be/nxOFXw5Efzo";

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


        if (params.release == PlatformParams.Release.PROD){
            USERS = "users";
            NOTIFICATION = "1notifications";
        }else {
            USERS = "ztestusers";
            NOTIFICATION = "ztest1notifications";
        }

        readNotificationJson();

        mAuth = FIRAuth.auth();
        FIRUser user = mAuth.getCurrentUser();

        LoginInterface loginInterface;

        if (user == null) {
            loginInterface = mLoginInterface;
        }else {
            System.out.println("UserID------------------------------>"+mAuth.getCurrentUser().getUid());
            setReferralzz();
            getReferralCount();
            loginInterface = mSetReferalInterface;
        }


        MunhauzenGame game = new MunhauzenGame(params,
                mAlarmInterface ,
                mExpansionDownloadInterface,
                loginInterface,
                mReferalInterface);
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
        } catch (Exception e){
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
        }catch (Exception e){
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
        } catch (Exception e){
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
        } catch (Exception e){
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
        } catch (Exception e){
            e.printStackTrace();
        }

        completionHandler.invoke(UIBackgroundFetchResult.NewData);
    }

    private void showNotificaiton() throws NSErrorException {
        if (Foundation.getMajorSystemVersion() >= 10) {
            NotificationDelegate delegate = new NotificationDelegate();
            notificationCenter.setDelegate(delegate);
            delegate.userRequest();
            notificationCenter.removeAllPendingNotificationRequests();
            readNotificationJson();
            try {
                if (needToDownload){
                    delegate.scheduleDownloadNotification();
                }else {
                    getLastChapter();
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

//        java.lang.String fileURL = "/Users/amar/Library/Developer/CoreSimulator/Devices/70E1D038-FA2F-49BD-B3FD-D58A65859153/data/Containers/Data/Application/333F1A41-9F56-4E8D-AE78-96643681ADDE/Documents/.Munchausen/ru.munchausen.fingertipsandcompany.any/save-active.json";
//        /Users/amar/Library/Developer/CoreSimulator/Devices/70E1D038-FA2F-49BD-B3FD-D58A65859153/data/Containers/Bundle/Application/50513BF7-B3DE-453E-AAE0-50FED3235353/IOSLauncher.app/inventory.json
        java.lang.String fileURL = dir.getPath();
        System.out.println("File Path : " + fileURL);


        java.lang.String savedAction = dir.getPath() + "/.Munchausen/ru.munchausen.fingertipsandcompany.any/save-active.json";
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


    private void getLastChapter() {
        //Saved

        try {
            NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
            java.lang.String savedAction = dir.getPath() + "/.Munchausen/ru.munchausen.fingertipsandcompany.any/save-active.json";
            String saveJson = readJsonFile(savedAction);


            //Chapter
            String chPath = NSBundle.getMainBundle().findResourcePath("chapters", "json");
            String chapterJson = readJsonFile(chPath);

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
            defaults.put(KEY_SAVE_DESCRIPTION, "Глава " + chapterNo + ". " + description);

            System.out.println("LastChapterString--->" + lastChapter);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("StartAlarmError: " + e);
        } catch (Exception e){
            System.out.println("StartAlarmError generalized exception -----------------------------> "+e);
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
        } catch (Exception e){
            e.printStackTrace();
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

//        java.lang.String fileURL = "/Users/amar/Library/Developer/CoreSimulator/Devices/70E1D038-FA2F-49BD-B3FD-D58A65859153/data/Containers/Data/Application/333F1A41-9F56-4E8D-AE78-96643681ADDE/Documents/.Munchausen/ru.munchausen.fingertipsandcompany.any/save-active.json";


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

        FIRDatabaseReference notificationsRef = database.reference(NOTIFICATION);

        FIRDatabaseReference notificationContinueRef = notificationsRef.child("1notification_to_continue");

        FIRDatabaseReference notificationDownloadRef = notificationsRef.child("2notification_to_download");


        notificationContinueRef.child("4ru_hours_notification").observeEvent(FIRDataEventType.Value,
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


        notificationContinueRef.child("5ru_title_notification").observeEvent(FIRDataEventType.Value,
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

        notificationContinueRef.child("6ru_message_notification").observeEvent(FIRDataEventType.Value
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


        notificationDownloadRef.child("4ru_hours_notification").observeEvent(FIRDataEventType.Value,
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


        notificationDownloadRef.child("5ru_title_notification").observeEvent(FIRDataEventType.Value,
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

        notificationDownloadRef.child("6ru_message_notification").observeEvent(FIRDataEventType.Value
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

//    @Override
//    public void willResignActive(UIApplication application) {
//        startAlarm();
//        try {
//            showNotificaiton();
//        } catch (NSErrorException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Resign");
//        super.willResignActive(application);
//    }

    @Override
    public void willTerminate(UIApplication application) {

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

        try {
            showNotificaiton();
        } catch (NSErrorException e) {
            e.printStackTrace();
        }
        System.out.println("Background Entered");
        super.didEnterBackground(application);
    }


}


