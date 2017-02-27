package niklasu.speedtester.config;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.eventbus.EventBus;
import niklasu.speedtester.DownloadService;
import niklasu.speedtester.events.ConfigChangedEvent;
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
    @Parameter(names = "-url", description = "file URL", required = false)
    private String url = "http://ftp.halifax.rwth-aachen.de/opensuse/distribution/13.2/iso/openSUSE-13.2-DVD-i586.iso";
    @Parameter(names = "-tray", description = "Debug mode", arity = 1)
    private boolean tray = true;
    private boolean running = true;

    @PostConstruct
    public void parseArgs() throws BadFileException, TooSmallFileException, MalformedURLException {
        new JCommander(this, DownloadService.myargs);
        paramValidator.validateParams(size, interval, url);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) throws TooSmallFileException, MalformedURLException, BadFileException {
        paramValidator.validateParams(size, interval, url);
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

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getDefaultSize() {
        return 50;
    }

    public int getDetaultInterval() {
        return 1;
    }

    public String getDefaultUrl() {
        return "http://ftp.halifax.rwth-aachen.de/opensuse/distribution/13.2/iso/openSUSE-13.2-DVD-i586.iso";
    }

    public void fireConfigChangedEvent() {
        eventBus.post(new ConfigChangedEvent());
    }
}
