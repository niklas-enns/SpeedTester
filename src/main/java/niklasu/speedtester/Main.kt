package niklasu.speedtester

import com.google.inject.Guice
import io.javalin.Javalin
import niklasu.speedtester.config.ConfigModule
import niklasu.speedtester.config.ConfigProvider
import niklasu.speedtester.downloader.DownloadScheduler
import niklasu.speedtester.downloader.DownloaderModule
import niklasu.speedtester.measurements.Measurements
import niklasu.speedtester.measurements.MeasurementsModule

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val app = Javalin.create { it.addStaticFiles("/public") }.start(7000)
        val injector = Guice.createInjector(ConfigModule(args), DownloaderModule(), MeasurementsModule())
        app.get("/measurements") { ctx -> ctx.result(injector.getInstance(Measurements::class.java).measurements.toString()) }
        app.get("/config") { ctx -> ctx.json(injector.getInstance(ConfigProvider::class.java)) }
        injector.getInstance(DownloadScheduler::class.java).start()
    }
}
