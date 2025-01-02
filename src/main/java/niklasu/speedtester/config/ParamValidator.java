package niklasu.speedtester.config;

import static niklasu.speedtester.Constants.MB;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

class ParamValidator {

    private final FileSizeChecker fileSizeChecker;

    ParamValidator(FileSizeChecker fileSizeChecker) {
        this.fileSizeChecker = fileSizeChecker;
    }

    void validate(Long size, String url) throws ValidationException {
        if (size < 1) {
            throw new ValidationException("download size must be >=1");
        }
        long realFileSize;
        try {
            realFileSize = fileSizeChecker.getFileSize(new URL(url));
        } catch (MalformedURLException e) {
            throw new ValidationException("Malformed URL. It has to start with http://", e);
        } catch (NumberFormatException | IOException | FileSizeChecker.NoFileSizeException e) {
            throw new ValidationException("Unable to get the file size for " + url, e);
        }
        if (realFileSize < MB) {
            throw new ValidationException("The size of " + url + " was " + realFileSize + " and is < " + MB + " byte");
        }
        if (realFileSize < size) {
            throw new ValidationException(
                    url + " has a size of " + realFileSize + " while " + size * MB + " is required");
        }
    }
}
