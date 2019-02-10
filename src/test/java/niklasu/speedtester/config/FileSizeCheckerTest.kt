package niklasu.speedtester.config

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class FileSizeCheckerTest {

    @Test
    fun getFileSize() {
        //GIVEN
        val mockWebServer =  MockWebServer()
        val responseBody = "0123456789"
        val mockResponse = MockResponse().setBody(responseBody)
        mockWebServer.enqueue(mockResponse)
        val fileSizeChecker = FileSizeChecker(OkHttpClient())

        //WHEN THEN
        assertEquals(responseBody.length.toLong(), fileSizeChecker.getFileSize(mockWebServer.url("nix").url()))
    }

    @Test
    fun get404() {
        //GIVEN
        val mockWebServer =  MockWebServer()
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)
        val fileSizeChecker = FileSizeChecker(OkHttpClient())

        //WHEN THEN
        assertThrows(ValidationException::class.java){fileSizeChecker.getFileSize(mockWebServer.url("nix").url())}
    }

    @Test
    fun ioException() {
        //GIVEN
        val mockWebServer =  MockWebServer()
        val mockResponse = MockResponse().setBody("1234").setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
        mockWebServer.enqueue(mockResponse)
        val fileSizeChecker = FileSizeChecker(OkHttpClient())

        //WHEN THEN
        assertThrows(ValidationException::class.java){fileSizeChecker.getFileSize(mockWebServer.url("nix").url())}.printStackTrace()
    }
}