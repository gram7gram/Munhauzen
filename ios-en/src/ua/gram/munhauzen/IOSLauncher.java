package ua.gram.munhauzen;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.pay.ios.apple.PurchaseManageriOSApple;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSData;
import org.robovm.apple.foundation.NSDate;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
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
import org.robovm.apple.uikit.UIPopoverArrowDirection;
import org.robovm.apple.uikit.UIPopoverPresentationController;
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
import org.robovm.pods.firebase.storage.FIRStorage;
import org.robovm.pods.firebase.storage.FIRStorageMetadata;
import org.robovm.pods.firebase.storage.FIRStorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.interfaces.DownloadExpansionInteface;
import ua.gram.munhauzen.interfaces.DownloadSuccessFailureListener;
import ua.gram.munhauzen.interfaces.InternetListenterInterface;
import ua.gram.munhauzen.interfaces.LoginInterface;
import ua.gram.munhauzen.interfaces.LoginListener;
import ua.gram.munhauzen.interfaces.OnExpansionDownloadComplete;
import ua.gram.munhauzen.interfaces.OnlineOfflineListenterInterface;
import ua.gram.munhauzen.interfaces.ReferralInterface;
import ua.gram.munhauzen.translator.EnglishTranslator;
import ua.gram.munhauzen.utils.AlarmInterface;
import ua.gram.munhauzen.utils.Log;

public class IOSLauncher extends IOSApplication.Delegate implements FIRMessagingDelegate, UNUserNotificationCenterDelegate, UIApplicationDelegate, FirebaseDownloader {

    public static final String TAG = "IOSLauncher";
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
    public static final String INVITE_LINK = MUNHAUSEN_URL + "/?invitedby=";
    public static final String BUNDLE_ID = "en.munchausen.fingertipsandcompany.full";
    public static final int CHAPTER0_COMPLETED = 1;
    public static final int CHAPTER0_INCOMPLETE = 0;

    public static String USERS = "ztestusers";
    public static String NOTIFICATION = "ztest1notifications";

    public static class FIREBASE_PATHS {
        public static final String LAST_LOGIN_TIME = "last_login_time";
        public static final String HAS_COMPLETED_CHAP_0 = "hasCompletedChap0";
        public static final String REFERRED_CANDIDATES = "referred_candidates";
        public static final String REFERRED_BY = "referred_by";
    }

    private IOSApplicationConfiguration config;

    private UNUserNotificationCenter notificationCenter;

    private boolean needToDownload = false;
    private String mInvitationURL = "";

    private FIRAuth mAuth;

    private FIRStorageReference storage;

    public int audioDownloadCount;
    public int imageDownloadCount;

    public int audiosNextChapterCount;
    public int imagesNextChapterCount;

    public DownloadSuccessFailureListener downloadSuccessFailureListener;

    List<String> audiosCurrentChapter;
    List<String> imagesCurrentChapter;

    List<String> audiosPrevChapter;
    List<String> imagesPrevChapter;

    List<String> audiosNextChapter;
    List<String> imagesNextChapter;

    MunhauzenGame game;

    @Override
    protected IOSApplication createApplication() {
        config = new IOSApplicationConfiguration();
        config.orientationPortrait = true;
        config.orientationLandscape = false;
        config.useAccelerometer = false;
        config.useCompass = false;

        PlatformParams params = new PlatformParams();
        params.appStoreId = "1496752335";
        params.device.type = isIpad() ? Device.Type.ipad : Device.Type.ios;
        params.isTablet = params.device.type == Device.Type.ipad;
        params.scaleFactor = getDeviceScaleFactor();
        params.storageDirectory = ".Munchausen/en.munchausen.fingertipsandcompany.any";
        params.locale = "en";
        params.appStoreSkuFull = "full_munchausen_audiobook_eng";
        params.appStoreSkuPart1 = "part_1_munchausen_audiobook_eng";
        params.appStoreSkuPart2 = "part_2_munchausen_audiobook_eng";
        params.appStoreSku1Chapter = "chapter_1_munchausen_audiobook_en";
        params.appStoreSku3Chapter = "chapter_3_munchausen_audiobook_en";
        params.appStoreSku5Chapter = "chapter_5_munchausen_audiobook_en";
        params.appStoreSku10Chapter = "chapter_10_munchausen_audiobook_en";
        params.appStoreSkuThanks = "thanks_munchausen_audiobook_en";
        params.appStoreSkuFullThanks = "all_munchausen_audiobook_en";
        params.iap = new PurchaseManageriOSApple();
        params.translator = new EnglishTranslator();
        params.appStore = new AppleStore(params);
        params.release = PlatformParams.Release.PROD;

        params.trailerLink = "https://www.youtube.com/watch?v=3dPQIT10ok8&t";
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


        if (params.release == PlatformParams.Release.PROD) {
            USERS = "users";
            NOTIFICATION = "1notifications";
        } else {
            USERS = "ztestusers";
            NOTIFICATION = "ztest1notifications";
        }

        readNotificationJson();

        mAuth = FIRAuth.auth();
        FIRUser user = mAuth.getCurrentUser();

        LoginInterface loginInterface;

        if (user == null) {
            loginInterface = mLoginInterface;
        } else {
            System.out.println("UserID------------------------------>" + mAuth.getCurrentUser().getUid());
            setReferralzz();
            getReferralCount();
            loginInterface = mSetReferalInterface;
        }


        game = new MunhauzenGame(
                params,
                mAlarmInterface,
                mExpansionDownloadInterface,
                loginInterface,
                mReferalInterface,
                mDownloadExpansionInterface,
                new OnlineOfflineListenterInterface() {
                    @Override
                    public void onGameModeChanged(boolean isOnline) {
                        if (!isOnline) {
                            deletePreviousChapterExpansions();
                        }
                    }
                },
                new InternetListenterInterface() {
                    @Override
                    public boolean hasInternet() {
                        return isInternetAvailable();
                    }
                }
        );

        return new IOSApplication(game, config);
    }

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        System.out.println("createApplication: ");

