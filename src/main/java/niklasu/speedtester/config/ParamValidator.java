package niklasu.speedtester.config;

import niklasu.speedtester.downloader.DownloadFileSizeChecker;
import niklasu.speedtester.exceptions.BadFileException;
import niklasu.speedtester.exceptions.TooSmallFileException;
import niklasu.speedtester.interfaces.Constants;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class ParamValidator implements Constants {
    static ApplicationContext context;

    public ParamValidator(ApplicationContext context) {
        this.context = context;
    }

    public static boolean validateParams(long requiredFileSize, int interval, String url) throws MalformedURLException, TooSmallFileException, BadFileException {
        long realFileSize = context.getBean(DownloadFileSizeChecker.class).getFileSize(new URL(url)); //Throws bad URL exceptions
        if (realFileSize < requiredFileSize)
            throw new TooSmallFileException("The target file is smaller than the required download size", realFileSize, requiredFileSize);
        return false;
    }


}
