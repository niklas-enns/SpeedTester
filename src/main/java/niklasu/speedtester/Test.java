package niklasu.speedtester;

import com.google.inject.Inject;

import java.util.concurrent.ScheduledExecutorService;

public class Test {
    private ScheduledExecutorService scheduledExecutorService;

    @Inject
    public Test(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }
}
