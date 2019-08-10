package niklasu.speedtester.downloader

import com.google.inject.Inject
import niklasu.speedtester.config.ConfigProvider
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit.MINUTES


class DownloadScheduler @Inject
constructor(private val downloadThread: DownloadThread, private val scheduler: ScheduledExecutorService, private val configProvider: ConfigProvider) {
    fun start() {
        scheduler.scheduleAtFixedRate(
                downloadThread,
                0,
                configProvider.interval.toLong(),
                MINUTES)
    }
}
