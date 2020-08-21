package ua.gram.munhauzen;

import com.badlogic.gdx.maps.MapLayers;

import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSDate;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSMutableDictionary;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSOperationQueue;
import org.robovm.apple.foundation.NSSet;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSTimeZone;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UILocalNotification;
import org.robovm.apple.uikit.UIRemoteNotificationType;
import org.robovm.apple.uikit.UISceneNotifications;
import org.robovm.apple.uikit.UIUserNotificationSettings;
import org.robovm.apple.uikit.UIUserNotificationType;
import org.robovm.apple.usernotifications.UNMutableNotificationContent;
import org.robovm.apple.usernotifications.UNNotificationAction;
import org.robovm.apple.usernotifications.UNNotificationActionOptions;
import org.robovm.apple.usernotifications.UNNotificationCategory;
import org.robovm.apple.usernotifications.UNNotificationCategoryOptions;
import org.robovm.apple.usernotifications.UNNotificationRequest;
import org.robovm.apple.usernotifications.UNNotificationSound;
import org.robovm.apple.usernotifications.UNTimeIntervalNotificationTrigger;
import org.robovm.apple.usernotifications.UNUserNotificationCenter;
import org.robovm.objc.block.VoidBlock1;

import java.util.Date;


/**
 * Created by leonziyo on 4/29/15.
 */
public class AdapterIOS implements NotificationHandler {

    public AdapterIOS () {
        //Registers notifications, it will ask user if ok to receive notifications from this app, if user selects no then no notifications will be received
//        UIApplication.getSharedApplication().registerUserNotificationSettings(UIUserNotificationSettings.create(UIUserNotificationType.Alert, null));
//        UIApplication.getSharedApplication().registerUserNotificationSettings(UIUserNotificationSettings.create(UIUserNotificationType.Sound, null));
//        UIApplication.getSharedApplication().registerUserNotificationSettings(UIUserNotificationSettings.create(UIUserNotificationType.Badge, null));


        if (Foundation.getMajorSystemVersion() >= 8)
        {
            System.out.println("Registers with iOS8+");
            UIUserNotificationType userNotificationTypes = UIUserNotificationType.with(UIUserNotificationType.Alert,
                    UIUserNotificationType.Badge, UIUserNotificationType.Sound);



            UIUserNotificationSettings settings = new UIUserNotificationSettings(userNotificationTypes, null);
            UIApplication.getSharedApplication().registerUserNotificationSettings(settings);
            UIApplication.getSharedApplication().registerForRemoteNotifications();
        }
        else
        {
            System.out.println("Registers with under iOS 8");
            UIRemoteNotificationType type = UIRemoteNotificationType.with(UIRemoteNotificationType.Alert,
                    UIRemoteNotificationType.Badge, UIRemoteNotificationType.Sound);
            UIApplication.getSharedApplication().registerForRemoteNotificationTypes(type);
        }


        //Removes notifications indicator in app icon, you can do this in a different way
        UIApplication.getSharedApplication().setApplicationIconBadgeNumber(0);
        UIApplication.getSharedApplication().cancelAllLocalNotifications();


    }


    @Override
    public void showNotification(final String title, final String text) {
//        NSOperationQueue.getMainQueue().addOperation(new Runnable() {
//            @Override
//            public void run() {
//                NSDate date = new NSDate();
//                //5 seconds from now
//                NSDate secondsMore = date.newDateByAddingTimeInterval(5);
//
//                UILocalNotification localNotification = new UILocalNotification();
//                localNotification.setFireDate(secondsMore);
//                localNotification.setAlertBody(title);
//                localNotification.setAlertAction(text);
//                localNotification.setTimeZone(NSTimeZone.getDefaultTimeZone());
//                localNotification.setApplicationIconBadgeNumber(UIApplication.getSharedApplication().getApplicationIconBadgeNumber() + 1);
//
//                UIApplication.getSharedApplication().scheduleLocalNotification(localNotification);
//            }
//        });

        UNMutableNotificationContent content = new UNMutableNotificationContent();// Содержимое уведомления

        String userActions = "UserActions";

        content.setTitle( "notificationType");
        content.setBody( "This is example how to create ");
        content.setSound( UNNotificationSound.getDefaultSound());
        content.setBadge(NSNumber.valueOf(1));
        content.setCategoryIdentifier( userActions);

        UNTimeIntervalNotificationTrigger trigger = new UNTimeIntervalNotificationTrigger( 5, false);
        String identifier = "Local Notification";
        UNNotificationRequest request = new UNNotificationRequest(identifier, content, trigger);
        UNUserNotificationCenter notificationCenter = UNUserNotificationCenter.currentNotificationCenter();


        UNNotificationRequest req = new UNNotificationRequest(){

        };
        VoidBlock1<NSError> err = new VoidBlock1<NSError>() {
            @Override
            public void invoke(NSError nsError) {

            }
        };

        notificationCenter.addNotificationRequest(req,err);


        UNNotificationAction snoozeAction = new UNNotificationAction( "Snooze",  "Snooze",  new UNNotificationActionOptions(0));
        UNNotificationAction deleteAction = new UNNotificationAction( "Delete",  "Delete", new UNNotificationActionOptions(2L));

        NSArray<UNNotificationAction> actions = new NSArray<UNNotificationAction>();


        UNNotificationCategory category = new UNNotificationCategory( userActions, actions , new NSArray<NSString>(), new UNNotificationCategoryOptions(0));

        NSSet<UNNotificationCategory> set = new NSSet<>(category);
        notificationCenter.setNotificationCategories(set);

//        try{
//            UIApplication.getSharedApplication().cancelAllLocalNotifications();
//            System.out.println("Cancelled all notifs.");
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
        Date date = new Date();
//        UILocalNotification notification = new UILocalNotification();
//        UISceneNotifications notifications= new UISceneNotifications();
//        UNUserNotificationCenter
//        if(Foundation.getMajorSystemVersion()>= 8 && Foundation.getMinorSystemVersion() >= 2)
//            notification.setAlertTitle("TEST");
//        notification.setAlertBody("TEST");
//        notification.setFireDate(new NSDate(date));
//        notification.setAlertAction("");
//        //  NSMutableDictionary<NSObject, NSObject> dict = new NSMutableDictionary<>();
//        // dict.put("id",NSNumber.valueOf(1));
//        // notification.setUserInfo(dict);
//        notification.setTimeZone(NSTimeZone.getLocalTimeZone());
//        UIApplication.getSharedApplication().scheduleLocalNotification(notification);
        System.out.println("Created notif to be fired at: " + date.getTime());
//        System.out.println(UIApplication.getSharedApplication().getScheduledLocalNotifications());
    }
}