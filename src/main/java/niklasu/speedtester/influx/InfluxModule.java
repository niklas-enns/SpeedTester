package niklasu.speedtester.influx;

import java.net.UnknownHostException;

import niklasu.speedtester.config.ConfigProvider;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

public class InfluxModule{

    public static InfluxWriter getInfluxWriter(ConfigProvider configProvider){
        String influxHost = configProvider.influxHost;
        if (influxHost == "") {
            return speed -> {
                // noop
            };
        }
        InfluxDB influxDB = InfluxDBFactory.connect(influxHost);
        influxDB.setDatabase("speedtester");
        return new InfluxConnector(influxDB, configProvider);
    }

    public interface InfluxWriter {
        void write(double speed) throws UnknownHostException;
    }

}