package ua.gram.munhauzen.expansion;

import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExtractGameConfigTask {

    private final String tag = getClass().getSimpleName();

    public void extract() {

        ZipInputStream zis = new ZipInputStream(ExternalFiles.getGameArchiveFile().read());

        try {

            ArrayList<ZipEntry> entries = determineEntries(zis);

            if (entries.size() == 0) {
                Log.e(tag, "No expansion entries found");
                return;
            }

            ZipFile zip = new ZipFile(ExternalFiles.getGameArchiveFile().file());

            for (ZipEntry entry : entries) {

                if (entry.isDirectory())
                    continue;

                Log.i(tag, entry.getName());

                FileHandle outputFile;
                switch (entry.getName()) {
                    case "scenario.json":
                        outputFile = ExternalFiles.getScenarioFile();
                        break;
                    case "audio.json":
                        outputFile = ExternalFiles.getAudioFile();
                        break;
                    case "audio-fails.json":
                        outputFile = ExternalFiles.getAudioFailsFile();
                        break;
                    case "chapters.json":
                        outputFile = ExternalFiles.getChaptersFile();
                        break;
                    case "images.json":
                        outputFile = ExternalFiles.getImagesFile();
                        break;
                    case "inventory.json":
                        outputFile = ExternalFiles.getInventoryFile();
                        break;
                    default:
                        continue;

                }

                outputFile.write(false);

                if (outputFile.exists() && outputFile.length() == entry.getSize())
                    continue;

                BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));

                int b;
                byte[] buffer = new byte[1024];
                OutputStream fos = outputFile.write(false);
                BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

                while ((b = bis.read(buffer, 0, 1024)) != -1) {
                    bos.write(buffer, 0, b);
                }

                bos.close();
                bis.close();
            }

            zis.close();

        } catch (IOException e) {
            Log.e(tag, e);
        }

        Log.i(tag, "completed");
    }

    private ArrayList<ZipEntry> determineEntries(ZipInputStream zis) throws IOException {

        ArrayList<ZipEntry> entries = new ArrayList<>();

        ZipEntry e;
        while ((e = zis.getNextEntry()) != null) {
            entries.add(e);
        }

        return entries;
    }
}