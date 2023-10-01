package niklasu.speedtester.downloader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.net.URL;

import niklasu.speedtester.influx.InfluxModule;
import niklasu.speedtester.ui.ConsoleResultPrinter;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DownloadSchedulerFactoryTest {
    public static final long DOWNLOADSIZE_IN_MB = 100L;
    private MockWebServer mockWebServer = new MockWebServer();
    final URL url = mockWebServer.url("").url();

    @Test
    @Disabled
    void testDownloads() {
        //GIVEN
        mockWebServer.enqueue(new MockResponse());

        //WHEN
        new DownloadThread(url, DOWNLOADSIZE_IN_MB, new ConsoleResultPrinter(), mock(InfluxModule.InfluxWriter.class)).run();

        //THEN
        assertEquals(1, mockWebServer.getRequestCount());
    }

    @Test
    void testError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        new DownloadThread(url, DOWNLOADSIZE_IN_MB, new ConsoleResultPrinter(), mock(InfluxModule.InfluxWriter.class)).run();

        assertEquals(1, mockWebServer.getRequestCount());
    }
}