        try {

            FIRApp.configure();

            //firebase Storage
            storage = FIRStorage.storage().referenceForURL("gs://oh-that-munchausen.appspot.com");

            System.out.println("didFinishLaunching: Firebase configured");
            NSUserDefaults.getStandardUserDefaults().put(KEY_REFERRAL_COUNT, 0);
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

            UIApplication.getSharedApplication().registerForRemoteNotifications();


            FIRMessaging.messaging().setDelegate(this);

            FIRMessaging.messaging().subscribeToTopic("updates");
            FIRMessaging.messaging().subscribeToTopic("ios-all");
            FIRMessaging.messaging().subscribeToTopic("ios-en");

            String icon = NSUserDefaults.getStandardUserDefaults().getString(KEY_SAVE_ICON);
            String des = NSUserDefaults.getStandardUserDefaults().getString(KEY_SAVE_DESCRIPTION);

            System.out.println("Icon: " + icon);
            System.out.println("Des: " + des);

            loadFromFirebaseDB();

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
            } catch (Exception e) {
                System.out.println("Applicatio Exception---------------->" + e);
            }
        }
    };

    private OnExpansionDownloadComplete mExpansionDownloadInterface = new OnExpansionDownloadComplete() {
        @Override
        public void setDownloadNeeded(boolean isDownloaded) {
            needToDownload = isDownloaded;
        }
    };

    /**
     * Creates ananomous user
     *
     * @param loginListener login interface from core
     */
    private void loginAnonymouslyz(final LoginListener loginListener) {
        mAuth.signInAnonymously(new VoidBlock2<FIRAuthDataResult, NSError>() {
            @Override
            public void invoke(FIRAuthDataResult firAuthDataResult, NSError nsError) {
                if (nsError == null) {
                    FIRUser user = firAuthDataResult.getUser();
                    if (user == null) {
                        loginListener.isLoggedIn(false);
                        System.out.println("LoginFailed------------------------------>" + nsError);
                        return;
                    }
                    System.out.println("UserID------------------------------>" + mAuth.getCurrentUser().getUid());
                    loginListener.isLoggedIn(true);
                    FIRDatabaseReference userRecord = FIRDatabase.database().reference()
                            .child(USERS).child(user.getUid());
                    userRecord.child(FIREBASE_PATHS.LAST_LOGIN_TIME).setValue(FIRServerValue.timestamp());
                    userRecord.child(FIREBASE_PATHS.HAS_COMPLETED_CHAP_0).setValue(NSNumber.valueOf(CHAPTER0_INCOMPLETE));
                    setReferralzz();
                } else {
                    Log.e(TAG, nsError.getLocalizedFailureReason());
                }
            }
        });


    }

    /**
     * Generates a short referal Link
     *
     * @return referal link
     */
    private String setReferralzz() {
        String uid = mAuth.getCurrentUser().getUid();
        String link = INVITE_LINK + uid;
        FIRDynamicLinkComponents referalLink = new FIRDynamicLinkComponents(new NSURL(link), "https://fingertipsandcompany.page.link");

        FIRDynamicLinkIOSParameters iOSParameters = new FIRDynamicLinkIOSParameters(BUNDLE_ID);
        iOSParameters.setMinimumAppVersion("1.0.1");
        iOSParameters.setAppStoreID(game.params.appStoreId);
        referalLink.setIOSParameters(iOSParameters);

        FIRDynamicLinkAndroidParameters androidParameters = new FIRDynamicLinkAndroidParameters(BUNDLE_ID);
        androidParameters.setMinimumVersion(125);
        referalLink.setAndroidParameters(androidParameters);


        referalLink.shorten(new VoidBlock3<NSURL, NSArray<NSString>, NSError>() {
            @Override
            public void invoke(NSURL shortURL, NSArray<NSString> nsStrings, NSError nsError) {
                if (nsError != null) {
                    System.out.println("Referal Link Shorter Error-------------------->" + nsError);
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
    private void sendInvitationLink() {
        String invitationLink = mInvitationURL;
        String msg = "Давайте вместе сыграем в Мюнхгаузена! Используйте мою реферерную ссылку: "
                + invitationLink;

        System.out.println(msg);

        NSMutableArray<NSString> array = new NSMutableArray<NSString>();
        array.add(msg);

        UIActivityViewController activityViewController = new UIActivityViewController(array, null);
        UIViewController currentViewController = UIApplication.getSharedApplication().getKeyWindow().getRootViewController();

        if (isIpad()) {
            UIPopoverPresentationController popoverController = activityViewController.getPopoverPresentationController();
            popoverController.setSourceRect(new CGRect(UIScreen.getMainScreen().getBounds().getWidth() / 2, UIScreen.getMainScreen().getBounds().getHeight() / 2, 0, 0));
            popoverController.setSourceView(activityViewController.getView());
            popoverController.setPermittedArrowDirections(new UIPopoverArrowDirection(0));
        }

        currentViewController.presentViewController(activityViewController, true, null);
    }

    /**
     * increase referral count if referral candidates have completed intro chapter
     */
    private void getReferralCount() {
        try {
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
                            NSUserDefaults.getStandardUserDefaults().put(KEY_REFERRAL_COUNT, 0);

                            int array = (int) firDataSnapshot.getChildrenCount();
                            int num = (int) firDataSnapshot.getChildrenCount();

                            System.out.println("Referral count ------------------>" + num);

                            for (FIRDataSnapshot referredCandidates : firDataSnapshot.getChildren().getAllObjects()) {
                                String userID = referredCandidates.getValue().toString();
                                FIRDatabaseReference hasCompetedRef = userRef
                                        .child(userID)
                                        .child(FIREBASE_PATHS.HAS_COMPLETED_CHAP_0);
                                hasCompetedRef.observeSingleEvent(FIRDataEventType.Value,
                                        new VoidBlock1<FIRDataSnapshot>() {
                                            @Override
                                            public void invoke(FIRDataSnapshot firDataSnapshot) {
                                                try {
                                                    NSNumber nsNumber = (NSNumber) firDataSnapshot.getValue();
                                                    int isChap0Completed = nsNumber.intValue();
                                                    int currentCount = NSUserDefaults.getStandardUserDefaults().getInt(KEY_REFERRAL_COUNT);
                                                    System.out.println("CurrentCount------------------------>" + currentCount);
                                                    if (isChap0Completed == CHAPTER0_COMPLETED) {
                                                        NSUserDefaults.getStandardUserDefaults().put(KEY_REFERRAL_COUNT, currentCount + 1);
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println("Get Chap0 completed Error ----------------->" + e);
                                                }
                                            }
                                        });
                            }

                        }
                    });

        } catch (Exception e) {
            System.out.println("getReferalCount Error ------------------>" + e);
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
            System.out.println("CurrentCount Updated------------------------>" + count);
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

    private boolean handleDynamicLink(FIRDynamicLink dynamicLink) {
        if (dynamicLink == null) {
            return false;
        }
        NSURL deepLink = dynamicLink.getUrl();
        if (deepLink == null) {
            return false;
        }

        System.out.println("DynaimcLink------------------->" + deepLink);
        NSArray<NSURLQueryItem> queryItems = new NSURLComponents(deepLink, true)
                .getQueryItems();

        NSArray<NSURLQueryItem> filteredList = new NSArray<NSURLQueryItem>();

        for (NSURLQueryItem item : queryItems) {
            if (item.getName().equals("invitedby")) {
                filteredList.add(item);
            }
        }

        final String invitedBy = filteredList.first().getValue();

        final FIRUser user = FIRAuth.auth().getCurrentUser();

        if (user == null && invitedBy != null) {
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

                                    if (dataSnapshot.exists()) {
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
        if (isDynamicLink) {
            FIRDynamicLink firDynamicLink = FIRDynamicLinks.dynamicLinks().dynamicLinkFromCustomSchemeURL(url);
            return handleDynamicLink(firDynamicLink);
        }
        return false;
    }

    private DownloadExpansionInteface mDownloadExpansionInterface = new DownloadExpansionInteface() {
        @Override
        public void downloadExpansionAndDeletePrev(String currentChapterName, DownloadSuccessFailureListener downloadSuccessFailureListener) {
            try {
                downloadExpansionFile(currentChapterName, downloadSuccessFailureListener);
            } catch (IOException e) {
                downloadSuccessFailureListener.onFailure();
            }
        }

        @Override
        public boolean downloadGoof(String goofName, DownloadSuccessFailureListener downloadSuccessFailureListener1) {

            return downloadGoofAudio(goofName, downloadSuccessFailureListener1);
        }

        @Override
        public boolean downloadGallery(String imageName, DownloadSuccessFailureListener downloadSuccessFailureListener2) {
            return downloadGalleryImage(imageName, downloadSuccessFailureListener2);
        }

        @Override
        public boolean isInternetAvailable() {
            return IOSLauncher.this.isInternetAvailable();
        }
    };

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

    private String readScenarioJsonFile() {
        String scenarioPath = NSBundle.getMainBundle().findResourcePath("scenario", "json");
        return readJsonFile(scenarioPath);

    }

    private String readAudioJsonFile() {
        String audioPath = NSBundle.getMainBundle().findResourcePath("audio", "json");
        return readJsonFile(audioPath);
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
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void didRegisterForRemoteNotifications(UIApplication application, NSData deviceToken) {
        System.out.println("DeviceToken----------------------> " + deviceToken);
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
        } catch (Throwable e) {
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
        } catch (Throwable e) {
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
        } catch (Throwable e) {
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
        } catch (Throwable e) {
            e.printStackTrace();
        }

        completionHandler.invoke(UIBackgroundFetchResult.NewData);
    }

    private void showNotificaiton() {
        if (Foundation.getMajorSystemVersion() >= 10) {
            try {
                NotificationDelegate delegate = new NotificationDelegate();
                notificationCenter.setDelegate(delegate);
                delegate.userRequest();
                notificationCenter.removeAllPendingNotificationRequests();
                readNotificationJson();
                if (needToDownload) {
                    delegate.scheduleDownloadNotification();
                } else {
                    getLastChapter();
                    delegate.scheduleNotification();
                }
            } catch (Exception e) {
                System.out.println("Show Notificaiton Error ----------------->" + e);
            }
        } else {
            String msg = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_NOTIFICATION2_MESSAGE);
            String title = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_NOTIFICATION2_TITLE);
            UILocalNotification notification = new UILocalNotification();
            NSDate date = new NSDate().newDateByAddingTimeInterval(60);
            notification.setFireDate(date);
            notification.setAlertTitle(title);
            notification.setAlertBody(msg);
            notification.setSoundName(UILocalNotification.getDefaultSoundName());
            UIApplication.getSharedApplication().scheduleLocalNotification(notification);


        }

    }

    private void getLastChapter() {
        //Saved

        try {
            NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
            java.lang.String savedAction = dir.getPath() + "/" + game.params.storageDirectory + "/save-active.json";
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
            defaults.put(KEY_SAVE_DESCRIPTION, "Chapter " + chapterNo + ". " + description);

            System.out.println("LastChapterString--->" + lastChapter);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("StartAlarmError: " + e);
        } catch (Exception e) {
            System.out.println("StartAlarmError generalized exception -----------------------------> " + e);
        }


    }

    public static void readNotificationJson() {


        try {
            //Notification
            String notificationPath = NSBundle.getMainBundle().findResourcePath("notification_texts", "json");
            String notificationJson = readJsonFile(notificationPath);
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
        } catch (Exception e) {
            e.printStackTrace();
        }


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

        FIRDatabaseReference notificationsRef = database.reference(NOTIFICATION);

        FIRDatabaseReference notificationContinueRef = notificationsRef.child("1notification_to_continue");

        FIRDatabaseReference notificationDownloadRef = notificationsRef.child("2notification_to_download");


        notificationContinueRef.child("1en_hours_notification").observeEvent(FIRDataEventType.Value,
                new VoidBlock1<FIRDataSnapshot>() {
                    @Override
                    public void invoke(FIRDataSnapshot firDataSnapshot) {
                        try {
                            NSNumber value = (NSNumber) firDataSnapshot.getValue();
                            NSUserDefaults.getStandardUserDefaults().put(KEY_NOTIFICATION1_AFTER, value);
                            System.out.println("Continue Hrs------------------------------>" + value);
                        } catch (Exception e) {
                            System.out.println("Continue Hrs Error------------------------>" + e);
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
                            System.out.println("Continue Title Error------------------------>" + e);
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
                            System.out.println("Continue Message Error------------------------>" + e);
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
                            System.out.println("Download Hrs------------------------>" + value);
                        } catch (Exception e) {
                            System.out.println("Download Hrs Error------------------------>" + e);
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
                            System.out.println("Download Title Error------------------------>" + e);
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
                            System.out.println("Continue Message Error------------------------>" + e);
                        }
                    }
                });

    }

    @Override
    public void willTerminate(UIApplication application) {

        try {
            showNotificaiton();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("Terminated");
        super.willTerminate(application);
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void didEnterBackground(UIApplication application) {

        try {
            showNotificaiton();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("Background Entered");
        super.didEnterBackground(application);
    }

    private void downloadExpansionFile(String currentChapterName, DownloadSuccessFailureListener downloadSuccessFailureListener) throws IOException {

        this.downloadSuccessFailureListener = downloadSuccessFailureListener;
        if (isInternetAvailable()) {
            extractInfoFromScenarioJsonAndPerformDeleteDownload(currentChapterName);
        } else {
            downloadSuccessFailureListener.onFailure();
        }


    }

    public void extractInfoFromScenarioJsonAndPerformDeleteDownload(String currentChapterName) {


        audioDownloadCount = 0;
        imageDownloadCount = 0;

        audiosCurrentChapter = getAudioFilesFromChapter(currentChapterName);
        imagesCurrentChapter = getImageFilesFromChapter(currentChapterName);

        audiosPrevChapter = new ArrayList<>();
        imagesPrevChapter = new ArrayList<>();

        audiosNextChapter = new ArrayList<>();
        imagesNextChapter = new ArrayList<>();

        //get previous and next chapter
        String previousChapter = "";
        String nextChapter = "";
        try {

            //Chapter
            String chPath = NSBundle.getMainBundle().findResourcePath("chapters", "json");
            String chapterJson = readJsonFile(chPath);

            JSONArray chapters = new JSONArray(chapterJson);

            for (int m = 0; m < chapters.length(); m++) {

                JSONObject jsonObject = chapters.getJSONObject(m);
                if (currentChapterName.equals(jsonObject.getString("name"))) {
                    int currentChapNumber = jsonObject.getInt("number");

                    for (int n = 0; n < chapters.length(); n++) {
                        JSONObject jsonObject1 = chapters.getJSONObject(n);

                        if (jsonObject1.getInt("number") == currentChapNumber - 1) {
                            previousChapter = jsonObject1.getString("name");
                            System.out.println("Prevchap--->" + previousChapter);
                        } else if (jsonObject1.getInt("number") == currentChapNumber + 1) {
                            nextChapter = jsonObject1.getString("name");
                            System.out.println("NextChap---श>" + nextChapter);
                        }

                    }

                    //System.out.println("next--->" + nextChapter);

                    break;
                }


            }


            audiosPrevChapter = getAudioFilesFromChapter(previousChapter);
            imagesPrevChapter = getImageFilesFromChapter(previousChapter);

            audiosNextChapter = getAudioFilesFromChapter(nextChapter);
            imagesNextChapter = getImageFilesFromChapter(nextChapter);


            System.out.println("ddf");


            System.out.println("Intermission");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            //download audioFilesForAChapter

            audiosNextChapterCount = audiosNextChapter.size();
            for (String audioPath : audiosNextChapter) {
                downloadChapterAudioFileFromCloud(audioPath);
            }


            //download imageFilesForAChapter
            imagesNextChapterCount = imagesNextChapter.size();
            for (String imagePath : imagesNextChapter) {
                downloadChapterImageFileFromCloud(imagePath);
            }

            game.firebaseManager.downloadInteraction(currentChapterName, this);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void downloadChapterAudioFileFromCloud(String filePath) {
        downloadChapterAudioFileFromCloud(filePath, true);
    }

    @Override
    public void downloadChapterAudioFileFromCloud(final String filePath, final boolean cleanup) {
        FIRStorageReference storageRef = storage;

        storageRef.getStorage().setMaxDownloadRetryTime(1000);

        final FIRStorageReference audioRef = storageRef.child("Expansion Files for online Munchausen/AUDIO_FINAL/Part_English/" + filePath);

        //Download to a local file

        NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
        File storagePath = new File(dir.getPath() + "/" + game.params.storageDirectory + "/expansion/");

        // Create direcorty if not exists
        if (!storagePath.exists()) {
            storagePath.mkdirs();
        }

        final File localFile = new File(storagePath, filePath + ".tmp");
        final File finalFile = new File(storagePath, filePath);

        final NSURL localURL = new NSURL(localFile);

        final Runnable onSuccess = new Runnable() {
            @Override
            public void run() {
                audioDownloadCount++;
                System.out.println("Success downloading from cloud: " + filePath + (cleanup ? ". Cleanup!" : ""));
                System.out.println("AudioDownloadCount--->" + audioDownloadCount + "/" + audiosNextChapterCount);
                System.out.println("ImageDownloadCount--->" + imageDownloadCount + "/" + imagesNextChapterCount);

                try {
                    localFile.renameTo(finalFile);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                if (cleanup) {
                    if (audioDownloadCount >= audiosNextChapterCount && imageDownloadCount >= imagesNextChapterCount) {
                        downloadSuccessFailureListener.onSuccess();
                        deletePreviousChapterExpansions();
                    }
                }
            }
        };

        final Runnable onFailure = new Runnable() {
            @Override
            public void run() {
                System.err.println("FAILURe downloading from cloud: " + filePath);
                System.out.println("AudioDownloadCount--->" + audioDownloadCount + "/" + audiosNextChapterCount);
                System.out.println("ImageDownloadCount--->" + imageDownloadCount + "/" + imagesNextChapterCount);
                downloadSuccessFailureListener.onFailure();
            }
        };

        if (!finalFile.exists()) {
            audioRef.writeToFile(localURL, new VoidBlock2<NSURL, NSError>() {
                @Override
                public void invoke(NSURL nsurl, NSError nsError) {
                    if (nsError == null) {
                        onSuccess.run();
                    } else {
                        onFailure.run();
                    }
                }
            });

        } else {

            final long totalSpaceInLocal = localFile.length();

            audioRef.metadata(new VoidBlock2<FIRStorageMetadata, NSError>() {
                @Override
                public void invoke(FIRStorageMetadata storageMetadata, NSError nsError) {
                    if (nsError == null) {
                        long totalByteCountedInCloud = storageMetadata.getSize();

                        System.out.println("totalByteCounted");

                        if (totalSpaceInLocal == totalByteCountedInCloud) {
                            System.out.println("Already full file on local");
                            onSuccess.run();
                        } else {
                            //download again

                            audioRef.writeToFile(localURL, new VoidBlock2<NSURL, NSError>() {
                                @Override
                                public void invoke(NSURL nsurl, NSError nsError) {
                                    if (nsError == null) {
                                        onSuccess.run();

                                    } else {
                                        onFailure.run();
                                    }

                                }
                            });
                            //download again ends
                        }


                    } else {
                        downloadSuccessFailureListener.onFailure();
                    }
                }
            });

        }
    }

    public void downloadChapterImageFileFromCloud(String filePath) {
        downloadChapterImageFileFromCloud(filePath, true);
    }

    @Override
    public void downloadChapterImageFileFromCloud(final String filePath, final boolean cleanup) {
        String PICTURES_DPI = "Pictures_Hdpi/";

        FIRStorageReference storageRef = storage;

        storageRef.getStorage().setMaxDownloadRetryTime(1000);

        final FIRStorageReference imageRef = storageRef.child("Expansion Files for online Munchausen/" + PICTURES_DPI + filePath + ".jpg");

        //Download to a local file
        NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
        File storagePath = new File(dir.getPath() + "/" + game.params.storageDirectory + "/expansion/images/");
        // Create direcorty if not exists
        if (!storagePath.exists()) {
            storagePath.mkdirs();
        }

        // Save to tmp file and mv to final file on complete
        final File localFile = new File(storagePath, filePath + ".jpg.tmp");
        final File finalFile = new File(storagePath, filePath + ".jpg");

        final NSURL localURL = new NSURL(localFile);

        final Runnable onSuccess = new Runnable() {
            @Override
            public void run() {
                imageDownloadCount++;
                System.out.println("Success downloading from cloud: " + filePath + (cleanup ? ". Cleanup!" : ""));
                System.out.println("AudioDownloadCount--->" + audioDownloadCount + "/" + audiosNextChapterCount);
                System.out.println("ImageDownloadCount--->" + imageDownloadCount + "/" + imagesNextChapterCount);

                try {
                    localFile.renameTo(finalFile);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                if (cleanup) {
                    if (audioDownloadCount >= audiosNextChapterCount && imageDownloadCount >= imagesNextChapterCount) {
                        downloadSuccessFailureListener.onSuccess();
                        deletePreviousChapterExpansions();
                    }
                }
            }
        };

        final Runnable onFailure = new Runnable() {
            @Override
            public void run() {
                System.err.println("FAILURe downloading from cloud: " + filePath);
                System.out.println("AudioDownloadCount--->" + audioDownloadCount + "/" + audiosNextChapterCount);
                System.out.println("ImageDownloadCount--->" + imageDownloadCount + "/" + imagesNextChapterCount);
                downloadSuccessFailureListener.onFailure();
            }
        };

        if (!localFile.exists()) {

            imageRef.writeToFile(localURL, new VoidBlock2<NSURL, NSError>() {
                @Override
                public void invoke(NSURL nsurl, NSError nsError) {
                    if (nsError == null) {
                        onSuccess.run();

                    } else {
                        onFailure.run();
                    }
                }
            });

        } else {

            final long totalSpaceInLocal = localFile.length();

            imageRef.metadata(new VoidBlock2<FIRStorageMetadata, NSError>() {
                @Override
                public void invoke(FIRStorageMetadata storageMetadata, NSError nsError) {
                    if (nsError == null) {
                        long totalByteCountedInCloud = storageMetadata.getSize();

                        if (totalSpaceInLocal == totalByteCountedInCloud) {
                            onSuccess.run();

                        } else {
                            imageRef.writeToFile(localURL, new VoidBlock2<NSURL, NSError>() {
                                @Override
                                public void invoke(NSURL nsurl, NSError nsError) {
                                    if (nsError == null) {
                                        onSuccess.run();
                                    } else {
                                        onFailure.run();
                                    }
                                }
                            });
                        }
                    } else {
                        downloadSuccessFailureListener.onFailure();
                    }
                }
            });

        }

    }

    public List<String> getAudioFilesFromChapter(String chapterName) {


        try {

            String scenarioJson = readScenarioJsonFile();

            JSONArray scenarios = new JSONArray(scenarioJson);

            List<String> audios = new ArrayList<>();
            //List<String> images = new ArrayList<>();

            for (int i = 0; i < scenarios.length(); i++) {

                JSONObject jsonObject = scenarios.getJSONObject(i);

                try {
                    if (jsonObject.has("chapter") && jsonObject.get("chapter").equals(chapterName)) {

                        JSONArray audioArray = jsonObject.getJSONArray("audio");

                        for (int j = 0; j < audioArray.length(); j++) {

                            audios.add(audioArray.getJSONObject(j).getString("audio"));

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Maybe no value for chapter ");
                }


            }
            System.out.println("Extracted audio and images related to chapter from scenario.json");


            String audioJson = readAudioJsonFile();

            JSONArray audioArray = new JSONArray(audioJson);

            //for converting audio s10_0 to audio/s10_0.aac


            List<String> audioPaths = new ArrayList<>();

            for (String audio : audios) {

                for (int j = 0; j < audioArray.length(); j++) {

                    //audios.add(audioArray.getJSONObject(j).getString("audio"));
                    if (audio.equals(audioArray.getJSONObject(j).getString("name"))) {

                        audioPaths.add(audioArray.getJSONObject(j).getString("file"));
                        break;
                    }

                }

            }

            //for adding chapter title audio

            String chPath = NSBundle.getMainBundle().findResourcePath("chapters", "json");
            String chapterJson = readJsonFile(chPath);

            JSONArray chapters = new JSONArray(chapterJson);

            for (int i = 0; i < chapters.length(); i++) {

                JSONObject jsonObject = chapters.getJSONObject(i);
                if (chapterName.equals(jsonObject.getString("name"))) {
                    audioPaths.add("audio/" + jsonObject.getString("chapterAudio") + ".aac");
                }

            }

            //title audio ends

            return audioPaths;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public List<String> getImageFilesFromChapter(String chapterName) {

        try {

            String scenarioJson = readScenarioJsonFile();

            JSONArray scenarios = new JSONArray(scenarioJson);

            List<String> images = new ArrayList<>();

            for (int i = 0; i < scenarios.length(); i++) {

                JSONObject jsonObject = scenarios.getJSONObject(i);

                try {
                    if (jsonObject.has("chapter") && jsonObject.get("chapter").equals(chapterName)) {


                        JSONArray imageArray = jsonObject.getJSONArray("images");

                        for (int k = 0; k < imageArray.length(); k++) {

                            if (!imageArray.getJSONObject(k).getString("image").equals("Last"))
                                images.add(imageArray.getJSONObject(k).getString("image"));

                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Maybe no value for chapter ");
                }


            }
            System.out.println("Extracted images related to chapter from scenario.json");

            return images;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public void deletePreviousChapterExpansions() {
        System.err.println("deletePreviousChapterExpansions");

        //check if audio files already present and delete
        try {
            NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
            File audioDirectory = new File(dir.getPath() + "/" + game.params.storageDirectory + "/expansion/audio");

            File[] directoryListing = audioDirectory.listFiles();

            if (directoryListing != null) {
                for (File file : directoryListing) {

                    String fileName = "audio/" + file.getName();
                    if (audiosCurrentChapter != null && audiosPrevChapter != null && audiosNextChapter != null) {

                        if (!audiosCurrentChapter.contains(fileName) && audiosPrevChapter.contains(fileName) && !audiosNextChapter.contains(fileName)) {
                            file.delete();
                        }
                    } else if (file.exists()) {
                        file.delete();
                    }


                    //for deleting all other files(making sure only chapters x-1, x and x+1 comes into play)
                    if (audiosCurrentChapter != null && audiosPrevChapter != null && audiosNextChapter != null) {

                        if (!audiosCurrentChapter.contains(fileName) && !audiosPrevChapter.contains(fileName) && !audiosNextChapter.contains(fileName)) {
                            file.delete();
                        }
                    } else if (file.exists()) {
                        file.delete();
                    }
                }
            }

            //*check if audio files already present and delete ends


            //check if image files already present and delete
            File imageDirectory = new File(dir.getPath() + "/" + game.params.storageDirectory + "/expansion/images");

            File[] imgdirectoryListing = imageDirectory.listFiles();

            if (imgdirectoryListing != null) {
                for (File file : imgdirectoryListing) {

                    int iend = file.getName().indexOf(".");

                    String fileName = "";

                    if (iend != 1) {
                        fileName = file.getName().substring(0, iend);
                    }
                    if (imagesCurrentChapter != null && imagesPrevChapter != null && imagesNextChapter != null) {

                        if (!imagesCurrentChapter.contains(fileName) && imagesPrevChapter.contains(fileName) && !imagesNextChapter.contains(fileName)) {
                            file.delete();
                        }
                    } else if (file.exists()) {
                        file.delete();
                    }

                    //for deleting all other files(making sure only chapters x-1, x and x+1 comes into play)
                    if (imagesCurrentChapter != null && imagesPrevChapter != null && imagesNextChapter != null) {

                        if (!imagesCurrentChapter.contains(fileName) && !imagesPrevChapter.contains(fileName) && !imagesNextChapter.contains(fileName)) {
                            file.delete();
                        }
                    } else if (file.exists()) {
                        file.delete();
                    }
                }
            }

            //*check if image files already present and delete ends

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean downloadGoofAudio(String goofName, DownloadSuccessFailureListener downloadSuccessFailureListener1) {

        downloadSuccessFailureListener = downloadSuccessFailureListener1;

        FIRStorageReference storageRef = storage;

        storageRef.getStorage().setMaxDownloadRetryTime(1000);

        String filePath = goofName + ".aac";

        final FIRStorageReference audioRef = storageRef.child("Expansion Files for online Munchausen/AUDIO_FINAL/Fails_Eng/" + filePath);

        //Download to a local file
        NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
        File storagePath = new File(dir.getPath() + "/" + game.params.storageDirectory + "/expansion/audio/");

        System.out.println("ABS Path-------------->" + dir.getAbsoluteString());

        // Create direcorty if not exists
        if (!storagePath.exists()) {
            storagePath.mkdirs();
        }

        final File localFile = new File(storagePath, filePath);

        final NSURL localURL = new NSURL(localFile);

        if (!localFile.exists()) {

            audioRef.writeToFile(localURL, new VoidBlock2<NSURL, NSError>() {
                @Override
                public void invoke(NSURL nsurl, NSError nsError) {
                    if (nsError == null) {
                        downloadSuccessFailureListener.onSuccess();
                    } else {
                        downloadSuccessFailureListener.onFailure();
                    }
                }
            });

        } else {

            final long totalSpaceInLocal = localFile.length();

            audioRef.metadata(new VoidBlock2<FIRStorageMetadata, NSError>() {
                @Override
                public void invoke(FIRStorageMetadata storageMetadata, NSError nsError) {
                    if (nsError == null) {
                        long totalByteCountedInCloud = storageMetadata.getSize();

                        System.out.println("totalByteCounted");

                        if (totalSpaceInLocal == totalByteCountedInCloud) {
                            System.out.println("Already full file on local");
                            downloadSuccessFailureListener.onSuccess();
                        } else {
                            //download again

                            audioRef.writeToFile(localURL, new VoidBlock2<NSURL, NSError>() {
                                @Override
                                public void invoke(NSURL nsurl, NSError nsError) {
                                    if (nsError == null) {
                                        downloadSuccessFailureListener.onSuccess();
                                    } else {
                                        downloadSuccessFailureListener.onFailure();
                                    }
                                }
                            });
                            //download again ends
                        }
                    } else {
                        downloadSuccessFailureListener.onFailure();
                    }
                }
            });

        }

        return true;

    }

    public boolean downloadGalleryImage(String imageName, DownloadSuccessFailureListener downloadSuccessFailureListener2) {

        downloadSuccessFailureListener = downloadSuccessFailureListener2;

        String PICTURES_DPI = "Pictures_Hdpi/";

        if (isInternetAvailable()) {
            FIRStorageReference storageRef = storage;

            storageRef.getStorage().setMaxDownloadRetryTime(1000);

            final FIRStorageReference imageRef = storageRef.child("Expansion Files for online Munchausen/" + PICTURES_DPI + imageName + ".jpg");

            //Download to a local file
            NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
            File storagePath = new File(dir.getPath() + "/" + game.params.storageDirectory + "/expansion/images/");
            // Create direcorty if not exists
            if (!storagePath.exists()) {
                storagePath.mkdirs();
            }

            String filePath = imageName + ".jpg";
            // Create direcorty if not exists
            if (!storagePath.exists()) {
                storagePath.mkdirs();
            }

            final File localFile = new File(storagePath, filePath);
            final NSURL localURL = new NSURL(localFile);

            if (!localFile.exists()) {

                imageRef.writeToFile(localURL, new VoidBlock2<NSURL, NSError>() {
                    @Override
                    public void invoke(NSURL nsurl, NSError nsError) {
                        if (nsError == null) {
                            downloadSuccessFailureListener.onSuccess();
                        } else {
                            downloadSuccessFailureListener.onFailure();
                        }
                    }
                });

            } else {

                final long totalSpaceInLocal = localFile.length();

                imageRef.metadata(new VoidBlock2<FIRStorageMetadata, NSError>() {
                    @Override
                    public void invoke(FIRStorageMetadata storageMetadata, NSError nsError) {
                        long totalByteCountedInCloud = storageMetadata.getSize();

                        System.out.println("totalByteCounted");

                        if (totalSpaceInLocal == totalByteCountedInCloud) {
                            System.out.println("Already full file on local");
                            downloadSuccessFailureListener.onSuccess();
                        } else {
                            //download again

                            imageRef.writeToFile(localURL, new VoidBlock2<NSURL, NSError>() {
                                @Override
                                public void invoke(NSURL nsurl, NSError nsError) {
                                    if (nsError == null) {
                                        downloadSuccessFailureListener.onSuccess();
                                    } else {
                                        downloadSuccessFailureListener.onFailure();
                                    }
                                }
                            });
                            //download again ends
                        }

                    }
                });

            }
        } else {
            downloadSuccessFailureListener.onFailure();
        }

        return true;

    }
}


