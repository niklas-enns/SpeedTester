package niklasu.speedtester.config;

import com.google.inject.Singleton;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static niklasu.speedtester.Constants.MB;

@Singleton
public class ParamValidator {
    private final static Logger logger = LoggerFactory.getLogger(ParamValidator.class);

    public void validateParams(long requiredFileSize, int interval, String url) throws ValidationException {
        logger.debug("Validating params: {}MB,  {}minutes interval, url: {}", requiredFileSize, interval, url);
        if (requiredFileSize <1) throw new ValidationException("download size must be >=1");
        long realFileSize;
        try {
            realFileSize = getFileSize(new URL(url));
        } catch (MalformedURLException e) {
            throw new ValidationException("Malformed URL. It has to start with http://",e);
        }
        if (realFileSize < MB) throw new ValidationException(String.format("The size of %s was %d and is < %d", url, realFileSize, MB));
        if (realFileSize < requiredFileSize * MB) throw new ValidationException(String.format("%s has a size of %d while %d is required", url, realFileSize, requiredFileSize*MB));

        if (interval < 1) throw new ValidationException(String.format("Interval must be >= 1 but it was %d", interval));
    }

    private long getFileSize(URL targetFile) throws ValidationException {
        final OkHttpClient client = new OkHttpClient();
        Request request =  new Request.Builder()
                .url(targetFile)
                .head()
                .build();
        Headers responseHeaders;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new ValidationException("Unexpected code " + response);
            responseHeaders = response.headers();
        }catch (IOException | NumberFormatException e){
            throw new ValidationException(String.format("Error while checking the file size of %s", targetFile),e);
        }
        String length = responseHeaders.get("Content-Length");
        try{
            return Long.parseLong(length);
        }catch (NumberFormatException e) {
            throw new ValidationException(String.format("%s was assumend to be the value for the \"Content-Length\" Header and could not be parsed", length));
        }
    }
}
