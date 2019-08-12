package niklasu.speedtester.downloader

import com.google.inject.Inject
import niklasu.speedtester.KB
import niklasu.speedtester.MB
import niklasu.speedtester.ui.ConsoleResultPrinter
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URL
import java.util.*

open class DownloadThread @Inject
constructor(private val targetFile: URL, private val downloadsizeInMB: Long, private val consoleResultPrinter: ConsoleResultPrinter) : Thread() {
    //for testing purposes
    var downloadedBytes = 0

    companion object {
        private val logger = LoggerFactory.getLogger(DownloadThread::class.java)
    }

    override fun run() {
        downloadedBytes = 0
        try {
            val startTime = Date().time
            download()
            val runtime = Date().time - startTime
            consoleResultPrinter.show(downloadsizeInMB.toDouble() / (runtime.toDouble() / 1000.0) * 8.0)
        } catch (e: DownloadException) {
            logger.error("Download failed, because", e)
        }
    }

    @Throws(DownloadException::class)
    private fun download() {
        try {
            val byteBuffer = ByteArray(1 * KB)
            val stream = targetFile.openStream()

            while (downloadedBytes < downloadsizeInMB * MB) {
                val read = stream.read(byteBuffer, 0, 1 * KB)
                downloadedBytes += read
            }
            logger.debug("Downloaded ${downloadedBytes} Bytes ~ ${downloadedBytes / MB} MB")
        } catch (e: IOException) {
            throw DownloadException("", e)
        }

    }
}
