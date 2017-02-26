package niklasu.speedtester.downloader;

import com.google.common.eventbus.EventBus;
import niklasu.speedtester.config.ConfigStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;

@Component
public class DownloadMgr {
    private ScheduledFuture<?> scheduledFuture;
    private ScheduledExecutorService scheduler;
    @Autowired
    private ConfigStore configStore;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private ApplicationContext context;

    @PostConstruct
    private void init(){
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start(){
        scheduledFuture = scheduler.scheduleAtFixedRate(context.getBean(DownloadThread.class), 0, configStore.getInterval(), MINUTES);
    }

    public void cancelDownloadQueue() {
        //log.finest("scheduledFuture.cancel");
        scheduledFuture.cancel(true);
    }
}
