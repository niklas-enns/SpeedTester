package niklasu.speedtester.downloader;

/**
 * Created by enzo on 24.08.2015.
 */
public class DownloadException extends Exception {

    public DownloadException(String s) {
        super(s);
    }

    public DownloadException(String message, Exception e){
        super(message, e);
    }
}
