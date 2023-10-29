package niklasu.speedtester.influx;

import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import niklasu.speedtester.config.ConfigProvider;
import niklasu.speedtester.influx.InfluxModule.InfluxWriter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

class InfluxConnector implements InfluxWriter {
    private final ConfigProvider configProvider;
    private final WriteApiBlocking api;

    public InfluxConnector(WriteApiBlocking api, ConfigProvider configProvider) {
        this.configProvider = configProvider;
        this.api = api;
    }

    @Override
    public void write(final double speed) throws UnknownHostException {
        Point point1 = Point.measurement("speed")
                .time(Instant.now(), WritePrecision.MS)
                .addField("value", speed)
                .addField("download_size", configProvider.size())
                .addTag("hostname", InetAddress.getLocalHost().getHostName())
                .addTag("download_url", configProvider.url());
        api.writePoint(point1);
    }
}
