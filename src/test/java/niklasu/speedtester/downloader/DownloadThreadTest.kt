package niklasu.speedtester.downloader

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import niklasu.speedtester.MB
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals

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

    @Test
    @DisplayName("Ensures that the correct file size is downloaded")
    fun downloadSize() {
        mockWebServer.enqueue(MockResponse().setBody(string))
        val tempFile = getTempFile()
        val downloadThread = DownloadThread(mockWebServer.url("").url(), 2, tempFile, mockk { every { remove(any()) } just Runs })
        downloadThread.run()
        assertEquals((2 * MB).toLong(), tempFile.length())
    }

    private fun getTempFile(): File {
        val tempFile = File.createTempFile("SpeedTester-", "-lol")
        tempFile.deleteOnExit()
        return tempFile
    }
}