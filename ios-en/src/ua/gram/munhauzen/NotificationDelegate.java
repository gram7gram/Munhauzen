package ua.gram.munhauzen;

import com.badlogic.gdx.math.Path;

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
import org.robovm.apple.foundation.NSMutableArray;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSProcessInfo;
import org.robovm.apple.foundation.NSSearchPathDirectory;
import org.robovm.apple.foundation.NSSearchPathDomainMask;
import org.robovm.apple.foundation.NSSet;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.foundation.NSUserDefaults;
import org.robovm.apple.metalps.MPSRayIntersector;
import org.robovm.apple.mobilecoreservices.UTType;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import ua.gram.munhauzen.utils.Files;
import ua.gram.munhauzen.utils.Log;

public class NotificationDelegate extends NSObject implements UNUserNotificationCenterDelegate {

    UNUserNotificationCenter notificationCenter = UNUserNotificationCenter.currentNotificationCenter();

    void userRequest() {
        UNAuthorizationOptions options = UNAuthorizationOptions.with(UNAuthorizationOptions.Alert,
                UNAuthorizationOptions.Sound, UNAuthorizationOptions.Badge);

        VoidBlock2<Boolean, NSError> errorVoidBlock2 = new VoidBlock2<Boolean, NSError>() {
            @Override
            public void invoke(Boolean didAllow, NSError nsError) {
                if (!didAllow) {
                    System.out.println("User has declined notifications");
                } else {
//                    try {
//                        //scheduleNotification("Local Notification");
//                    } catch (NSErrorException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        };
        notificationCenter.requestAuthorization(options, errorVoidBlock2);
    }

    void scheduleNotification(String notificationType) throws NSErrorException {
        UNMutableNotificationContent content = new UNMutableNotificationContent();
        String userActions = "User Actions";

        String icon = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_SAVE_ICON);
        String des = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_SAVE_DESCRIPTION);
        String msg = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_NOTIFICATION1_MESSAGE);
        String title = NSUserDefaults.getStandardUserDefaults().getString(IOSLauncher.KEY_NOTIFICATION1_TITLE);

        int hrs = NSUserDefaults.getStandardUserDefaults().getInt(IOSLauncher.KEY_NOTIFICATION_AFTER);

        NSDictionary<?, ?> dictionary = new NSDictionary<>();


        NSURL dir = NSFileManager.getDefaultManager().getURLsForDirectory(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask).first();
        NSURL uurl = new NSURL(dir.getPath() + "/.Munchausen/en.munchausen.fingertipsandcompany.any/expansion/chapter/icon_a1.png", new NSURL());


        UNNotificationAttachment attachment = null;


        java.lang.String imageURL = dir.getPath() + "/.Munchausen/en.munchausen.fingertipsandcompany.any/expansion/chapter/icon_a1.png";
        System.out.println("ImagePath---------------->"+imageURL);
        File imageFile = new File(imageURL);
        java.lang.String temp = dir.getPath() + "/.Munchausen/en.munchausen.fingertipsandcompany.any/expansion/chapter/temp";
        File tempFolder = new File(temp);
        if (!tempFolder.exists()) {
            if (tempFolder.mkdir()) {
                System.out.println("Temp Folder created");
            } else {
                System.out.println("Failed to create Temp Folder");
            }
        }

//        try{
//            copyDirectory(imageFile,tempFolder);
//            System.out.println("TempFolder-----------------> "+tempFolder);
//        }catch (IOException e){
//            System.out.println("IOException--------------->"+e);
//        }

        try {
            copyFile(imageFile, temp);
        } catch (IOException e) {
            System.out.println("IOException----------------------->" + e);
        }
        System.out.println("tempFolder--------------------->" + tempFolder);

        java.lang.String tempImgPath = dir.getPath() + "/.Munchausen/en.munchausen.fingertipsandcompany.any/expansion/chapter/temp/icon_temp.png";
        File tempImageFile = new File(tempImgPath);

        URL url = null;
        try {
            url = tempImageFile.toURI().toURL();
        } catch (MalformedURLException e) {
            System.out.println("URL--------------------->" + url);
        }
        if (url != null) {
            NSURL nsURL = new NSURL(url);
            System.out.println("NSURL -------------" + nsURL);
            try {
                attachment = new UNNotificationAttachment("image", nsURL, null);
            } catch (NSErrorException e) {
                System.out.println("Attachment Error:----------------->" + e);
            }
            System.out.println("Attachment : " + attachment);
        }


        content.setTitle(title);
        content.setBody(msg + " " + des);
        content.setSound(UNNotificationSound.getDefaultSound());
        content.setBadge(NSNumber.valueOf(1));
        content.setCategoryIdentifier(userActions);

        System.out.println("Attachment----------------->" + attachment);


        if (attachment != null) {
            System.out.println("Attachment NotNUll----------------------->");
            NSMutableArray<UNNotificationAttachment> array = new NSMutableArray<UNNotificationAttachment>();
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
                ), date);

        UNCalendarNotificationTrigger trig = new UNCalendarNotificationTrigger(components, false);
        ;


        UNTimeIntervalNotificationTrigger trigger = new UNTimeIntervalNotificationTrigger(hrs, false);

        String identifier = "Local Notification";
        UNNotificationRequest request = new UNNotificationRequest(identifier, content, trigger);

        notificationCenter.addNotificationRequest(request, new VoidBlock1<NSError>() {
            @Override
            public void invoke(NSError nsError) {
                if (nsError != null) {
                    System.out.println("Error: invoke::" + nsError);
                }
            }
        });

        UNNotificationAction snoozeAction = new UNNotificationAction("Snooze", "Snooze", UNNotificationActionOptions.None);
        UNNotificationAction deleteAction = new UNNotificationAction("Delete", "Delete", UNNotificationActionOptions.Destructive);
        UNNotificationCategory category = new UNNotificationCategory(userActions, new NSArray<UNNotificationAction>(snoozeAction, deleteAction), new NSArray<NSString>(), UNNotificationCategoryOptions.None);

        notificationCenter.setNotificationCategories(new NSSet<UNNotificationCategory>(category));
    }

    public void copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (String child : children) {
                copyDirectory(new File(sourceLocation, child),
                        new File(targetLocation, child));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }


    private void copyFile(File sourceLocation, String targetLocation) throws IOException {
        InputStream in = new FileInputStream(sourceLocation);
        String imageLocation = targetLocation+"/icon_temp.png";
        OutputStream out = new FileOutputStream(imageLocation);

        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
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
