package niklasu.speedtester.influx

import niklasu.speedtester.config.ConfigProvider
import niklasu.speedtester.influx.InfluxModule.InfluxWriter
import org.influxdb.InfluxDB
import org.influxdb.dto.Point
import java.net.InetAddress
import java.util.concurrent.TimeUnit

class InfluxConnector(private val influx: InfluxDB, private val configProvider: ConfigProvider) : InfluxWriter {
    override fun write(speed: Double) {
        val point1: Point = Point.measurement("speed")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("value", speed)
                .addField("download_size", configProvider.size)
                .tag("hostname", InetAddress.getLocalHost().hostName)
                .tag("download_url", configProvider.url)
                .build()
        influx.write(point1)
    }
}