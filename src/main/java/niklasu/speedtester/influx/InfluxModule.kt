package niklasu.speedtester.influx

import com.google.inject.AbstractModule
import com.google.inject.Provides
import niklasu.speedtester.config.ConfigProvider
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory

class InfluxModule : AbstractModule() {

    @Provides
    private fun getInfluxConnector(configProvider: ConfigProvider): InfluxWriter {
        val influxHost = configProvider.influxHost
        if (influxHost == "") {
            return object : InfluxWriter {
                override fun write(speed: Double) {
                    //noop
                }
            }
        }
        val influxDB = InfluxDBFactory.connect(influxHost)
        influxDB.setDatabase("speedtester")
        return InfluxConnector(influxDB, configProvider)
    }

    interface InfluxWriter {
        fun write(speed: Double)
    }

}