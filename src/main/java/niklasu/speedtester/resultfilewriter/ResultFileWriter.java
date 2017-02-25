package niklasu.speedtester.resultfilewriter;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import niklasu.speedtester.Result;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ResultFileWriter {
    private EventBus eventBus;

    public ResultFileWriter(EventBus eventBus){
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Subscribe
    public void appendToLogFile(Result result) {
        File file = new File("results.txt");
        FileWriter writer;
        try {
            //init writer with append
            writer = new FileWriter(file, true);
            String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(result.date);
            writer.write("" + date + " " + String.format("%.2f", result.speed) + " Mbit/s");
            writer.write(System.getProperty("line.separator"));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
