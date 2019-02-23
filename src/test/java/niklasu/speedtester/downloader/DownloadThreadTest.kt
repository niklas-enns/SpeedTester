package niklasu.speedtester.downloader

import com.google.common.eventbus.EventBus
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Test

internal class DownloadThreadTest {

    @Test
    fun run() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setBody("lol"))
        val downloadThread = DownloadThread(EventBus(), mockWebServer.url("lol").url(), 2)
        downloadThread.run()
    }
}