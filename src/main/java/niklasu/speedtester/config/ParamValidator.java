package niklasu.speedtester.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

import static niklasu.speedtester.Constants.MB;

@Singleton
public class ParamValidator {
    private final static Logger logger = LoggerFactory.getLogger(ParamValidator.class);
    private final FileSizeChecker fileSizeChecker;

    @Inject
    public ParamValidator(FileSizeChecker fileSizeChecker) {
        this.fileSizeChecker = fileSizeChecker;
    }

    public void validateParams(long requiredFileSize, int interval, String url) throws ValidationException {
        logger.debug("Validating params: {}MB,  {}minutes interval, url: {}", requiredFileSize, interval, url);
        if (requiredFileSize <1) throw new ValidationException("download size must be >=1");
        long realFileSize;
        try {
            realFileSize = fileSizeChecker.getFileSize(new URL(url));
        } catch (MalformedURLException e) {
            throw new ValidationException("Malformed URL. It has to start with http://",e);
        }
        if (realFileSize < MB) throw new ValidationException(String.format("The size of %s was %d and is < %d", url, realFileSize, MB));
        if (realFileSize < requiredFileSize * MB) throw new ValidationException(String.format("%s has a size of %d while %d is required", url, realFileSize, requiredFileSize*MB));

        if (interval < 1) throw new ValidationException(String.format("Interval must be >= 1 but it was %d", interval));
    }
}
