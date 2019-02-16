package niklasu.speedtester.downloader

import com.google.common.eventbus.EventBus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import niklasu.speedtester.config.ConfigProvider
import niklasu.speedtester.events.StartEvent
import niklasu.speedtester.events.StopEvent
import org.junit.jupiter.api.Test
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

internal class DownloadManagerTest {

    @Test
    fun startEventHandler() {
        val eventBus = EventBus()
        val configProvider = mockk<ConfigProvider>()
        every {configProvider.interval}returns 10
        val downloadThread = mockk<DownloadThread>()
        val scheduledExecutorService = mockk<ScheduledExecutorService>()

        val downloadManager = DownloadManager(eventBus, configProvider, downloadThread, scheduledExecutorService)

        //WHEN
        eventBus.post(StartEvent())

        //THEN
        verify(exactly = 1) { scheduledExecutorService.scheduleAtFixedRate(downloadThread, 0, 10, TimeUnit.MINUTES)}
    }

    @Test
    fun stopEventHandler() {
        val eventBus = EventBus()
        val configProvider = mockk<ConfigProvider>()
        every {configProvider.interval}returns 10
        val downloadThread = mockk<DownloadThread>()
        val scheduledExecutorService = mockk<ScheduledExecutorService>()
        val mockk = mockk<ScheduledFuture<*>>()
        every { scheduledExecutorService.scheduleAtFixedRate(downloadThread, 0, 10, TimeUnit.MINUTES) } returns mockk


        val downloadManager = DownloadManager(eventBus, configProvider, downloadThread, scheduledExecutorService)

        //WHEN
        eventBus.post(StartEvent())
        eventBus.post(StopEvent())

        //THEN
        verify(exactly = 1) { mockk.cancel(false)}
    }
}