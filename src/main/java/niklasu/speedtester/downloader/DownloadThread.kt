package niklasu.speedtester.downloader

import com.google.inject.Inject
import niklasu.speedtester.MB
import niklasu.speedtester.measurements.Measurements
import niklasu.speedtester.ui.ConsoleResultPrinter
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URL
import java.util.*

open class DownloadThread @Inject
constructor(private val targetFile: URL, private val downloadsizeInMB: Long, private val consoleResultPrinter: ConsoleResultPrinter, val measurements: Measurements) : Thread() {
    //for testing purposes
    var downloadedBytes: Long = 0

    companion object {
        private val logger = LoggerFactory.getLogger(DownloadThread::class.java)
    }

    override fun run() {
        downloadedBytes = 0
        try {
            val startTime = Date().time
            download()
            val runtime = Date().time - startTime
            val resultSpeed = (downloadedBytes.toDouble() / MB) / (runtime.toDouble() / 1000.0) * 8.0
            consoleResultPrinter.show(resultSpeed)
            measurements.add(resultSpeed)
        } catch (e: DownloadException) {
            logger.error("Download failed, because", e)
        }
    }

    @Throws(DownloadException::class)
    private fun download() {
        try {
            val bufferSize = 1 * MB
            val byteBuffer = ByteArray(bufferSize)
            val stream = targetFile.openStream()

            while (downloadedBytes < downloadsizeInMB * MB) {
                val read = stream.read(byteBuffer, 0, bufferSize)
                downloadedBytes += read
                consoleResultPrinter.showProgress(downloadedBytes, downloadsizeInMB * MB)
            }
            logger.debug("Downloaded ${downloadedBytes} Bytes ~ ${downloadedBytes / MB} MB")
        } catch (e: IOException) {
            throw DownloadException("", e)
        }

    }
}
