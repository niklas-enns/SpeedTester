package niklasu.speedtester.influx;

import niklasu.speedtester.config.ConfigProvider;
import niklasu.speedtester.influx.InfluxModule.InfluxWriter;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

class InfluxConnector implements InfluxWriter {
    private final ConfigProvider configProvider;
    private final InfluxDB influxDB;

    public InfluxConnector(InfluxDB influxDB, ConfigProvider configProvider) {
        this.configProvider = configProvider;
        this.influxDB = influxDB;
    }

    @Override
    public void write(final double speed) throws UnknownHostException {
        Point point1 = Point.measurement("speed")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("value", speed)
                .addField("download_size", configProvider.size())
                .tag("hostname", InetAddress.getLocalHost().getHostName())
                .tag("download_url", configProvider.url())
                .build();
        influxDB.write(point1);
    }
}
