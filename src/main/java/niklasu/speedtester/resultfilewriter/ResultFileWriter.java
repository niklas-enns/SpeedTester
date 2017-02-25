package niklasu.speedtester.resultfilewriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by enzo on 25.02.2017.
 */
public class ResultFileWriter {
    ResultFileWriter(){
        //TODO register EventReceiver for results
    }
    public void appendToLogFile(Date dateOfDownload, double resultSpeed) {
        File file = new File("results.txt");
        FileWriter writer;
        try {
            //init writer with append
            writer = new FileWriter(file, true);
            String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(dateOfDownload);
            writer.write("" + date + " " + String.format("%.2f", resultSpeed) + " Mbit/s");
            writer.write(System.getProperty("line.separator"));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
