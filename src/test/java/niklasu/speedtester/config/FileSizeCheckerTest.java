package niklasu.speedtester.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.stream.IntStream;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class FileSizeCheckerTest {

    @Test
    @DisplayName("gets file size from headers correctly")
    void getFileSize() throws FileSizeChecker.NoFileSizeException, ValidationException, IOException {
        //GIVEN
        MockWebServer mockWebServer = new MockWebServer();
        String responseBody = "0123456789";
        MockResponse mockResponse = new MockResponse().setBody(responseBody);
        mockWebServer.enqueue(mockResponse);
        FileSizeChecker fileSizeChecker = new FileSizeChecker(new OkHttpClient());

        //WHEN THEN
        assertEquals(responseBody.length(), fileSizeChecker.getFileSize(mockWebServer.url("nix").url()));
    }

    @ParameterizedTest
    @MethodSource("invalidResponseCodes")
    @DisplayName("Throws exception on response code 400 and 404")
    void get404(int number) {
        //GIVEN
        MockWebServer mockWebServer = new MockWebServer();
        MockResponse mockResponse = new MockResponse().setResponseCode(number);
        mockWebServer.enqueue(mockResponse);
        FileSizeChecker fileSizeChecker = new FileSizeChecker(new OkHttpClient());

        //WHEN THEN
        assertThrows(ValidationException.class, () -> fileSizeChecker.getFileSize(mockWebServer.url("nix").url()));
    }

    private static IntStream invalidResponseCodes() {
        return IntStream.of(400, 404);
    }

    @Test
    void ioException() {
        //GIVEN
        MockWebServer mockWebServer = new MockWebServer();
        MockResponse mockResponse =
                new MockResponse().setBody("1234").setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mockWebServer.enqueue(mockResponse);
        FileSizeChecker fileSizeChecker = new FileSizeChecker(new OkHttpClient());

        //WHEN THEN
        assertThrows(IOException.class, () ->
                fileSizeChecker.getFileSize(mockWebServer.url("nix").url()));

    }

    @Test
    @DisplayName("Content-Length: noNumber")
    void contentLengthHeaderHasNoNumericalValue() {
        //GIVEN
        MockWebServer mockWebServer = new MockWebServer();
        MockResponse mockResponse = new MockResponse().setHeader("Content-Length", "noNumber");
        mockWebServer.enqueue(mockResponse);
        FileSizeChecker fileSizeChecker = new FileSizeChecker(new OkHttpClient());

        //WHEN THEN
        assertThrows(NumberFormatException.class, () ->
                fileSizeChecker.getFileSize(mockWebServer.url("nix").url()));
    }
}
