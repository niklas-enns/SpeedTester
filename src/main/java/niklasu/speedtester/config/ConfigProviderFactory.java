package niklasu.speedtester.config;

import java.util.Arrays;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigProviderFactory {
    private static final Logger logger = LoggerFactory.getLogger("ConfigModule");

    public static ConfigProvider getConfigProvider(String[] args) throws ValidationException {
        var url = System.getenv("URL");
        if (url == null) {
            url = getString(args, "-url");
        }

        var interval = 0;
        var intervalAsString = System.getenv("INTERVAL");
        if (intervalAsString != null) {
            interval = Integer.parseInt(intervalAsString);
        } else {
            interval = getInt(args, "-interval");
        }

        var size = 0;
        var sizeAsString = System.getenv("SIZE");
        if (sizeAsString != null) {
            size = Integer.parseInt(sizeAsString);
        } else {
            size = getInt(args, "-size");
        }

        String influxUrl = null;
        String influxToken = null;
        String influxOrg = null;
        String influxBucket = null;
        try {
            influxUrl = System.getenv("INFLUX_URL");
            if (influxUrl == null) {
                influxUrl = getString(args, "-influx-url");
            }

            influxToken = System.getenv("INFLUX_TOKEN");
            if (influxToken == null) {
                influxToken = getString(args, "-influx-token");
            }

            influxOrg = System.getenv("INFLUX_ORG");
            if (influxOrg == null) {
                influxOrg = getString(args, "-influx-org");
            }

            influxBucket = System.getenv("INFLUX_BUCKET");
            if (influxBucket == null) {
                influxBucket = getString(args, "-influx-bucket");
            }
        } catch (Exception e) {
            logger.info("InfluxDB config broken but let's ignore that for now and just measure", e);
            influxUrl = influxToken = influxOrg = influxBucket = "";
        }

        var configProvider = new ConfigProvider(size, interval, url, influxUrl, influxToken, influxOrg, influxBucket);

        var paramValidator = new ParamValidator(new FileSizeChecker(new OkHttpClient()));

        paramValidator.validate(configProvider.size(), configProvider.url());
        logger.info(configProvider.toString());
        return configProvider;
    }

    private static int getInt(final String[] args, final String searchTerm) {
        return Integer.parseInt(getString(args, searchTerm));
    }

    private static String getString(final String[] args, final String searchTerm) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(searchTerm)) {
                return args[i + 1];
            }
        }
        throw new RuntimeException(searchTerm + " not found in " + Arrays.toString(args));
    }
}
