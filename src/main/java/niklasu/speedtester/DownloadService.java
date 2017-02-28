package niklasu.speedtester;

import com.google.common.eventbus.EventBus;
import niklasu.speedtester.interfaces.Constants;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
@ComponentScan
public class DownloadService implements Constants {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static String[] myargs = new String[0];

    public static void main(String[] args) throws Exception {
        myargs = args;
        ApplicationContext context = new AnnotationConfigApplicationContext(DownloadService.class);
    }

    //@Subscribe
    //public void configChangedHandler(DeadEvent deadEvent){
    //    System.out.println("Dafür interessiert sich sonst niemand...");
    //}
    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}
