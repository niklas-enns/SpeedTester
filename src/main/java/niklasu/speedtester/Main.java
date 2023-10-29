package niklasu.speedtester;

import java.net.MalformedURLException;

import niklasu.speedtester.config.ConfigProvider;
import niklasu.speedtester.config.ConfigProviderFactory;
import niklasu.speedtester.config.ValidationException;
import niklasu.speedtester.downloader.DownloadSchedulerFactory;
import niklasu.speedtester.influx.InfluxModule;
import niklasu.speedtester.ui.ConsoleResultPrinter;

public class Main {

    public static void main(String... args) throws MalformedURLException, ValidationException {
        final ConfigProvider configProvider = ConfigProviderFactory.getConfigProvider(args);

        DownloadSchedulerFactory.getDownloadScheduler(
                configProvider,
                new ConsoleResultPrinter(),
                InfluxModule.getInfluxWriter(configProvider
                )
        ).start();
    }
}
