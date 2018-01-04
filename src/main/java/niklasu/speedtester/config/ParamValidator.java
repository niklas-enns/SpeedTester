package niklasu.speedtester.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import niklasu.speedtester.downloader.DownloadFileSizeChecker;
import niklasu.speedtester.exceptions.BadFileException;
import niklasu.speedtester.exceptions.TooSmallFileException;
import niklasu.speedtester.interfaces.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

@Singleton
public class ParamValidator implements Constants {
    private final static Logger logger = LoggerFactory.getLogger(ParamValidator.class);
    private DownloadFileSizeChecker downloadFileSizeChecker;

    @Inject
    public ParamValidator(DownloadFileSizeChecker downloadFileSizeChecker) {
        this.downloadFileSizeChecker = downloadFileSizeChecker;
    }

    public boolean validateParams(long requiredFileSize, int interval, String url) throws MalformedURLException, TooSmallFileException, BadFileException {
        logger.debug("Validating params: {} {} {}", requiredFileSize, interval, url);
        long realFileSize = downloadFileSizeChecker.getFileSize(new URL(url));
        if (realFileSize < requiredFileSize)
            throw new TooSmallFileException("The target file is smaller than the required download size", realFileSize, requiredFileSize);
        return false;
    }
}
