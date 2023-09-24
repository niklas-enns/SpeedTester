package niklasu.speedtester;

import com.google.inject.Guice;
import com.google.inject.Injector;
import niklasu.speedtester.config.ConfigModule;
import niklasu.speedtester.downloader.DownloadScheduler;
import niklasu.speedtester.downloader.DownloaderModule;
import niklasu.speedtester.influx.InfluxModule;

public class Main {

    public static void main(String... args) {
        final Injector injector =
                Guice.createInjector(new ConfigModule(args), new DownloaderModule(), new InfluxModule());
        injector.getInstance(DownloadScheduler.class).start();
    }
}
