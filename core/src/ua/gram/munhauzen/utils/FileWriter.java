package ua.gram.munhauzen.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Timer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FileWriter {

    final byte[] bytes = new byte[1024];
    int count, totalRead;
    long start;
    final String tag = getClass().getSimpleName();
    String fileTag;
    final ProgressListener listener;

    final Timer.Task task = new Timer.Task() {
        @Override
        public void run() {

            long elapsed = System.nanoTime() - start;
            long elapsedTimeInSecond = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);

            if (elapsedTimeInSecond == 0) return;

            float downloaded = totalRead / 1024f / 1024f;

            float speed = downloaded / elapsedTimeInSecond;

            Log.i(tag, String.format(Locale.US, "%.2f", speed) + "mb/s " + fileTag);

            listener.onProgress(downloaded, elapsedTimeInSecond, speed);
        }
    };

    public interface ProgressListener {
        void onProgress(float downloaded, long elapsed, float speed);
    }

    public FileWriter(ProgressListener listener) {
        this.listener = listener;
    }

    public void stream(InputStream is, FileHandle file) throws IOException {

        fileTag = file.name();

        file.parent().mkdirs();

        OutputStream os = file.write(false);

        start = System.nanoTime();
        totalRead = 0;
        count = 0;

        Timer.instance().scheduleTask(task, 0, 1);

        try {
            while ((count = is.read(bytes, 0, bytes.length)) != -1) {
                os.write(bytes, 0, count);

                totalRead += count;
            }
        } finally {

            if (task != null)
                task.cancel();

            if (is != null)
                is.close();

            if (os != null)
                os.close();
        }

    }
}
