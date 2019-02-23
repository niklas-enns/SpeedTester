package niklasu.speedtester.guice

import com.google.common.eventbus.EventBus
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService


class Module : AbstractModule() {

    private val eventBus: EventBus
        @Provides
        @Singleton
        get() = EventBus()


    override fun configure() {}

    @Provides
    @Singleton
    private fun get(): ScheduledExecutorService {
        return Executors.newSingleThreadScheduledExecutor()
    }
}
