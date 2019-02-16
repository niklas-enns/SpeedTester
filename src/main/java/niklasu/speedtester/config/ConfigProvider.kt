package niklasu.speedtester.config

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.google.common.eventbus.EventBus
import com.google.inject.Inject
import com.google.inject.Singleton
import niklasu.speedtester.events.ConfigChangedEvent

@Singleton
class ConfigProvider @Inject
constructor(private val paramValidator: ParamValidator, private val eventBus: EventBus) {
    @Parameter(names = ["-size"], description = "Download size in MB")
    var size: Long = 50
        private set
    @Parameter(names = ["-interval"], description = "Download interval in minutes")
    var interval = 1
        private set
    @Parameter(names = ["-url"], description = "file URL", required = true)
    var url = ""
        private set
    @Parameter(names = ["-tray"], description = "Debug mode", arity = 1)
    val isTray = true

    @Throws(ValidationException::class)
    fun setConfig(args: Array<String>) {
        JCommander(this, *args)
        paramValidator.validate(Config(size, interval, url))
    }

    @Throws(ValidationException::class)
    fun setConfig(config: Config) {
        paramValidator.validate(config)
        this.size = config.fileSize
        this.interval = config.interval
        this.url = config.url
        eventBus.post(ConfigChangedEvent())
    }

    fun reset() {
        size = 50
        interval = 1
        eventBus.post(ConfigChangedEvent())
    }
}
