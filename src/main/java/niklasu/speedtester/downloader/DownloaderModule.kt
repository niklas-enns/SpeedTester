package niklasu.speedtester.downloader

import com.google.inject.AbstractModule
import com.google.inject.Provides
import niklasu.speedtester.config.ConfigProvider
import niklasu.speedtester.ui.ConsoleResultPrinter
import java.net.URL


class DownloaderModule : AbstractModule() {
    @Provides
    private fun getThread(configProvider: ConfigProvider, consoleResultPrinter: ConsoleResultPrinter): DownloadThread {
        return DownloadThread(URL(configProvider.url), configProvider.size, consoleResultPrinter)
    }

}