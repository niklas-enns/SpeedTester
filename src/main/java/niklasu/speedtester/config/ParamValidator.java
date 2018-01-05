package niklasu.speedtester.config;

import com.google.inject.Singleton;
import niklasu.speedtester.exceptions.BadFileException;
import niklasu.speedtester.exceptions.ValidationException;
import niklasu.speedtester.interfaces.Constants;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
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
        } catch (BadFileException | MalformedURLException e) {
            throw new ValidationException("",e);
        }
        if (realFileSize < 1*MB) throw new ValidationException(String.format("The size of %s was %d and is < %d", url, realFileSize, 1*MB));
        if (realFileSize < requiredFileSize * MB) throw new ValidationException(String.format("%s has a size of %d while %d is required", url, realFileSize, requiredFileSize*MB));
    }

    private long getFileSize(URL targetFile) throws BadFileException {
        HttpClient client = new DefaultHttpClient();
        HttpHead head = new HttpHead(String.valueOf(targetFile));
        HttpResponse response = null;
        try {
            response = client.execute(head);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Header[] size = response.getHeaders("Content-length");
        if (size.length == 0) throw new BadFileException("The HTTP response has no Content-Length field");
        logger.debug("Size of {} is {} bytes", targetFile, size[0].getValue());
        String contentLength = size[0].getValue().replaceAll("[^\\d.]", ""); //delete all non-numbers
        long contentLengthAsInt = Long.parseLong(contentLength);
        return contentLengthAsInt;
    }
}
