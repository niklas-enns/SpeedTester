package niklasu.speedtester;

import com.google.common.eventbus.EventBus;
import niklasu.speedtester.config.ConfigReader;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.downloader.DownloadFileSizeChecker;
import niklasu.speedtester.downloader.DownloadMgr;
import niklasu.speedtester.interfaces.Constants;
import niklasu.speedtester.resultfilewriter.ResultFileWriter;
import niklasu.speedtester.ui.TrayIcon;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.logging.Level;
import java.util.logging.Logger;

//import niklasu.speedtester.ui.TrayIcon;

public class DownloadService implements Constants {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    private EventBus eventBus;

    private ConfigStore configStore;

    public DownloadService(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(DIConfig.class);
        log.setLevel(Level.INFO);
        configStore = context.getBean(ConfigStore.class);
        ConfigReader configReader = context.getBean(ConfigReader.class);
        configReader.parseArgs(args);
        eventBus = context.getBean(EventBus.class);
        DownloadMgr downloadMgr = context.getBean(DownloadMgr.class);
        downloadMgr.start();
        ResultFileWriter writer = context.getBean(ResultFileWriter.class);
        new TrayIcon().go();
    }

    public static void main(String[] args) throws Exception {
        new DownloadService(args);
    }
}
