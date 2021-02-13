package niklasu.speedtester.config

import com.beust.jcommander.Parameter
import com.fasterxml.jackson.annotation.JsonProperty

class ConfigProvider {
    @Parameter(names = ["-size"], description = "Download size in MB")
    @JsonProperty("downloadSizeMB")
    var size: Long = 50

    @Parameter(names = ["-interval"], description = "Download interval in minutes")
    @JsonProperty("downloadIntervalMinutes")
    var interval = 1

    @Parameter(names = ["-url"], description = "file URL", required = true)
    var url = ""
    @Parameter(names = ["-influx-host"], description = "Enables pushing data to influxDB")
    var influxHost=""

}
