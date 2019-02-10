package niklasu.speedtester.config

import com.google.inject.Inject
import niklasu.speedtester.Constants.MB
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class KotlinParamValidator @Inject constructor(private val fileSizeChecker: FileSizeChecker) : ParamValidator {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Throws(ValidationException::class)
    override fun validate(config: Config) {
        logger.debug("Validating {}", config)
        if (config.fileSize < 1) throw ValidationException("download size must be >=1")
        val realFileSize: Long
        try {
            val url = URL(config.url)
            realFileSize = fileSizeChecker.getFileSize(url)
        } catch (e: MalformedURLException) {
            throw ValidationException("Malformed URL. It has to start with http://", e)
        } catch (e: NumberFormatException) {
            throw ValidationException("Unable to get the file size for ${config.url}")
        } catch (e: IOException) {
            throw ValidationException("Unable to get the file size for ${config.url}")
        }

        if (realFileSize < MB) throw ValidationException("The size of ${config.url} was $realFileSize and is < $MB")
        if (realFileSize<config.fileSize) throw ValidationException("${config.url} has a size of $realFileSize while ${config.fileSize * MB} is required")

        if (config.interval < 1) throw ValidationException("Interval must be >= 1. Your input was ${config.interval}")

    }
}
