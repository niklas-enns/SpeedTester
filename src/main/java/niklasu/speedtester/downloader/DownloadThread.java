package niklasu.speedtester.downloader;

import com.google.common.eventbus.EventBus;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.events.ResultEvent;
import niklasu.speedtester.interfaces.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class DownloadThread extends Thread implements Constants {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    private URL targetFile;
    private long downloadsizeInMB;
    @Autowired
    private ConfigStore configStore;
    @Autowired
    private EventBus eventBus;
    @PostConstruct
    private void init() throws MalformedURLException {
        this.downloadsizeInMB = configStore.getSize();
        targetFile = new URL(configStore.getUrl());
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
        eventBus.post(new ResultEvent(startOfDownload, resultSpeed));
        configStore.fireConfigChangedEvent();
    }

}
