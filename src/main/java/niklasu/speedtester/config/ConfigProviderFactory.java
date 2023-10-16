package niklasu.speedtester.config;

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
        var configProvider = new ConfigProvider(size, interval, url, "");

        var paramValidator = new ParamValidator(new FileSizeChecker(new OkHttpClient()));

        paramValidator.validate(configProvider.size(), configProvider.interval(), configProvider.url());
        logger.info(configProvider.toString());
        return configProvider;
    }

    private static int getInt(final String[] args, final String searchTerm) {
        return Integer.parseInt(getString(args, searchTerm));
    }

    private static String getString(final String[] args, final String searchTerm) {
        int indexOfSearchTerm = -1;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(searchTerm)) {
                indexOfSearchTerm = i;
            }
        }
        return args[indexOfSearchTerm + 1];
    }
}
