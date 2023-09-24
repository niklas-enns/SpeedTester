package niklasu.speedtester.config;

import com.google.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;

class FileSizeChecker {
    private final OkHttpClient client;

    @Inject
    FileSizeChecker(OkHttpClient client) {
        this.client = client;
    }

    /**
     * @return file size given by the Content-Length response header
     * @throws IOException
     * @throws NumberFormatException
     */
    long getFileSize(URL url) throws IOException, ValidationException, NoFileSizeException {
        final Request request = new Request.Builder()
                .url(url)
                .head()
                .build();

        final Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new ValidationException("Unexpected code $response");
        }
        String contentLength = response.headers().get("Content-Length");
        if (contentLength == null) {
            throw new NoFileSizeException("Unable to determine URL file size. Content-Length header missing");
        }
        return Long.parseLong(contentLength);
    }

    static class NoFileSizeException extends Exception {
        public NoFileSizeException(final String string) {
        }

    }
}

