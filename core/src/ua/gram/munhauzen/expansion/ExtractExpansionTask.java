package ua.gram.munhauzen.expansion;

import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExtractExpansionTask {

    private final String tag = getClass().getSimpleName();
    final MunhauzenGame game;

    public ExtractExpansionTask(MunhauzenGame game) {
        this.game = game;
    }

    public void extract() throws IOException {

        FileHandle expansionFile = ExternalFiles.getExpansionFile(game.params);
        FileHandle targetDirectory = ExternalFiles.getExpansionDir();
        FileHandle lock = ExternalFiles.getExpansionLockFile(game.params);

        ZipInputStream zis = new ZipInputStream(expansionFile.read());

        try {

            lock.delete();

            ArrayList<ZipEntry> entries = determineEntries(zis);

            if (entries.size() == 0) {
                throw new NullPointerException("No expansion entries found");
            }

            ZipFile zip = new ZipFile(expansionFile.file());

            for (ZipEntry entry : entries) {

                if (entry.isDirectory())
                    continue;

                File outputFile = new File(targetDirectory.file(), entry.getName());
                File dir = entry.isDirectory() ? outputFile : outputFile.getParentFile();

                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());

                if (entry.isDirectory())
                    continue;

                if (outputFile.exists() && outputFile.length() == entry.getSize())
                    continue;

                BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));

                int b;
                byte[] buffer = new byte[1024];
                FileOutputStream fos = new FileOutputStream(outputFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

                while ((b = bis.read(buffer, 0, 1024)) != -1) {
                    bos.write(buffer, 0, b);
                }

                bos.close();
                bis.close();
            }

            zis.close();

            lock.writeString("{\"extracted\":true}", false);

            expansionFile.delete();

        } catch (Throwable e) {

            cleanup();

            throw e;
        }

        Log.i(tag, "completed");
    }

    private void cleanup() {
        ExternalFiles.getExpansionFile(game.params).delete();

        ExternalFiles.getExpansionLockFile(game.params).delete();

        ExternalFiles.getExpansionImagesDir().deleteDirectory();

        ExternalFiles.getExpansionAudioDir().deleteDirectory();
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