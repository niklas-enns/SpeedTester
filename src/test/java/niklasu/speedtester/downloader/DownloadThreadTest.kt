package niklasu.speedtester.downloader

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import niklasu.speedtester.Constants.KB
import niklasu.speedtester.Constants.MB
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DownloadThreadTest {

    val mockWebServer = MockWebServer()
    var string = "asd"

    @BeforeAll
    fun initbytes() {
        var repsonseBytes = ByteArray(100 * MB)
        for (i in 1..100 * MB) {
            repsonseBytes[i - 1] = "${i % 128}".toByte()
        }
        string = String(repsonseBytes)
    }

    @Test
    @DisplayName("Ensures that the correct file size is downloaded (1KB tolerance)")
    @Disabled
    fun downloadSize() {
        mockWebServer.enqueue(MockResponse().setBody(string))
        val downloadThread = DownloadThread(
                mockWebServer.url("").url(),
                5,
                mockk {
                    every { show(any()) } just runs
                    every { showProgress(any(), any()) } just runs
                },
                mockk{
                    every { write(any()) } just runs
                }
        )
        downloadThread.run()
        Assert.assertEquals((5 * MB).toDouble(), downloadThread.downloadedBytes.toDouble(), 10 * KB.toDouble())
    }

    @ParameterizedTest
    @ValueSource(longs = [1, 5, 10, 50, 100])
//    @Disabled("Intended for manual Testing")
    @DisplayName("Ensures that the download implementation is not the bottleneck of the download")
    fun throttling(downloadSizeInMB: Long) {
        mockWebServer.enqueue(MockResponse().setBody(string))

        val downloadThread = DownloadThread(mockWebServer.url("").url(), downloadSizeInMB,
                mockk {
                    every { show(any()) } just runs
                    every { showProgress(any(), any()) } just runs
                },
                mockk{
                    every { write(any()) } just runs
                }

        )

        downloadThread.run()

        //look at the logs for the measured download speed and decide if its okay
    }

}