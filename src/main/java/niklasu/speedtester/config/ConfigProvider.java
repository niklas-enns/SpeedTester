package niklasu.speedtester.config;

import com.beust.jcommander.Parameter;

public class ConfigProvider {
    @Parameter(names = { "-size" }, description = "Download size in MB")
    public Long size = 50L;

    @Parameter(names = {"-interval"}, description = "Download interval in minutes")
    public int interval = 1;

    @Parameter(names = {"-url"}, description = "file URL", required = true)
    public String url = "";
    @Parameter(names = {"-influx-host"}, description = "Enables pushing data to influxDB")
    public String influxHost="";

}
