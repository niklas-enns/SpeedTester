package niklasu.speedtester;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import niklasu.speedtester.config.ConfigReader;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.downloader.DownloadFileSizeChecker;
import niklasu.speedtester.downloader.DownloadMgr;
import niklasu.speedtester.interfaces.Constants;
import niklasu.speedtester.resultfilewriter.ResultFileWriter;
import niklasu.speedtester.ui.UI;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadService implements Constants {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    private EventBus eventBus;

    private ConfigStore configStore;

    public DownloadService(String[] args) throws Exception {
        log.setLevel(Level.INFO);
        configStore = new ConfigStore();
        new ConfigReader(args, configStore);
        eventBus = new EventBus();
        if (SystemTray.isSupported() && configStore.isTray()) {
            new UI(configStore);
        } else {
            log.info("SystemTray not available");
        }
        new DownloadMgr(configStore, eventBus);
        ResultFileWriter writer = new ResultFileWriter(eventBus);
        eventBus.register(this);
    }

    public static void main(String[] args) throws Exception {
        new DownloadService(args);
    }
}
