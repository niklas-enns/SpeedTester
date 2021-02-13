package niklasu.speedtester.config

import com.beust.jcommander.JCommander
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import niklasu.speedtester.logger


class ConfigModule(private val args: Array<String>) : AbstractModule() {
    private val logger = logger()

    @Provides
    private fun getArgs(): Array<String> {
        return args
    }

    @Provides
    @Singleton
    private fun getConfigProvider(args: Array<String>, paramValidator: ParamValidator): ConfigProvider {
        var argsWithUrl: Array<String> = args
        if (!args.contains("-url")) {
            argsWithUrl += "-url"
            argsWithUrl += System.getenv("DOWNLOAD_URL")
        }
        val configProvider = ConfigProvider()
        JCommander(configProvider, *argsWithUrl)
        paramValidator.validate(configProvider.size, configProvider.interval, configProvider.url)
        logger.info("URL: {} size: {} interval: {}", configProvider.url, configProvider.size, configProvider.interval)
        return configProvider
    }
}
