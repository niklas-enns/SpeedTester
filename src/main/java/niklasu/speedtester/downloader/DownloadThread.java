package niklasu.speedtester.downloader;

import com.google.common.eventbus.EventBus;
import niklasu.speedtester.Result;
import niklasu.speedtester.interfaces.Constants;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.logging.Logger;

public class DownloadThread extends Thread implements Constants {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    private URL targetFile;
    private long downloadsizeInMB;
    private Date startOfDownload;
    private EventBus eventBus;
    public DownloadThread(long downloadsizeInMB, URL url, EventBus eventBus) {
        this.eventBus = eventBus;
        targetFile = url;
        this.downloadsizeInMB = downloadsizeInMB;
    }

    public void run() {
        startOfDownload = new Date();
        long startTime = startOfDownload.getTime();
        download();
        long runningTime = new Date().getTime() - startTime;
        double resultSpeed = ((double) downloadsizeInMB / ((double) runningTime / 1000.)) * 8.;
        log.info("Download of " + downloadsizeInMB + "MB completed, calculated Speed: " + String.format("%.2f", resultSpeed) + " MBit/s");
        publishResult(startOfDownload, resultSpeed);
}

    private void download() {
        try {
            log.info("Starting download...");
            ReadableByteChannel rbc = Channels.newChannel(targetFile.openStream());
            FileOutputStream fos = new FileOutputStream("temp.dat");
            fos.getChannel().transferFrom(rbc, 0, downloadsizeInMB * MB);
            rbc.close();
            //yes
        } catch (Exception e) {
            log.severe("Could not download file");
        }
    }

    private void publishResult(Date startOfDownload, double resultSpeed) {
        eventBus.post(new Result(startOfDownload, resultSpeed));
    }

}
