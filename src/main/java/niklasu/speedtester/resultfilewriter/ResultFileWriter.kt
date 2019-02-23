package niklasu.speedtester.resultfilewriter

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.google.inject.Inject
import niklasu.speedtester.events.ResultEvent
import org.slf4j.LoggerFactory

class ResultFileWriter @Inject
constructor(eventBus: EventBus) {

    init {
        logger.trace("Constructing ResultFileWriter")
        eventBus.register(this)
    }

    @Subscribe
    fun appendToLogFile(result: ResultEvent) {
        logger.debug(result.toString())
        resultLogger.info(result.toString())
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ResultFileWriter::class.java)
        private val resultLogger = LoggerFactory.getLogger("resultlogger")
    }
}
