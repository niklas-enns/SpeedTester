package niklasu.speedtester

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.inject.Guice
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJackson
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
        JavalinJackson.configure(jacksonObjectMapper().registerModule(JavaTimeModule()))
        val injector = Guice.createInjector(ConfigModule(args), DownloaderModule(), MeasurementsModule())
        app.get("/measurements") { ctx -> ctx.json(injector.getInstance(Measurements::class.java).measurements) }
        app.get("/config") { ctx -> ctx.json(injector.getInstance(ConfigProvider::class.java)) }
        injector.getInstance(DownloadScheduler::class.java).start()
    }
}
