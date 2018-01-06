package niklasu.speedtester.downloader;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import niklasu.speedtester.events.ResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;

import static niklasu.speedtester.Constants.MB;

public class DownloadThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(DownloadThread.class);
    private URL targetFile;
    private long downloadsizeInMB;
    private EventBus eventBus;

    @Inject
    public DownloadThread(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setSize(int size) {
        this.downloadsizeInMB = size;
    }

    public void setUrl(URL url) {
        this.targetFile = url;
    }

    public void run() {
        Date startOfDownload = new Date();
        long startTime = startOfDownload.getTime();
        try {
            download();
            long runningTime = new Date().getTime() - startTime;
            double resultSpeed = ((double) downloadsizeInMB / ((double) runningTime / 1000.)) * 8.;
            logger.info("Download of " + downloadsizeInMB + "MB completed, calculated Speed: " + String.format("%.2f", resultSpeed) + " MBit/s");
            eventBus.post(new ResultEvent(startOfDownload, resultSpeed));
        } catch (DownloadException e) {
            logger.error("Download failed, because", e);
            eventBus.post(new ResultEvent(startOfDownload, -1));
        }
    }

    private void download() throws DownloadException {
        try {
            logger.info(String.format("Starting download of %d MB from %s", downloadsizeInMB, targetFile.toString()));
            File tempFile = File.createTempFile("SpeedTester-","-lol");
            tempFile.deleteOnExit();
            ReadableByteChannel rbc = Channels.newChannel(targetFile.openStream());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.getChannel().transferFrom(rbc, 0, downloadsizeInMB * MB);
            rbc.close();
            tempFile.delete();
        } catch (FileNotFoundException e) {
            throw new DownloadException("", e);
        } catch (IOException e) {
            throw new DownloadException("", e);
        }
    }
}
