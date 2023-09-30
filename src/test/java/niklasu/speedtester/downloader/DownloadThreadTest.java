package niklasu.speedtester.downloader;

import static niklasu.speedtester.Constants.KB;
import static niklasu.speedtester.Constants.MB;
import static org.mockito.Mockito.mock;

import niklasu.speedtester.influx.InfluxModule;
import niklasu.speedtester.ui.ConsoleResultPrinter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DownloadThreadTest {

    MockWebServer mockWebServer = new MockWebServer();
    static String string = "asd";

    @BeforeAll
    static void initbytes() {
        byte[] repsonseBytes = new byte[100 * MB];
        for (int i = 0; i < repsonseBytes.length; i++) {
            repsonseBytes[i] = (byte) (i % 128);
        }
        string = new String(repsonseBytes);
    }

    @Test
    @DisplayName("Ensures that the correct file size is downloaded (10KB tolerance)")
    @Disabled
    void downloadSize() {
        mockWebServer.enqueue(new MockResponse().setBody(string));
        ConsoleResultPrinter consoleResultPrinter = mock(ConsoleResultPrinter.class);
        InfluxModule.InfluxWriter influxWriter = mock(InfluxModule.InfluxWriter.class);
        DownloadThread downloadThread = new DownloadThread(
                mockWebServer.url("").url(),
                5,
                consoleResultPrinter,
                influxWriter
        );
        downloadThread.run();
        Assertions.assertEquals((5 * MB), downloadThread.downloadedBytes, 10 * KB);
    }

    @ParameterizedTest
    @ValueSource(longs = { 1, 5, 10, 50, 100 })
    //    @Disabled("Intended for manual Testing")
    @DisplayName("Ensures that the download implementation is not the bottleneck of the download")
    void throttling(long downloadSizeInMB) {
        mockWebServer.enqueue(new MockResponse().setBody(string));
        ConsoleResultPrinter consoleResultPrinter = mock(ConsoleResultPrinter.class);
        InfluxModule.InfluxWriter influxWriter = mock(InfluxModule.InfluxWriter.class);

        DownloadThread downloadThread = new DownloadThread(
                mockWebServer.url("").url(),
                downloadSizeInMB,
                consoleResultPrinter,
                influxWriter
        );

        downloadThread.run();

        //look at the logs for the measured download speed and decide if its okay
    }

}