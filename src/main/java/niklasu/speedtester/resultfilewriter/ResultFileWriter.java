package niklasu.speedtester.resultfilewriter;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import niklasu.speedtester.events.ResultEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Component
public class ResultFileWriter {
    @Autowired
    private EventBus eventBus;

    @PostConstruct
    private void init() {
        eventBus.register(this);
    }

    @Subscribe
    public void appendToLogFile(ResultEvent result) {
        File file = new File("results.txt");
        FileWriter writer;
        try {
            //init writer with append
            writer = new FileWriter(file, true);
            String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(result.getDate());
            writer.write("" + date + " " + String.format("%.2f", result.getSpeed()) + " Mbit/s");
            writer.write(System.getProperty("line.separator"));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
