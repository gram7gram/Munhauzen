package ua.gram.munhauzen;

import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSCalendar;
import org.robovm.apple.foundation.NSCalendarUnit;
import org.robovm.apple.foundation.NSDate;
import org.robovm.apple.foundation.NSDateComponents;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSErrorException;
import org.robovm.apple.foundation.NSFileManager;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSSearchPathDirectory;
import org.robovm.apple.foundation.NSSearchPathDomainMask;
import org.robovm.apple.foundation.NSSet;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.foundation.NSUserDefaults;
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

import java.net.URL;

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
//                    try {
//                        //scheduleNotification("Local Notification");
//                    } catch (NSErrorException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        };
        notificationCenter.requestAuthorization(options,errorVoidBlock2);
    }

    void scheduleNotification(String notificationType) throws NSErrorException {
        UNMutableNotificationContent content = new UNMutableNotificationContent();
        String userActions = "User Actions";

        String icon = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_SAVE_ICON);
        String des = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_SAVE_DESCRIPTION);
        String msg = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_NOTIFICATION1_MESSAGE);
        String title = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_NOTIFICATION1_TITLE);

        int hrs = NSUserDefaults.getStandardUserDefaults().getInt(IOSLauncher.KEY_NOTIFICATION_AFTER);

        NSDictionary<?,?> dictionary = new NSDictionary<>();


        NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
        NSURL uurl = new NSURL(dir.getPath()+"/.Munchausen/en.munchausen.fingertipsandcompany.any/expansion/chapter/icon_a1.png",new NSURL());


        UNNotificationAttachment attachment = null;


        try {
            System.out.println("Attachment TryEntered--------------->");
//            dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
//
//            String tmpSubFolderName = NSProcessInfo.getSharedProcessInfo().getGloballyUniqueString();
//            NSURL tmpSubFolderURL = new NSURL().newURLByAppendingPathComponent(tmpSubFolderName,false);
//
            String imageURL =  dir.getPath()+"/.Munchausen/en.munchausen.fingertipsandcompany.any/expansion/chapter/icon_a1.png";
//            System.out.println("Img url = "+imageURL);
//
//
            NSDictionary<?,?> dictionary1 = new NSDictionary<>();

            attachment = new UNNotificationAttachment("image", new NSURL("https://miro.medium.com/max/440/1*IDRLEDmc7cacnrQzgclvSw.jpeg"),dictionary1 );
            System.out.println("Attachment------------------->"+attachment);
//            attachment = new UNNotificationAttachment("image", new NSURL(imageURL), dictionary1);

            System.out.println("Direcory---->" + NSBundle.getMainBundle());

//            NSURL url =  NSBundle.getMainBundle().findResourceURL(".Munchausen/en.munchausen.fingertipsandcompany.any/expansion/chapter/icon_a1", ".png");
            NSURL url = new NSURL( NSBundle.getMainBundle()+"/.Munchausen/en.munchausen.fingertipsandcompany.any/expansion/chapter/icon_a1.png");
            System.out.println("NSURL ----------------->"+url);
            URL url1 = new URL(NSBundle.getMainBundle()+"/.Munchausen/en.munchausen.fingertipsandcompany.any/expansion/chapter/icon_a1.png");
//            attachment = new UNNotificationAttachment("image", url1, dictionary);

        }catch (Exception e){
            System.out.println("Attachment Error--------------->"+e);
        }
        NSArray<UNNotificationAction> actions = new NSArray<UNNotificationAction>();


        content.setTitle(title);
        content.setBody(msg+" "+des);
        content.setSound(UNNotificationSound.getDefaultSound());
        content.setBadge(NSNumber.valueOf(1));
        content.setCategoryIdentifier(userActions);

        System.out.println("Attachment----------------->"+attachment);
        if (attachment!=null){
            System.out.println("Attachment NotNUll----------------------->");
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

        UNCalendarNotificationTrigger trig = new UNCalendarNotificationTrigger(components,false);;


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
