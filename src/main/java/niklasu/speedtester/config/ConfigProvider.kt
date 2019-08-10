package niklasu.speedtester.config

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.google.inject.Inject
import com.google.inject.Singleton

@Singleton
class ConfigProvider @Inject
constructor(private val paramValidator: ParamValidator) {
    @Parameter(names = ["-size"], description = "Download size in MB")
    var size: Long = 50
        private set
    @Parameter(names = ["-interval"], description = "Download interval in minutes")
    var interval = 1
        private set
    @Parameter(names = ["-url"], description = "file URL", required = true)
    var url = ""
        private set

    @Throws(ValidationException::class)
    fun setConfig(args: Array<String>) {
        JCommander(this, *args)
        paramValidator.validate(Config(size, interval, url))
    }
}
