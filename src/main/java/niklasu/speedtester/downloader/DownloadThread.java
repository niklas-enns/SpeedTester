package niklasu.speedtester.downloader;

import com.google.common.eventbus.EventBus;
import niklasu.speedtester.events.ResultEvent;
import niklasu.speedtester.interfaces.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class DownloadThread extends Thread implements Constants {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private URL targetFile;
    private long downloadsizeInMB;
    @Autowired
    private EventBus eventBus;

    public void setSize(int size) {
        this.downloadsizeInMB = size;
    }

    public void setUrl(URL url) {
        this.targetFile = url;
    }

    public void run() {
        Date startOfDownload = new Date();
        long startTime = startOfDownload.getTime();
        download();
        long runningTime = new Date().getTime() - startTime;
        double resultSpeed = ((double) downloadsizeInMB / ((double) runningTime / 1000.)) * 8.;
        log.info("Download of " + downloadsizeInMB + "MB completed, calculated Speed: " + String.format("%.2f", resultSpeed) + " MBit/s");
        publishResult(startOfDownload, resultSpeed);

    }

    private void download() {
        try {
            log.info(String.format("Starting download of %d MB from %s", downloadsizeInMB, targetFile.toString()));
            ReadableByteChannel rbc = Channels.newChannel(targetFile.openStream());
            FileOutputStream fos = new FileOutputStream("temp.dat");
            fos.getChannel().transferFrom(rbc, 0, downloadsizeInMB * MB);
            rbc.close();
        } catch (Exception e) {
            log.severe("Could not download file");
        }
    }

    private void publishResult(Date startOfDownload, double resultSpeed) {
        eventBus.post(new ResultEvent(startOfDownload, resultSpeed));
    }

}
