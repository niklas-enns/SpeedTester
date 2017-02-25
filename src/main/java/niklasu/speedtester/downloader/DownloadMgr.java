package niklasu.speedtester.downloader;

import niklasu.speedtester.config.ConfigStore;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Created by enzo on 25.02.2017.
 */
public class DownloadMgr {
    ScheduledFuture<?> scheduledFuture;
    ScheduledExecutorService scheduler;
    ConfigStore configStore;

    public DownloadMgr(ConfigStore configStore){
        this.configStore = configStore;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        startDownloadQueue();
    }

    public void startDownloadQueue() {
        //log.finest("scheduledFuture.start");
        try {
            scheduledFuture = scheduler.scheduleAtFixedRate(new DownloadThread(configStore.getSize(), new URL(configStore.getUrl())), 0, configStore.getInterval(), MINUTES);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void cancelDownloadQueue() {
        //log.finest("scheduledFuture.cancel");
        scheduledFuture.cancel(true);
    }
}
