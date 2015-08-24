package downloadTestService;

import downloadTestService.exceptions.BadFileException;
import downloadTestService.exceptions.TooSmallFileException;
import downloadTestService.interfaces.Constants;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by enzo on 24.08.2015.
 */
public class ParamValidator implements Constants {
    public static boolean validateParams(long requiredFileSize, int interval, String url) throws MalformedURLException, TooSmallFileException, BadFileException {
        long realFileSize = new DownloadFileSizeChecker().getFileSize(new URL(url)); //Throws bad URL exceptions
        if (realFileSize < requiredFileSize)
            throw new TooSmallFileException("The target file is smaller than the required download size", realFileSize, requiredFileSize);
        return false;
    }


}
