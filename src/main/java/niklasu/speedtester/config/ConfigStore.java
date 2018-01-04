package niklasu.speedtester.config;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.eventbus.EventBus;
import niklasu.speedtester.DownloadService;
import niklasu.speedtester.exceptions.BadFileException;
import niklasu.speedtester.exceptions.TooSmallFileException;
import niklasu.speedtester.interfaces.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;

@Component
public class ConfigStore implements Constants {
    @Autowired
    ParamValidator paramValidator;
    @Autowired
    EventBus eventBus;

    @Parameter(names = "-size", description = "Download size in MB", required = false)
    private int size = 50;
    @Parameter(names = "-interval", description = "Download interval in minutes", required = false)
    private int interval = 1;
    @Parameter(names = "-url", description = "file URL", required = true)
    private String url = "";
    @Parameter(names = "-tray", description = "Debug mode", arity = 1)
    private boolean tray = true;

    @PostConstruct
    public void parseArgs() throws BadFileException, TooSmallFileException, MalformedURLException {
        new JCommander(this, DownloadService.myargs);
        paramValidator.validateParams(size, interval, url);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isTray() {
        return tray;
    }

    public void setTray(boolean tray) {
        this.tray = tray;
    }

    public String toString() {
        return "" + size + " " + interval + " " + url;
    }

    public void reset() {
        url = "http://ftp.halifax.rwth-aachen.de/opensuse/distribution/13.2/iso/openSUSE-13.2-DVD-i586.iso";
        size = 50;
        interval = 1;
    }
}
