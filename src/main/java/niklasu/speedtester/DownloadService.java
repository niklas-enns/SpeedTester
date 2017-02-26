package niklasu.speedtester;

import com.google.common.eventbus.EventBus;
import niklasu.speedtester.downloader.DownloadFileSizeChecker;
import niklasu.speedtester.interfaces.Constants;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@ComponentScan
public class DownloadService implements Constants {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    public static String[] myargs = new String[0];

    public static void main(String[] args) throws Exception {
        myargs = args;
        ApplicationContext context = new AnnotationConfigApplicationContext(DownloadService.class);
        log.setLevel(Level.INFO);
        //TODO ensure Thread-safety. DownloadMgr must not start a download before ConfigStore isnt initialized
        /*
        Status quo: ConfigReader pusht, wenn er fertig ist, in Configstore rein. Also ist ConfigReader davon abhängig.
        Kann DownloadMgr davor schon daten aus dem ConfigStore geholt haben?
        Entweder wird ConfigStore von ConfigReader erstellt. Nein!
         */
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}
