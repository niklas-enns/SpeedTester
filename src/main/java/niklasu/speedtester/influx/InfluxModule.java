package niklasu.speedtester.influx;

import java.net.UnknownHostException;
import java.util.Objects;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import niklasu.speedtester.config.ConfigProvider;

public class InfluxModule {

    public static InfluxWriter getInfluxWriter(ConfigProvider configProvider) {
        String influxUrl = configProvider.influxHost();
        if (Objects.equals(influxUrl, "")) {
            return speed -> {
                // noop
            };
        }
        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(influxUrl,
                configProvider.influxToken().toCharArray(), configProvider.influxOrg(), configProvider.influxBucket());
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        return new InfluxConnector(writeApi, configProvider);
    }

    public interface InfluxWriter {
        void write(double speed) throws UnknownHostException;
    }

}