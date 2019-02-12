package niklasu.speedtester.downloader;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import niklasu.speedtester.config.ConfigProvider;
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
 * Schedules downloads and
 */
public class DownloadManager {
    private static final Logger logger = LoggerFactory.getLogger(DownloadManager.class);

    private final ConfigProvider configProvider;
    private final DownloadThread downloadThread;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;

    @Inject
    public DownloadManager(EventBus eventBus, ConfigProvider configProvider, DownloadThread downloadThread, ScheduledExecutorService scheduledExecutorService) {
        this.configProvider = configProvider;
        this.downloadThread = downloadThread;
        this.scheduler = scheduledExecutorService;
        eventBus.register(this);
    }

    @Subscribe
    public void configChangedHandler(ConfigChangedEvent configChangedEvent) throws MalformedURLException {
        logger.trace("configChangeHandler");
        downloadThread.setSize(configProvider.getSize());
        downloadThread.setUrl(new URL(configProvider.getUrl()));
    }

    @Subscribe
    public void stopEventHandler(StopEvent stopEvent) {
        logger.debug("stopEvent");
        logger.info("Stopped scheduling downloads. If there was already a download running while stopping, it will be finished.");
        scheduledFuture.cancel(false);
    }

    @Subscribe
    public void startEventHandler(StartEvent startEvent) {
        scheduledFuture = scheduler.scheduleAtFixedRate(downloadThread, 0, configProvider.getInterval(), MINUTES);
    }
}
