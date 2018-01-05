package niklasu.speedtester.config;

import com.google.inject.Singleton;
import niklasu.speedtester.exceptions.ValidationException;
import niklasu.speedtester.interfaces.Constants;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Singleton
public class ParamValidator implements Constants {
    private final static Logger logger = LoggerFactory.getLogger(ParamValidator.class);

    public void validateParams(long requiredFileSize, int interval, String url) throws ValidationException {
        logger.debug("Validating params: {}MB,  {}minutes interval, url: {}", requiredFileSize, interval, url);
        if (requiredFileSize <1) throw new ValidationException("download size must be >=1");
        long realFileSize = 0;
        try {
            realFileSize = getFileSize(new URL(url));
        } catch (MalformedURLException e) {
            throw new ValidationException("",e);
        }
        if (realFileSize < 1*MB) throw new ValidationException(String.format("The size of %s was %d and is < %d", url, realFileSize, 1*MB));
        if (realFileSize < requiredFileSize * MB) throw new ValidationException(String.format("%s has a size of %d while %d is required", url, realFileSize, requiredFileSize*MB));
    }

    private long getFileSize(URL targetFile) throws ValidationException {
        final OkHttpClient client = new OkHttpClient();
        Request request =  new Request.Builder()
                .url(targetFile)
                .head()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new ValidationException("Unexpected code " + response);
            Headers responseHeaders = response.headers();
            return Long.parseLong(responseHeaders.value(3));
        }catch (IOException | NumberFormatException e){
            throw new ValidationException("",e);
        }
    }
}
