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

internal class DownloadSchedulerTest {

    @Test
    fun startEventHandler() {
        val eventBus = EventBus()
        val configProvider = mockk<ConfigProvider>()
        every {configProvider.interval}returns 10
        val downloadThread = mockk<DownloadThread>()
        val scheduledExecutorService = mockk<ScheduledExecutorService>()

        val downloadManager = DownloadScheduler(eventBus, mockk<DownloadThreadFactory>(), scheduledExecutorService, configProvider)

        //WHEN
        eventBus.post(StartEvent())

        //THEN
        verify(exactly = 1, timeout = 1000) { scheduledExecutorService.scheduleAtFixedRate(any(), any(), any(), any())}
    }

    @Test
    fun stopEventHandler() {
        val eventBus = EventBus()
        val configProvider = mockk<ConfigProvider>()
        every {configProvider.interval}returns 10
        val downloadThread = mockk<DownloadThread>()
        val scheduledExecutorService = mockk<ScheduledExecutorService>()
        val downloadTask = mockk<ScheduledFuture<*>>()
        every { scheduledExecutorService.scheduleAtFixedRate(downloadThread, 0, 10, TimeUnit.MINUTES) } returns downloadTask


        val downloadManager = DownloadScheduler(eventBus, mockk<DownloadThreadFactory>(), scheduledExecutorService, configProvider)

        //WHEN
        eventBus.post(StartEvent())
        eventBus.post(StopEvent())

        //THEN
        verify(exactly = 1) { downloadTask.cancel(false)}
    }

    @Test
    fun stopBeforeStart() {
        val eventBus = EventBus()
        val configProvider = mockk<ConfigProvider>()
        every {configProvider.interval}returns 10
        val downloadThread = mockk<DownloadThread>()
        val scheduledExecutorService = mockk<ScheduledExecutorService>()
        val downloadTask = mockk<ScheduledFuture<*>>()
        every { scheduledExecutorService.scheduleAtFixedRate(downloadThread, 0, 10, TimeUnit.MINUTES) } returns downloadTask


        val downloadManager = DownloadScheduler(eventBus, mockk<DownloadThreadFactory>(), scheduledExecutorService, configProvider)

        //WHEN
        eventBus.post(StopEvent())

        //THEN
    }
}