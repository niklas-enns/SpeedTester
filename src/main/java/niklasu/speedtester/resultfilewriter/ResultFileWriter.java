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

    private final EventBus eventBus;

    @Inject
    public ResultFileWriter(EventBus eventBus) {
        logger.trace("Constructing ResultFileWriter");
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Subscribe
    public void appendToLogFile(ResultEvent result) {
        logger.debug(result.toString());
        resultLogger.info(result.toString());
    }
}
