package niklasu.speedtester.config

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.IOException
import java.util.stream.IntStream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FileSizeCheckerTest {

    @Test
    @DisplayName("gets file size from headers correctly")
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

    @ParameterizedTest
    @MethodSource("invalidResponseCodes")
    @DisplayName("Throws exception on response code 400 and 404")
    fun get404(number: Int) {
        //GIVEN
        val mockWebServer =  MockWebServer()
        val mockResponse = MockResponse().setResponseCode(number)
        mockWebServer.enqueue(mockResponse)
        val fileSizeChecker = FileSizeChecker(OkHttpClient())

        //WHEN THEN
        assertThrows(ValidationException::class.java){fileSizeChecker.getFileSize(mockWebServer.url("nix").url())}
    }

    fun invalidResponseCodes():IntStream {
        return IntStream.of(400, 404)
    }

    @Test
    fun ioException() {
        //GIVEN
        val mockWebServer =  MockWebServer()
        val mockResponse = MockResponse().setBody("1234").setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
        mockWebServer.enqueue(mockResponse)
        val fileSizeChecker = FileSizeChecker(OkHttpClient())

        //WHEN THEN
        assertThrows(IOException::class.java){fileSizeChecker.getFileSize(mockWebServer.url("nix").url())}.printStackTrace()
    }

    @Test
    @DisplayName("Content-Length: noNumber")
    fun contentLengthHeaderHasNoNumericalValue() {
        //GIVEN
        val mockWebServer =  MockWebServer()
        val mockResponse = MockResponse().setHeader("Content-Length", "noNumber")
        mockWebServer.enqueue(mockResponse)
        val fileSizeChecker = FileSizeChecker(OkHttpClient())

        //WHEN THEN
        assertThrows(NumberFormatException::class.java){ fileSizeChecker.getFileSize(mockWebServer.url("nix").url())}
    }
}