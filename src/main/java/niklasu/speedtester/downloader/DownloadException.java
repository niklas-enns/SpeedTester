package niklasu.speedtester.downloader;

import java.io.IOException;

class DownloadException extends Exception {
    public DownloadException(final String s, final IOException e) {
        super(s,e);
    }
}

