package niklasu.speedtester.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static niklasu.speedtester.Constants.MB;

@Singleton
public class JavaParamValidator implements ParamValidator {
    private final static Logger logger = LoggerFactory.getLogger(JavaParamValidator.class);
    private final FileSizeChecker fileSizeChecker;

    @Inject
    public JavaParamValidator(FileSizeChecker fileSizeChecker) {
        this.fileSizeChecker = fileSizeChecker;
    }

    @Override
    public void validate(Config config) throws ValidationException {
        logger.debug("Validating {}", config);
        if (config.getFileSize() <1) throw new ValidationException("download size must be >=1");
        long realFileSize = 0;
        try {
            final URL url = new URL(config.getUrl());
            fileSizeChecker.getFileSize(url);
        } catch (MalformedURLException e) {
            throw new ValidationException("Malformed URL. It has to start with http://", e);
        } catch (NumberFormatException | IOException e) {
            throw new ValidationException("Unable to get the file size for " + config.getUrl());
        }

        if (realFileSize < MB) throw new ValidationException(String.format("The size of %s was %d and is < %d", config.getUrl(), realFileSize, MB));
        if (realFileSize < config.getFileSize() * MB) throw new ValidationException(String.format("%s has a size of %d while %d is required", config.getUrl(), realFileSize, config.getFileSize()*MB));

        if (config.getInterval() < 1) throw new ValidationException(String.format("Interval must be >= 1 but it was %d", config.getInterval()));
    }
}
