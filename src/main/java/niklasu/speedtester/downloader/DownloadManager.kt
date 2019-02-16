package niklasu.speedtester.downloader

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.google.inject.Inject
import niklasu.speedtester.config.ConfigProvider
import niklasu.speedtester.events.ConfigChangedEvent
import niklasu.speedtester.events.StartEvent
import niklasu.speedtester.events.StopEvent
import org.slf4j.LoggerFactory
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit.MINUTES

/**
 * Schedules downloads and
 */
class DownloadManager @Inject
constructor(eventBus: EventBus, private val configProvider: ConfigProvider, private val downloadThread: DownloadThread, private val scheduler: ScheduledExecutorService) {
    private var scheduledFuture: ScheduledFuture<*>? = null

    init {
        eventBus.register(this)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DownloadManager::class.java)
    }

    @Subscribe
    @Throws(MalformedURLException::class)
    fun configChangedHandler(configChangedEvent: ConfigChangedEvent) {
        logger.trace("configChangeHandler")
        downloadThread.setSize(configProvider.size)
        downloadThread.setUrl(URL(configProvider.url))
    }

    @Subscribe
    fun stopEventHandler(stopEvent: StopEvent) {
        logger.debug("stopEvent")
        logger.info("Stopped scheduling downloads. If there was already a download running while stopping, it will be finished.")
        scheduledFuture!!.cancel(false)
    }

    @Subscribe
    fun startEventHandler(startEvent: StartEvent) {
        scheduledFuture = scheduler.scheduleAtFixedRate(downloadThread, 0, configProvider.interval.toLong(), MINUTES)
    }
}
