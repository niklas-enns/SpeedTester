package niklasu.speedtester;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import niklasu.speedtester.config.ConfigProvider;
import niklasu.speedtester.downloader.DownloadManager;
import niklasu.speedtester.events.ConfigChangedEvent;
import niklasu.speedtester.events.StartEvent;
import niklasu.speedtester.guice.Module;
import niklasu.speedtester.resultfilewriter.ResultFileWriter;
import niklasu.speedtester.ui.TrayContextMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main{
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new Module());
        DownloadManager downloadManager = injector.getInstance(DownloadManager.class);
        ConfigProvider configProvider = injector.getInstance(ConfigProvider.class);
        configProvider.setConfig(args);
        injector.getInstance(ResultFileWriter.class);

        injector.getInstance(EventBus.class).post(new ConfigChangedEvent());
        injector.getInstance(EventBus.class).post(new StartEvent());

        if (configProvider.isTray()) injector.getInstance(TrayContextMenu.class);
    }
}
