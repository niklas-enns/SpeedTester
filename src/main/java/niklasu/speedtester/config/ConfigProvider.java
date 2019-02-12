package niklasu.speedtester.config;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import niklasu.speedtester.events.ConfigChangedEvent;
@Singleton
public class ConfigProvider {
    private ParamValidator paramValidator;
    private EventBus eventBus;
    @Parameter(names = "-size", description = "Download size in MB")
    @Getter
    private long size = 50;
    @Parameter(names = "-interval", description = "Download interval in minutes")
    @Getter
    private int interval = 1;
    @Parameter(names = "-url", description = "file URL", required = true)
    @Getter
    private String url = "";
    @Parameter(names = "-tray", description = "Debug mode", arity = 1)
    @Getter
    private boolean tray = true;

    @Inject
    public ConfigProvider(ParamValidator paramValidator, EventBus eventBus) {
        this.paramValidator = paramValidator;
        this.eventBus = eventBus;
    }

    public void setConfig(String[] args) throws ValidationException {
        new JCommander(this, args);
        paramValidator.validate(new Config(size, interval, url));
    }

    public void setConfig(Config config) throws ValidationException {
        paramValidator.validate(config);
        this.size = config.getFileSize();
        this.interval = config.getInterval();
        this.url = config.getUrl();
        eventBus.post(new ConfigChangedEvent());
    }

    public void reset() {
        size = 50;
        interval = 1;
        eventBus.post(new ConfigChangedEvent());
    }
}
