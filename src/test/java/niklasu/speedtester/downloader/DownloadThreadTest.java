package niklasu.speedtester.downloader;

import java.net.MalformedURLException;
import java.net.URL;

import static niklasu.speedtester.interfaces.Constants.MB;

class DownloadThreadTest {
    private DownloadThread dlt;
    @org.junit.jupiter.api.BeforeEach
    void setUp() throws MalformedURLException {
        dlt = new DownloadThread(100*MB, new URL("http://ftp.halifax.rwth-aachen.de/opensuse/distribution/13.2/iso/openSUSE-13.2-DVD-i586.iso"), null);
    }

    @org.junit.jupiter.api.Test
    void run() {
        dlt.run();
    }

}