package niklasu.speedtester.resultfilewriter;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import niklasu.speedtester.events.ResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultFileWriter {
    private final static Logger logger = LoggerFactory.getLogger(ResultFileWriter.class);
    private final static Logger resultLogger = LoggerFactory.getLogger("resultlogger");

    private EventBus eventBus;

    @Inject
    public ResultFileWriter(EventBus eventBus) {
        logger.trace("Constructing ResultFileWriter");
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Subscribe
    public void appendToLogFile(ResultEvent result) {
        logger.debug("Received ResultEvent {} {}", result.getDate(), result.getSpeed());
        resultLogger.info("{} {} Mbit/s", result.getDate(), String.format("%.2f", result.getSpeed()));
    }
}
