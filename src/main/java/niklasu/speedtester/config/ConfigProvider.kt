package niklasu.speedtester.config

import com.beust.jcommander.Parameter

class ConfigProvider {
    @Parameter(names = ["-size"], description = "Download size in MB")
    var size: Long = 50

    @Parameter(names = ["-interval"], description = "Download interval in minutes")
    var interval = 1

    @Parameter(names = ["-url"], description = "file URL", required = true)
    var url = ""
    @Parameter(names = ["-influx-host"], description = "Enables pushing data to influxDB")
    var influxHost=""

}
