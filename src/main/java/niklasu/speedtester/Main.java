package niklasu.speedtester;

import com.google.inject.Guice;
import com.google.inject.Injector;
import niklasu.speedtester.config.ConfigModule;
import niklasu.speedtester.downloader.DownloadScheduler;
import niklasu.speedtester.downloader.DownloaderModule;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ConfigModule(args), new DownloaderModule());
        injector.getInstance(DownloadScheduler.class).start();
    }
}
