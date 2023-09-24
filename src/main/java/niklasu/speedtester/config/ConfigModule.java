package niklasu.speedtester.config;

import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigModule  extends AbstractModule {
    private final Logger logger = LoggerFactory.getLogger("ConfigModule");
    private final String[] args;

    public ConfigModule(String[] args) {
        this.args = args;
    }

    @Provides
    private String[] getArgs(){
        return args;
    }

    @Provides
    @Singleton
    private ConfigProvider getConfigProvider(String[] args, ParamValidator paramValidator) throws ValidationException {
        final List<String> argsWithUrl = Arrays.asList(args);
        if (!argsWithUrl.contains("-url")) {
            argsWithUrl.add("-url");
            argsWithUrl.add(System.getenv("DOWNLOAD_URL"));
        }
        final ConfigProvider configProvider = new ConfigProvider();
        new JCommander(configProvider, argsWithUrl.toArray(new String[0]));
        paramValidator.validate(configProvider.size, configProvider.interval, configProvider.url);
        logger.info("URL: {} size: {} interval: {}", configProvider.url, configProvider.size, configProvider.interval);
        return configProvider;
    }
}
