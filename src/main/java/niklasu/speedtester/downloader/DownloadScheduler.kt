package niklasu.speedtester.downloader

import com.google.inject.Inject
import niklasu.speedtester.config.ConfigProvider
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.MINUTES


class DownloadScheduler @Inject
constructor(private val downloadThread: DownloadThread, private val configProvider: ConfigProvider) {
    fun start() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                downloadThread,
                0,
                configProvider.interval.toLong(),
                MINUTES)
    }
}
