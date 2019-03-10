package niklasu.speedtester.downloader

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.google.inject.Inject
import niklasu.speedtester.config.ConfigProvider
import niklasu.speedtester.events.StartEvent
import niklasu.speedtester.events.StopEvent
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit.MINUTES


class DownloadScheduler @Inject
constructor(eventBus: EventBus, private val downloadThreadFactory: DownloadThreadFactory, private val scheduler: ScheduledExecutorService, private val configProvider: ConfigProvider) {
    private var scheduledFuture: ScheduledFuture<*>? = null

    init {
        eventBus.register(this)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DownloadScheduler::class.java)
    }

    @Subscribe
    fun stopEventHandler(stopEvent: StopEvent) {
        logger.info("Stopped scheduling downloads")
        scheduledFuture!!.cancel(false)
    }

    @Subscribe
    fun startEventHandler(startEvent: StartEvent) {
        scheduledFuture = scheduler.scheduleAtFixedRate(
                downloadThreadFactory.getInstance(),
                0,
                configProvider.interval.toLong(),
                MINUTES)
    }
}
