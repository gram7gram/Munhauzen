package ua.gram.munhauzen.expansion;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
public class ExtractGameConfigTask {

    private final MunhauzenGame game;
    private final String tag = getClass().getSimpleName();

    public ExtractGameConfigTask(MunhauzenGame game) {
        this.game = game;
    }

    public void extract() throws IOException {

        FileHandle archive = ExternalFiles.getGameArchiveFile(game.params);
        if (!archive.exists()) {
            throw new GdxRuntimeException("Nothing to extract");
        }

        try {

            ZipInputStream zis = new ZipInputStream(archive.read());

            ArrayList<ZipEntry> entries = determineEntries(zis);

            if (entries.size() == 0) {
                throw new NullPointerException("No expansion entries found");
            }

            ZipFile zip = new ZipFile(ExternalFiles.getGameArchiveFile(game.params).file());

            for (ZipEntry entry : entries) {

                if (entry.isDirectory())
                    continue;

                FileHandle outputFile;
                switch (entry.getName()) {
                    case "scenario.json":
                        outputFile = ExternalFiles.getScenarioFile(game.params);
                        break;
                    case "audio.json":
                        outputFile = ExternalFiles.getAudioFile(game.params);
                        break;
                    case "audio-fails.json":
                        outputFile = ExternalFiles.getAudioFailsFile(game.params);
                        break;
                    case "chapters.json":
                        outputFile = ExternalFiles.getChaptersFile(game.params);
                        break;
                    case "images.json":
                        outputFile = ExternalFiles.getImagesFile(game.params);
                        break;
                    case "inventory.json":
                        outputFile = ExternalFiles.getInventoryFile(game.params);
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
        } catch (Throwable e) {

            archive.delete();

            throw e;
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