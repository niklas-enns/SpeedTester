package niklasu.speedtester.downloader;

import java.net.MalformedURLException;
import java.net.URL;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import niklasu.speedtester.config.ConfigProvider;
import niklasu.speedtester.influx.InfluxModule;
import niklasu.speedtester.ui.ConsoleResultPrinter;

public class DownloaderModule extends AbstractModule {
    @Provides
    private DownloadThread getThread(ConfigProvider configProvider, ConsoleResultPrinter consoleResultPrinter,
            InfluxModule.InfluxWriter influxConnector)
            throws MalformedURLException {
        return new DownloadThread(new URL(configProvider.url), configProvider.size, consoleResultPrinter,
                influxConnector);
    }

}