package niklasu.speedtester.guice;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class ScheduledExecutorServiceModule extends AbstractModule {
    private final static EventBus eventBus = new EventBus();

    @Override
    protected void configure() {
        bind(ScheduledExecutorService.class).toProvider(Executors::newSingleThreadScheduledExecutor);
        bind(EventBus.class).toInstance(eventBus);
    }

}
