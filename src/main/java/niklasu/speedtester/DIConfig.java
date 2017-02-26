package niklasu.speedtester;

import com.google.common.eventbus.EventBus;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.downloader.DownloadFileSizeChecker;
import niklasu.speedtester.downloader.DownloadMgr;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan
public class DIConfig {
    @Bean
    public EventBus eventBus(){
        return new EventBus();
    }

}
