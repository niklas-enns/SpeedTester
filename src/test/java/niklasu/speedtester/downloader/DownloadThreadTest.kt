package niklasu.speedtester.downloader

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import niklasu.speedtester.KB
import niklasu.speedtester.MB
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DownloadThreadTest {

    val mockWebServer = MockWebServer()
    var string = "asd"
    @BeforeAll
    fun initbytes() {
        var repsonseBytes = ByteArray(100* MB)
        for (i in 1..100 * MB) {
            repsonseBytes[i - 1] = "${i % 128}".toByte()
        }
        string = String(repsonseBytes)
    }

    @Test
    @DisplayName("Ensures that the correct file size is downloaded")
    fun downloadSize() {
        mockWebServer.enqueue(MockResponse().setBody(string))
        val downloadThread = DownloadThread(mockWebServer.url("").url(), 5, mockk { every { show(any()) } just runs })
        downloadThread.run()
        Assert.assertEquals((5 * MB).toDouble(), downloadThread.downloadedBytes.toDouble(), 1 * KB.toDouble())
    }

}