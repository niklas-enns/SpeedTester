package niklasu.speedtester.config;

import niklasu.speedtester.downloader.DownloadFileSizeChecker;
import niklasu.speedtester.exceptions.BadFileException;
import niklasu.speedtester.exceptions.TooSmallFileException;
import niklasu.speedtester.interfaces.Constants;

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
