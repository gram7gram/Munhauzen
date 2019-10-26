package ua.gram.munhauzen;

import android.os.Environment;
import android.os.StatFs;

import ua.gram.munhauzen.utils.MemoryUsage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AndroidMemoryUsage implements MemoryUsage {

    @Override
    public float megabytesAvailable() {

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        else
            bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();

        return bytesAvailable / (1024.f * 1024.f);

    }
}
