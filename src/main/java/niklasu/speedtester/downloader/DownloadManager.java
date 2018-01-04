package niklasu.speedtester.downloader;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.events.ConfigChangedEvent;
import niklasu.speedtester.events.StartEvent;
import niklasu.speedtester.events.StopEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Schedules downloads
 */
public class DownloadManager {
    private static final Logger logger = LoggerFactory.getLogger(DownloadManager.class);

    private EventBus eventBus;
    private ConfigStore configStore;
    private DownloadThread downloadThread;

    private ScheduledFuture<?> scheduledFuture;
    private ScheduledExecutorService scheduler;

    @Inject
    public DownloadManager(EventBus eventBus, ConfigStore configStore, DownloadThread downloadThread, ScheduledExecutorService scheduledExecutorService) throws MalformedURLException {
        logger.trace("Constructing DownloadManager");
        this.eventBus = eventBus;
        this.configStore = configStore;
        this.downloadThread = downloadThread;
        this.scheduler = scheduledExecutorService;
        eventBus.register(this);
    }

    @Subscribe
    public void configChangedHandler(ConfigChangedEvent configChangedEvent) throws MalformedURLException {
        logger.debug("configChangeHandler");
        downloadThread.setSize(configStore.getSize());
        downloadThread.setUrl(new URL(configStore.getUrl()));
    }

    @Subscribe
    public void stopEventHandler(StopEvent stopEvent) {
        logger.debug("stopEvent");
        scheduledFuture.cancel(true);
    }

    @Subscribe
    public void startEventHandler(StartEvent startEvent) {
        logger.info("startEvent");
        scheduledFuture = scheduler.scheduleAtFixedRate(downloadThread, 0, configStore.getInterval(), MINUTES);
    }
}
