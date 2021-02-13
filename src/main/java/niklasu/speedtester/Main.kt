package niklasu.speedtester

import com.google.inject.Guice
import niklasu.speedtester.config.ConfigModule
import niklasu.speedtester.downloader.DownloadScheduler
import niklasu.speedtester.downloader.DownloaderModule
import niklasu.speedtester.influx.InfluxModule

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val injector = Guice.createInjector(ConfigModule(args), DownloaderModule(), InfluxModule())
        injector.getInstance(DownloadScheduler::class.java).start()
    }
}
