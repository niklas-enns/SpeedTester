package niklasu.speedtester.config;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import niklasu.speedtester.exceptions.BadFileException;
import niklasu.speedtester.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

@Singleton
public class ConfigStore{
    private final static Logger logger = LoggerFactory.getLogger(ConfigStore.class);
    private ParamValidator paramValidator;
    private EventBus eventBus;
    @Parameter(names = "-size", description = "Download size in MB", required = false)
    private int size = 50;
    @Parameter(names = "-interval", description = "Download interval in minutes", required = false)
    private int interval = 1;
    @Parameter(names = "-url", description = "file URL", required = true)
    private String url = "";
    @Parameter(names = "-tray", description = "Debug mode", arity = 1)
    private boolean tray = true;

    @Inject
    public ConfigStore(ParamValidator paramValidator, EventBus eventBus) {
        logger.trace("Constructing Configstore");
        this.paramValidator = paramValidator;
        this.eventBus = eventBus;
    }

    public void parseArgs(String[] args) throws BadFileException, ValidationException, MalformedURLException {
        new JCommander(this, args);
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
        size = 50;
        interval = 1;
    }
}
