package niklasu.speedtester.config;

import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.JCommander;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigProviderFactory {
    private static final Logger logger = LoggerFactory.getLogger("ConfigModule");

    public static ConfigProvider getConfigProvider(String[] args) throws ValidationException {
        final List<String> argsWithUrl = Arrays.asList(args);
        if (!argsWithUrl.contains("-url")) {
            argsWithUrl.add("-url");
            argsWithUrl.add(System.getenv("DOWNLOAD_URL"));
        }
        final ConfigProvider configProvider = new ConfigProvider();
        new JCommander(configProvider, argsWithUrl.toArray(new String[0]));

        ParamValidator paramValidator = new ParamValidator(new FileSizeChecker(new OkHttpClient()));

        paramValidator.validate(configProvider.size, configProvider.interval, configProvider.url);
        logger.info("URL: {} size: {} interval: {}", configProvider.url, configProvider.size, configProvider.interval);
        return configProvider;
    }
}
