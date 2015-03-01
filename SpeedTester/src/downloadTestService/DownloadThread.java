package downloadTestService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by enzo on 06.11.2014.
 */
public class DownloadThread extends Thread {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    URL targetFile;
    int dlsize;
    DownloadService host;
    Date startOfDownload = new Date();

    public DownloadThread(int size, DownloadService h, URL url){
        targetFile=url;
        dlsize =size;
        host = h;
    }

    public void run()
    {
        long startTime = startOfDownload.getTime();
        download();
        long runningTime = new Date().getTime() - startTime;
        double resultSpeed = ((double) dlsize / ((double) runningTime / (double) 1000)) * 8.;
        log.info("Download of "+dlsize+" MB completed, calculated Speed: " + String.format( "%.2f", resultSpeed )  + " MBit/s");
        sendResultToHost(startOfDownload,resultSpeed);
        appendToLogFile(startOfDownload,resultSpeed);

    }

    private void download(){
        try{
            log.info("Starting download...");
            ReadableByteChannel rbc = Channels.newChannel(targetFile.openStream());
            FileOutputStream fos = new FileOutputStream("temp.data");
            fos.getChannel().transferFrom(rbc, 0, 1000 * 1000 * dlsize);
        }catch (Exception e){
            log.severe("Could not download file");
        }
    }
    private void sendResultToHost(Date dateOfDownload, double resultSpeed){
        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");
        String time = sdftime.format(dateOfDownload);

        host.setResult(time+" - "+String.format( "%.2f", resultSpeed )+ " Mbit/s");
    }
    private void appendToLogFile(Date dateOfDownload,double resultSpeed){
        File file = new File("results.txt");
        FileWriter writer;
        try {
            //init writer with append
            writer = new FileWriter(file, true);

            String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(dateOfDownload);

            writer.write(""+date+" "+String.format( "%.2f", resultSpeed )+" Mbit/s");
            writer.write(System.getProperty("line.separator"));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
