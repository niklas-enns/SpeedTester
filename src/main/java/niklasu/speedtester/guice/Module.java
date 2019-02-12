package niklasu.speedtester.guice;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class Module extends AbstractModule {


    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    private EventBus getEventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    private ScheduledExecutorService get() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
