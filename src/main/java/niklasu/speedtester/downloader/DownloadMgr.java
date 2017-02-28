package niklasu.speedtester.downloader;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.events.ConfigChangedEvent;
import niklasu.speedtester.events.StartEvent;
import niklasu.speedtester.events.StopEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.MINUTES;

@Component
public class DownloadMgr {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    @Autowired
    EventBus eventBus;
    private ScheduledFuture<?> scheduledFuture;
    private ScheduledExecutorService scheduler;
    @Autowired
    private ConfigStore configStore;
    @Autowired
    private ApplicationContext context;

    @PostConstruct
    private void initAndStart() throws MalformedURLException {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        eventBus.register(this);
        //TODO Downloader startet implizit, nachdem der Thread von Spring instanziiert wurde. Das nicht gut.
        configChangedHandler(new ConfigChangedEvent());
        startEventHandler(new StartEvent());
    }

    @Subscribe
    public void configChangedHandler(ConfigChangedEvent configChangedEvent) throws MalformedURLException {
        DownloadThread downloadThread = context.getBean(DownloadThread.class);
        downloadThread.setSize(configStore.getSize());
        downloadThread.setUrl(new URL(configStore.getUrl()));
    }

    @Subscribe
    public void stopEventHandler(StopEvent stopEvent) {
        log.info("stopEvent");
        scheduledFuture.cancel(true);
    }

    @Subscribe
    public void startEventHandler(StartEvent startEvent) {
        log.info("startEvent");
        scheduledFuture = scheduler.scheduleAtFixedRate(context.getBean(DownloadThread.class), 0, configStore.getInterval(), MINUTES);
    }
}
