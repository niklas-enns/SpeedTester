package niklasu.speedtester;

import com.google.common.eventbus.EventBus;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.downloader.DownloadFileSizeChecker;
import niklasu.speedtester.downloader.DownloadThread;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan
public class DIConfig {
    @Bean
    public ConfigStore configStore() {
        return new ConfigStore();
    }

    @Bean
    public EventBus eventBus(){
        return new EventBus();
    }

    @Bean
    public DownloadFileSizeChecker checker(){return new DownloadFileSizeChecker();}

}
