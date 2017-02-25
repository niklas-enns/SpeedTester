package niklasu.speedtester.downloader;

import niklasu.speedtester.DownloadService;
import niklasu.speedtester.downloader.DownloadFileSizeChecker;
import niklasu.speedtester.interfaces.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by enzo on 06.11.2014.
 */
public class DownloadThread extends Thread implements Constants {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    URL targetFile;
    long downloadSize;
    Date startOfDownload;

    public DownloadThread(long size, URL url) {
        targetFile = url;
        downloadSize = size;
    }

    public void run() {
        startOfDownload = new Date();
        long startTime = startOfDownload.getTime();
        download();
        long runningTime = new Date().getTime() - startTime;
        double resultSpeed = ((double) downloadSize / ((double) runningTime / 1000.)) / MB * 8.;
        log.info("Download of " + downloadSize / MB + "MB completed, calculated Speed: " + String.format("%.2f", resultSpeed) + " MBit/s");
        publishResult(startOfDownload, resultSpeed);
    }

    private void publishResult(Date startOfDownload, double resultSpeed) {
        //TODO via EventBus
    }

    private void download() {
        try {
            log.info("Starting download...");
            ReadableByteChannel rbc = Channels.newChannel(targetFile.openStream());
            FileOutputStream fos = new FileOutputStream("temp.dat");
            fos.getChannel().transferFrom(rbc, 0, downloadSize);
            rbc.close();
            //yes
        } catch (Exception e) {
            log.severe("Could not download file");
        }
    }

}
