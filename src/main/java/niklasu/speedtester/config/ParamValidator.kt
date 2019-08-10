package niklasu.speedtester.config

import com.google.inject.Inject
import niklasu.speedtester.MB
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class ParamValidator @Inject constructor(private val fileSizeChecker: FileSizeChecker) {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Throws(ValidationException::class)
    fun validate(size: Long, interval: Int, url: String) {
        if (size < 1) throw ValidationException("download size must be >=1")
        val realFileSize: Long
        try {
            val url = URL(url)
            realFileSize = fileSizeChecker.getFileSize(url)
        } catch (e: MalformedURLException) {
            throw ValidationException("Malformed URL. It has to start with http://", e)
        } catch (e: NumberFormatException) {
            throw ValidationException("Unable to get the file size for ${url}", e)
        } catch (e: IOException) {
            throw ValidationException("Unable to get the file size for ${url}", e)
        }

        if (realFileSize < MB) throw ValidationException("The size of ${url} was $realFileSize and is < $MB")
        if (realFileSize < size) throw ValidationException("${url} has a size of $realFileSize while ${size * MB} is required")

        if (interval < 1) throw ValidationException("Interval must be >= 1. Your input was ${interval}")

    }
}
