package ua.gram.munhauzen;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PermissionManager {

    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
    };

    public static final int PERMISSION_REQUEST = 99;

    public static void grant(Activity context, String[] permissions) {
        ArrayList<String> ungrantedPermissions = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            int state = ContextCompat.checkSelfPermission(context, permission);
            switch (state) {
                case PackageManager.PERMISSION_GRANTED:
                    break;
                case PackageManager.PERMISSION_DENIED:
                    ungrantedPermissions.add(permission);
                    break;
            }
        }

        if (ungrantedPermissions.size() == 0) return;

        String[] plainPermissions = new String[ungrantedPermissions.size()];
        int i = 0;
        for (String p : ungrantedPermissions) {
            plainPermissions[i++] = p;
        }
        try {
            ActivityCompat.requestPermissions(context, plainPermissions, PERMISSION_REQUEST);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}