package niklasu.speedtester.config;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigProviderFactory {
    private static final Logger logger = LoggerFactory.getLogger("ConfigModule");

    public static ConfigProvider getConfigProvider(String[] args) throws ValidationException {
        var url = getString(args, "-url");
        var interval = getInt(args, "-interval");
        var size = getInt(args, "-size");
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
