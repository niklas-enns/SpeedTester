package downloadTestService;

import com.beust.jcommander.Parameter;

/**
 * Created by enzo on 20.08.2015.
 */
public class DownloadServiceArguments {

    @Parameter(names = "-size", description = "Download size in MB", required = false)
    private int size = 50;

    @Parameter(names = "-interval", description = "Download interval in minutes", required = false)
    private int interval = 1;

    @Parameter(names = "-url", description = "file URL", required = false)
    private String url = "http://ftp.halifax.rwth-aachen.de/opensuse/distribution/13.2/iso/openSUSE-13.2-DVD-i586.iso";

    @Parameter(names = "-tray", description = "Debug mode", arity = 1)
    private boolean tray = false;


    public int getSize() {
        return size;
    }

    public int getInterval() {
        return interval;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getTray() {
        return tray;
    }

    public String toString() {
        return "" + size + " " + interval + " " + url;
    }
}
