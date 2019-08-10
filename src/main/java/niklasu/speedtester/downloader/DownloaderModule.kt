package niklasu.speedtester.downloader

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import niklasu.speedtester.config.ConfigProvider
import java.io.File
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService


class DownloaderModule : AbstractModule() {
    @Provides
    @Singleton
    private fun get(): ScheduledExecutorService {
        return Executors.newSingleThreadScheduledExecutor()
    }

    @Provides
    private fun getThread(configProvider: ConfigProvider): DownloadThread {
        return DownloadThread(URL(configProvider.url), configProvider.size, getTempFile(), DownloadThread.FileRemover())
    }

    private fun getTempFile(): File {
        val tempFile = File.createTempFile("SpeedTester-", "-lol")
        tempFile.deleteOnExit()
        return tempFile
    }
}