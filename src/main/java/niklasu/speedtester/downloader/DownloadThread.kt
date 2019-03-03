package niklasu.speedtester.downloader

import com.google.common.eventbus.EventBus

import com.google.inject.Inject
import niklasu.speedtester.MB
import niklasu.speedtester.events.ResultEvent
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels
import java.util.*
open class DownloadThread @Inject
constructor(private val eventBus: EventBus, val targetFile: URL, val downloadsizeInMB: Long, private val tempFile: File, private val fileRemover: FileRemover) : Thread() {

    companion object {
        private val logger = LoggerFactory.getLogger(DownloadThread::class.java)
    }

    override fun run() {
        val startOfDownload = Date()
        val startTime = startOfDownload.time
        try {
            download()
            val runtime = Date().time - startTime
            val resultSpeed = downloadsizeInMB.toDouble() / (runtime.toDouble() / 1000.0) * 8.0
            fileRemover.remove(tempFile)
            eventBus.post(ResultEvent(startOfDownload, resultSpeed))
        } catch (e: DownloadException) {
            logger.error("Download failed, because", e)
            eventBus.post(ResultEvent(startOfDownload, -1.0))
        }
    }

    @Throws(DownloadException::class)
    private fun download() {
        try {
            logger.debug(String.format("Starting download of %d MB from %s", downloadsizeInMB, targetFile.toString()))
            val rbc = Channels.newChannel(targetFile.openStream())
            val fos = FileOutputStream(tempFile)
            fos.channel.transferFrom(rbc, 0, downloadsizeInMB * MB)
            rbc.close()
            fos.close()

        } catch (e: IOException) {
            throw DownloadException("", e)
        }

    }
    class FileRemover {
        fun remove(file: File) {
            file.delete()
        }
    }
}
