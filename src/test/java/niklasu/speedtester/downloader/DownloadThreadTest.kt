package niklasu.speedtester.downloader

import com.google.common.eventbus.EventBus
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import niklasu.speedtester.MB
import niklasu.speedtester.events.ResultEvent
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DownloadThreadTest {

    val mockWebServer = MockWebServer()
    var string = "asd"
    @BeforeAll
    fun initbytes() {
        var repsonseBytes = ByteArray(100* MB)
        for (i in 1..100 * MB) {
            repsonseBytes[i - 1] = 33
        }
        string = String(repsonseBytes)
    }

    @ParameterizedTest
    @ValueSource(longs = [1, 5, 10, 50, 100])
    @DisplayName("Ensures that the download implementation is not the bottleneck of the download")
    fun throttling(downloadSizeInMB: Long) {

        mockWebServer.enqueue(MockResponse().setBody(string))

        val results = mutableListOf<ResultEvent>()

        val eventBus = mockk<EventBus> { every { post(capture(results)) }just Runs }
        val downloadThread = DownloadThread(eventBus, mockWebServer.url("").url(), downloadSizeInMB, getTempFile(), DownloadThread.FileRemover())
        downloadThread.run()

        val resultEvent = results[0]
        println(resultEvent.speed)
        assertTrue(resultEvent.speed > 200)

    }

    @Test
    @DisplayName("Ensures that the correct file size is downloaded")
    fun downloadSize() {
        mockWebServer.enqueue(MockResponse().setBody(string))
        val tempFile = getTempFile()
        val downloadThread = DownloadThread(EventBus(), mockWebServer.url("").url(), 2, tempFile, mockk() { every { remove(any()) } just Runs })
        downloadThread.run()
        assertEquals((2 * MB).toLong(), tempFile.length())
    }

    private fun getTempFile(): File {
        val tempFile = File.createTempFile("SpeedTester-", "-lol")
        tempFile.deleteOnExit()
        return tempFile
    }
}