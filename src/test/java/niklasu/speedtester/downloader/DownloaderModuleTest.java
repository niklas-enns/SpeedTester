package niklasu.speedtester.downloader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import niklasu.speedtester.config.ConfigProvider;
import niklasu.speedtester.influx.InfluxModule;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DownloaderModuleTest {
    private MockWebServer mockWebServer = new MockWebServer();

    private Injector injector =
            Guice.createInjector(new DownloaderModule(), new MockConfigProvider(mockWebServer.url("")), new InfluxModule());

    class MockConfigProvider extends AbstractModule {
        HttpUrl url;

        public MockConfigProvider(final HttpUrl url) {
            this.url = url;
        }

        @Provides
        ConfigProvider getConfigProvider () {
            ConfigProvider configProvider = new ConfigProvider();
            configProvider.interval = 1;
            configProvider.size = 100L;
            configProvider.url = url.toString();
            return configProvider;
        }
    }

    @Test
    @Disabled
    void testDownloads() {
        //GIVEN
        mockWebServer.enqueue(new MockResponse());
        DownloadThread singleDownloader = injector.getInstance(DownloadThread.class);

        //WHEN
        singleDownloader.run();

        //THEN
        assertEquals(1, mockWebServer.getRequestCount());
    }

    @Test
    void testError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        DownloadThread singleDownloader = injector.getInstance(DownloadThread.class);
        singleDownloader.run();

        assertEquals(1, mockWebServer.getRequestCount());
    }
}