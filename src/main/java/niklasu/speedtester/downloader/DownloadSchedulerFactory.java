package niklasu.speedtester.downloader;

import java.net.MalformedURLException;
import java.net.URL;

import niklasu.speedtester.config.ConfigProvider;
import niklasu.speedtester.influx.InfluxModule;
import niklasu.speedtester.ui.ConsoleResultPrinter;

public class DownloadSchedulerFactory{

    public static DownloadScheduler getDownloadScheduler(ConfigProvider configProvider, ConsoleResultPrinter consoleResultPrinter,
            InfluxModule.InfluxWriter influxConnector)
            throws MalformedURLException {
        final DownloadThread downloadThread =
                new DownloadThread(new URL(configProvider.url), configProvider.size, consoleResultPrinter,
                        influxConnector);
        return new DownloadScheduler(downloadThread, configProvider);
    }

}