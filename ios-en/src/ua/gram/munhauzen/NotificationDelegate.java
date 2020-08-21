package ua.gram.munhauzen;

import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSCalendar;
import org.robovm.apple.foundation.NSCalendarUnit;
import org.robovm.apple.foundation.NSDate;
import org.robovm.apple.foundation.NSDateComponents;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSSet;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.foundation.NSUserDefaults;
import org.robovm.apple.metalps.MPSRayIntersector;
import org.robovm.apple.usernotifications.UNAuthorizationOptions;
import org.robovm.apple.usernotifications.UNCalendarNotificationTrigger;
import org.robovm.apple.usernotifications.UNMutableNotificationContent;
import org.robovm.apple.usernotifications.UNNotification;
import org.robovm.apple.usernotifications.UNNotificationAction;
import org.robovm.apple.usernotifications.UNNotificationActionOptions;
import org.robovm.apple.usernotifications.UNNotificationAttachment;
import org.robovm.apple.usernotifications.UNNotificationCategory;
import org.robovm.apple.usernotifications.UNNotificationCategoryOptions;
import org.robovm.apple.usernotifications.UNNotificationPresentationOptions;
import org.robovm.apple.usernotifications.UNNotificationRequest;
import org.robovm.apple.usernotifications.UNNotificationResponse;
import org.robovm.apple.usernotifications.UNNotificationSound;
import org.robovm.apple.usernotifications.UNTimeIntervalNotificationTrigger;
import org.robovm.apple.usernotifications.UNUserNotificationCenter;
import org.robovm.apple.usernotifications.UNUserNotificationCenterDelegate;
import org.robovm.objc.block.VoidBlock1;
import org.robovm.objc.block.VoidBlock2;
import org.robovm.rt.bro.Bits;

import java.util.Calendar;

public class NotificationDelegate extends NSObject implements UNUserNotificationCenterDelegate {

    UNUserNotificationCenter notificationCenter = UNUserNotificationCenter.currentNotificationCenter();

    void  userRequest(){
        UNAuthorizationOptions options = UNAuthorizationOptions.with(UNAuthorizationOptions.Alert,
                UNAuthorizationOptions.Sound, UNAuthorizationOptions.Badge);

        VoidBlock2<Boolean, NSError> errorVoidBlock2 = new VoidBlock2<Boolean, NSError>() {
            @Override
            public void invoke(Boolean didAllow, NSError nsError) {
                if (!didAllow){
                    System.out.println("User has declined notifications");
                }else {
                    scheduleNotification("Local Notification");
                }
            }
        };
        notificationCenter.requestAuthorization(options,errorVoidBlock2);
    }

    void scheduleNotification(String notificationType){
        UNMutableNotificationContent content = new UNMutableNotificationContent();
        String userActions = "User Actions";

        String icon = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_SAVE_ICON);
        String des = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_SAVE_DESCRIPTION);
        String msg = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_NOTIFICATION1_MESSAGE);
        String title = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_NOTIFICATION1_TITLE);
        int hrs = NSUserDefaults.getStandardUserDefaults().getInt(IOSLauncher.KEY_NOTIFICATION_AFTER);

        NSDictionary<?,?> dictionary = new NSDictionary<>();

        UNNotificationAttachment attachment = null;
        try {
            attachment = new UNNotificationAttachment("image", new NSURL("https://png.pngtree.com/element_our/20190528/ourmid/pngtree-small-url-icon-opened-on-the-computer-image_1132275.jpg"), null);
        }catch (Exception e){

        }

//        let attachement = try! UNNotificationAttachment(identifier: "image",
//                url: Bundle.main.url(forResource: "cat", withExtension: "png")!,
//                options: nil)


        content.setTitle(title);
        content.setBody(msg+" "+des);
        content.setSound(UNNotificationSound.getDefaultSound());
        content.setBadge(NSNumber.valueOf(1));
        content.setCategoryIdentifier(userActions);
        if (attachment!=null){
            NSArray<UNNotificationAttachment> array =new NSArray<UNNotificationAttachment>();
            array.add(attachment);
            content.setAttachments(array);
        }


        NSDate date = new NSDate().newDateByAddingTimeInterval(5);

        NSDateComponents components = NSCalendar.getCurrentCalendar().getComponents(
                NSCalendarUnit.with(NSCalendarUnit.Year,
                        NSCalendarUnit.Month,
                        NSCalendarUnit.Day,
                        NSCalendarUnit.Hour,
                        NSCalendarUnit.Minute,
                        NSCalendarUnit.Second
                ),date);

        UNCalendarNotificationTrigger trig = new UNCalendarNotificationTrigger(components,false);


        UNTimeIntervalNotificationTrigger trigger = new UNTimeIntervalNotificationTrigger(hrs, false);

        String identifier = "Local Notification";
        UNNotificationRequest request = new UNNotificationRequest(identifier, content, trigger);

        notificationCenter.addNotificationRequest(request, new VoidBlock1<NSError>() {
            @Override
            public void invoke(NSError nsError) {
                if (nsError!=null){
                    System.out.println("Error: invoke::"+nsError);
                }
            }
        });

        UNNotificationAction snoozeAction = new UNNotificationAction("Snooze","Snooze", UNNotificationActionOptions.None);
        UNNotificationAction deleteAction = new UNNotificationAction("Delete","Delete", UNNotificationActionOptions.Destructive);
        UNNotificationCategory category = new UNNotificationCategory(userActions,new NSArray<UNNotificationAction>(snoozeAction,deleteAction),new NSArray<NSString>(), UNNotificationCategoryOptions.None);

        notificationCenter.setNotificationCategories(new NSSet<UNNotificationCategory>(category));
    }


    @Override
    public void willPresentNotification(UNUserNotificationCenter unUserNotificationCenter, UNNotification unNotification, VoidBlock1<UNNotificationPresentationOptions> voidBlock1) {
        System.out.println("willPresentNotification");
    }

    @Override
    public void didReceiveNotificationResponse(UNUserNotificationCenter unUserNotificationCenter, UNNotificationResponse unNotificationResponse, Runnable runnable) {
        System.out.println("didReceiveNotificationResponse");
    }

    @Override
    public void openSettings(UNUserNotificationCenter unUserNotificationCenter, UNNotification unNotification) {

    }
}
